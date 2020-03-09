package com.isco.upc.security.beans;

import java.security.Principal;
import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.SecurityContext;

/**
 * Implements {@link SecurityContext} to create a custom context from JWT token.
 */
public class JWTSecurityContext implements SecurityContext {
    private JWTPrincipal principal;
    private boolean      isSecure;
    private Set<String>  roles = new HashSet<>();

    /**
     * Init context.
     *
     * @param principal
     *            The {@link JWTPrincipal}
     * @param isSecure
     *            true if secure, false otherwise
     */
    public JWTSecurityContext(final JWTPrincipal principal, final boolean isSecure) {
        this.principal  = principal;
        this.isSecure   = isSecure;
//        String[] names  = principal.getRoles();
//        for (int iIndex = 0; names != null && iIndex < names.length; ++iIndex) {
//            roles.add(names[iIndex]);
//        }
//        names = principal.getOrganizations();
//        for (int iIndex = 0; names != null && iIndex < names.length; ++iIndex) {
//            roles.add(names[iIndex]);
//        }
        //log.trace("JWTSecurityContext() - principal: {}, roles: {}, isSecure: {}", principal, roles);
    }

    /*
     * (non-Javadoc)
     * @see javax.ws.rs.core.SecurityContext#getAuthenticationScheme()
     */
    @Override
    public String getAuthenticationScheme() {
        return "JWT"; // informational
    }

    /*
     * (non-Javadoc)
     * @see javax.ws.rs.core.SecurityContext#getUserPrincipal()
     */
    @Override
    public Principal getUserPrincipal() {
        return principal;
    }

    /*
     * (non-Javadoc)
     * @see javax.ws.rs.core.SecurityContext#isSecure()
     */
    @Override
    public boolean isSecure() {
        return isSecure;
    }

    /*
     * (non-Javadoc)
     * @see javax.ws.rs.core.SecurityContext#isUserInRole(java.lang.String)
     */
    @Override
    public boolean isUserInRole(final String role) {
        return roles.contains(role);
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("JWTSecurityContext {")
               .append("principal:").append(principal).append(",")
               .append("roles:").append(roles).append(",")
               .append("isSecure:").append(isSecure)
               .append("}");
        return builder.toString();
    }
}