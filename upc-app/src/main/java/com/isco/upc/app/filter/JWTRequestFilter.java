package com.isco.upc.app.filter;

import java.util.regex.Pattern;

import javax.annotation.Priority;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.isco.upc.common.enums.ErrorCode;
import com.isco.upc.common.exceptions.UPCException;
import com.isco.upc.security.annotations.Secured;
import com.isco.upc.security.beans.JWTPrincipal;
import com.isco.upc.security.beans.JWTSecurityContext;
import com.isco.upc.security.utils.AuthorizationCheck;
import com.isco.upc.security.utils.TokenUtils;





/**
 * A JAX-RS Request Filter to intercept authorization header and verify the validity of JWT
 * token using a JWT library and shared signing secret from IDP.
 * <p>
 * {@code
 *  <system-properties>
         <property name="api.security.keystore.file" value="secure-keystore.jks"/>
         <property name="api.security.keystore.password" value="changeit"/>
         <property name="api.security.key.alias" value="jwt"/>
 *  /system-properties>
 * }
 * </p>
 */
@Provider
@Priority(Priorities.AUTHENTICATION)
@Secured
public class JWTRequestFilter implements ContainerRequestFilter {
	
    private static final Logger LOGGER = LoggerFactory.getLogger(JWTRequestFilter.class);

    @Context
    private ResourceInfo resourceInfo;

    /**
     * Instantiates a new JWT verifier with signing secret.
     *
     * @throws Exception in case of error setting up JWS Verifier
     */
    public JWTRequestFilter() throws Exception {
    	//Load keys
       TokenUtils.getInstance();
    }

    /*
     * (non-Javadoc)
     * @see javax.ws.rs.container.ContainerRequestFilter#filter(javax.ws.rs.container.ContainerRequestContext)
     */
    @Override
    public void filter(final ContainerRequestContext requestContext) {
    	LOGGER.info("auth filter ...");
        String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
        if (authorizationHeader != null) {
            String token = TokenUtils.getInstance().parseBearerToken(authorizationHeader);
            if (token != null) {
                JWTPrincipal principal  = TokenUtils.getInstance().validateTokenAndBuildPrincipal(token);
                if (principal != null) {
                    // Build and inject JavaEE SecurityContext for @RoleAllowed, isUserInRole(), getUserPrincipal() to work
                    JWTSecurityContext ctx = new JWTSecurityContext(
                                                    principal,
                                                    requestContext.getSecurityContext().isSecure());
                    requestContext.setSecurityContext(ctx);
                    AuthorizationCheck.checkRoleAndPermissions(resourceInfo.getResourceClass(), resourceInfo.getResourceMethod(), principal.getRoles(), principal.getPermissions());
                } else {
                    throw new UPCException(ErrorCode.UNAUTHORIZED, "Unauthorized: Unable to extract claims from JWT");
                }
            } else {
                throw new UPCException(ErrorCode.UNAUTHORIZED, "Unauthorized: Unable to parse Bearer token");
            }
        } else {
            throw new UPCException(ErrorCode.UNAUTHORIZED, "Unauthorized: No Authorization header was found");
        }
    }


}