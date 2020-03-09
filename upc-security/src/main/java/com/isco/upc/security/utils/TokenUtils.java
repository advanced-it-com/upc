package com.isco.upc.security.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.isco.upc.common.enums.UserPermission;
import com.isco.upc.common.enums.UserRole;
import com.isco.upc.security.beans.JWTPrincipal;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.JWTParser;
import com.nimbusds.jwt.SignedJWT;


public class TokenUtils {

	  private static final Logger log = LoggerFactory.getLogger(TokenUtils.class); 
	    public static final String PROP_SECURITY_KEYSTORE  = "api.security.keystore.file";
	    public static final String PROP_SECURITY_PASSWORD  = "api.security.keystore.password";
	    public static final String PROP_SECURITY_KEY_ALIAS = "api.security.key.alias";

	    private static final String DEFAULT_KEYSTORE_KEY_ALIAS  = "jwt";
	    private static final String DEFAULT_KEYSTORE_PASSWORD   = "password";
	    private static final String DEFAULT_KEYSTORE            = "keystore.jks"; 

	    private Pattern     tokenPattern  = Pattern.compile("^Bearer$", Pattern.CASE_INSENSITIVE);
	    
	    private JWSVerifier jwsVerifier;
	    private JWSSigner jwsSigner;
	    

	    private TokenUtils(){
	    	String keystore = System.getProperty(PROP_SECURITY_KEYSTORE,  DEFAULT_KEYSTORE);
	        String password = System.getProperty(PROP_SECURITY_PASSWORD,  DEFAULT_KEYSTORE_PASSWORD);
	        String alias    = System.getProperty(PROP_SECURITY_KEY_ALIAS, DEFAULT_KEYSTORE_KEY_ALIAS);
	 
	        // Create RSA-signer with the private key 
	        RSAPrivateKey privateKey = loadPrivateKey(keystore, password, alias);
	        if (privateKey != null){
	        	 jwsSigner = new RSASSASigner(privateKey); 
	        } else {
	        	  throw new RuntimeException("Configuration error: unable to load JWT signing private key from keystore: " + keystore);

	        }

	         PublicKey publicKey = loadPublicKey(keystore, password, alias);
	         if (publicKey != null) {
	             jwsVerifier = new RSASSAVerifier((RSAPublicKey) publicKey);
	         } else {
	             throw new RuntimeException("Configuration error: unable to load JWT signing public key from keystore: " + keystore);
	         }
	    }
	    
	    /** Instance unique non pr�initialis�e */
		private static TokenUtils INSTANCE = null;
	 
		/** Point d'acc�s pour l'instance unique du singleton */
		public static TokenUtils getInstance()
		{			
			if (INSTANCE == null)
			{ 	INSTANCE = new TokenUtils();	
			}
			return INSTANCE;
		}
	 
	    public String generateToken(JWTPrincipal user) { 

	        // Compose the JWT claims set
	        com.nimbusds.jwt.JWTClaimsSet.Builder jwtClaims = new JWTClaimsSet.Builder();
	        jwtClaims.issuer(user.getName());
	        jwtClaims.subject(user.getUserId());
	        jwtClaims.claim("email", user.getEmail()); 
	        jwtClaims.claim("organisationId", user.getOrganisationId()); 
	        jwtClaims.claim("roles", user.getRoles()); 
	        jwtClaims.claim("permissions", user.getPermissions()); 
	        
	        // Set expiration in 10 minutes
	        jwtClaims.expirationTime(new Date(new Date().getTime() + 60 * 60 * 1000 * 24)); //1 day
	        jwtClaims.notBeforeTime(new Date());
	        jwtClaims.issueTime(new Date());
	        jwtClaims.jwtID(UUID.randomUUID().toString());
	       
	
	 
	        SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.RS256), jwtClaims.build()); 
	 
	        // Compute the RSA signature 
	        try {
				signedJWT.sign(jwsSigner);
			} catch (JOSEException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
	 
	        // To serialize to compact form, produces something like 
	        // eyJhbGciOiJSUzI1NiJ9.SW4gUlNBIHdlIHRydXN0IQ.IRMQENi4nJyp4er2L 
	        // mZq3ivwoAjqa1uUkSBKFIX7ATndFF5ivnt-m8uApHO4kfIFOrW7w2Ezmlg3Qd 
	        // maXlS9DhN0nUk_hGI3amEjkKd0BWYCB8vfUbUv0XGjQip78AI4z1PrFRNidm7 
	        // -jPDm5Iq0SZnjKjCNS5Q15fokXZc8u0A 
	 
	        return signedJWT.serialize(); 
	    	
	    } 
	    
	    /**
	     * Extract Bearer token value from string "Bearer [value]".
	     *
	     * @param bearerToken
	     *            The Bearer token string of the form "Bearer [value]"
	     * @return The value part of the token if scheme (prefix) matches with
	     *         Bearer, null otherwise
	     */
	    public String parseBearerToken(final String bearerToken) {
	        String tokenValue = null;
	        if (bearerToken  != null) {
	            String[] parts = bearerToken.split(" ");
	            if (parts.length == 2) {
	                String scheme       = parts[0];
	                String credentials  = parts[1];
	                if (tokenPattern.matcher(scheme).matches()) {
	                    tokenValue = credentials;
	                }
	            }
	        }
	        return tokenValue;
	    }

	    
	    
	    public JWTPrincipal validateTokenAndBuildPrincipal(final String token) {
	        JWTClaimsSet claims     = TokenUtils.getInstance().validateToken(token);
           return TokenUtils.getInstance().buildPrincipal(claims);
	    }
	    
	    
	    
	    /**
	     * Creates a new instance of {@link JWTPrincipal} from JSON Web Token (JWT)
	     * claims.
	     * @param claims
	     *            The JWT claims set
	     * @return A new instance of {@link JWTPrincipal} from JSON Web Token (JWT)
	     *         claims
	     */
		@SuppressWarnings("unchecked")
		public JWTPrincipal buildPrincipal(final JWTClaimsSet claims) {
	        JWTPrincipal principal = null;

	        try {
	            if (claims != null) {
	                String userId   = claims.getSubject();
	                String username   = claims.getIssuer();
	                String email     = (String) claims.getClaim("email");
	                String organisationId     = (String) claims.getClaim("organisationId");
	                List<UserRole> roles = ((List<String>) claims.getClaim("roles")).stream().map(UserRole::valueOf).collect(Collectors.toList());
	                List<UserPermission> permissions = new ArrayList<UserPermission>();
	                if (claims.getClaim("permissions") != null){
	                 permissions = ((List<String>) claims.getClaim("permissions")).stream().map(UserPermission::valueOf).collect(Collectors.toList());
	                }
	                // TODO: Extract custom attributes, e.g. roles, organization affiliation etc. and put into principal.
	                principal = new JWTPrincipal(userId, username, email, organisationId, roles, permissions);
	            }
	        } catch (Exception e) {
	            log.error(e.getMessage(), e);
	        }
	        return principal;
	    }
		

	    /**
	     * Validate the JSON Web Token for signature, expiration and not before
	     * time.
	     *
	     * @param token
	     *            The JSON Web Token
	     * @return {@link JWTClaimsSet} in case of success, null otherwise
	     */
	    public JWTClaimsSet validateToken(final String token) {
	        JWTClaimsSet claims = null;

	        try {
	            JWT jwt = JWTParser.parse(token);
	            if (jwt instanceof SignedJWT) {
	                SignedJWT signedJWT = (SignedJWT) jwt;
	                if (signedJWT.verify(jwsVerifier)) {
	                    claims = signedJWT.getJWTClaimsSet();
	                    log.trace("JWT claims: {}", claims.getClaims());

	                    Date expirationTime = claims.getExpirationTime();
	                    Date now = new Date();
	                    Date notBeforeTime = claims.getNotBeforeTime();
	                    if (notBeforeTime.compareTo(now) > 0) {
	                        throw new NotAuthorizedException(
	                                    "Unauthorized: too early, token not valid yet",
	                                    Response.status(Status.UNAUTHORIZED));
	                    }
	                    if (expirationTime.compareTo(now) <= 0) {
	                        throw new NotAuthorizedException(
	                                    "Unauthorized: too late, token expired",
	                                    Response.status(Status.UNAUTHORIZED));
	                    }
	                } else {
	                    throw new NotAuthorizedException(
	                                "Unauthorized: Unable to verify Bearer token",
	                                Response.status(Status.UNAUTHORIZED));
	                }
	            } else {
	                throw new NotAuthorizedException(
	                            "Unauthorized: Unexpected JWT type",
	                            Response.status(Status.UNAUTHORIZED));
	            }
	        } catch (ParseException | JOSEException e) {
	            throw new NotAuthorizedException(
	                        e.getMessage(),
	                        Response.status(Status.UNAUTHORIZED),
	                        e);
	        }
	        return claims;
	    }
	 
	    private RSAPrivateKey loadPrivateKey(String keyStorePath, String keyStorePassword, String alias) { 
	 
	        InputStream inputStream = null; 
	        RSAPrivateKey privateKey = null;
	        try { 
	            inputStream = this.getClass().getClassLoader().getResourceAsStream(keyStorePath); 
	            KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType()); 
	            keystore.load(inputStream, keyStorePassword.toCharArray()); 
	 
	            privateKey = (RSAPrivateKey) keystore.getKey(alias, keyStorePassword.toCharArray()); 
	        }catch (Exception e) {
	            log.error(e.getMessage(), e);
	        } finally { 
	            if (inputStream != null) { 
	                try {
						inputStream.close();
					} catch (IOException e) {
						//Nothing to do
					} 
	            } 
	        }
            return privateKey; 
	    } 
	    
	    
	    /**
	     * Gets public key from a JKS keystore.
	     *
	     * @param keystoreFile
	     *            The keystore file pathname
	     * @param password
	     *            The keystore password
	     * @param alias
	     *            The key alias name
	     * @return {@link RSAPublicKey} for the alias if found, null otherwise
	     */
	    private PublicKey loadPublicKey(String keystoreFile, String password, String alias) {
	        PublicKey publicKey = null;
	        log.debug("Loading public key: {} from keystore: {}", alias, keystoreFile);
	        try {
	            KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());

	            File file = new File(keystoreFile);
	            InputStream is = null;
	            if (file.exists()) {
	                is = new BufferedInputStream(new FileInputStream(file));
	            } else {
	            	is = this.getClass().getClassLoader().getResourceAsStream(keystoreFile); 
	               // is = getClass().getResourceAsStream(keystoreFile);
	            }

	            if (is != null) {
	              keystore.load(is, password.toCharArray());
	              Certificate cert = keystore.getCertificate(alias);
	              if (cert != null) {
	                  publicKey = cert.getPublicKey();
	              } else {
	                  log.error("Invalid key alias provided, key not found");
	              }
	            } else {
	                log.error("Unable to load keystore file: {}", keystoreFile);
	            }
	        } catch (Exception e) {
	            log.error(e.getMessage(), e);
	        }
	        return publicKey;
	    }
}
