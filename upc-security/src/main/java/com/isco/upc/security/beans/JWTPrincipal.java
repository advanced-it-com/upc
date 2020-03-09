package com.isco.upc.security.beans;

import java.security.Principal;
import java.util.List;

import com.isco.upc.common.enums.UserPermission;
import com.isco.upc.common.enums.UserRole;

/**
 * Implements {@link Principal} to represent a username, role etc. for JAAS Subject.
 * An instance of {@link Principal} is obtained from call to SecurityContext.getUserPrincipal().
 * The default returned type is above principal interface which provides just principal.getName().
 *
 * In order to get more information about the logged-in user, type cast to JWTPrincipal.
 * {@code String email = (JWTPrincipal) principal.getEmail();}
 */
public class JWTPrincipal implements Principal {
    private String userId;
    private String name;
    private String email;
    private List<UserRole> roles;
    private List<UserPermission> permissions;
    private String organisationId;
    /**
     * Init {@link JWTPrincipal}.
     *
     * @param name
     *            Unique name of the user
     * @param email
     *            Email of the user
     * @param firstName
     *            First name of the user
     * @param lastName
     *            Last name of the user
     * @param username
     *            The username
     */
    public JWTPrincipal(
            final String userId,
            final String username,
            final String email,
            final String organisationId,
            final List<UserRole> roles,
            final List<UserPermission> permissions
            ) {
        this.userId       = userId;
        this.name     = username;
        this.email      = email;
        this.organisationId = organisationId;
        this.roles      = roles;
        this.roles      = roles;
    }

    /* (non-Javadoc)
     * @see java.security.Principal#getName()
     */
    @Override
    public String getName() {
        return name;
    }
    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }



	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public List<UserRole> getRoles() {
		return roles;
	}

	public void setRoles(List<UserRole> roles) {
		this.roles = roles;
	}

	public List<UserPermission> getPermissions() {
		return permissions;
	}

	public void setPermissions(List<UserPermission> permissions) {
		this.permissions = permissions;
	}
	
	

	public String getOrganisationId() {
		return organisationId;
	}

	public void setOrganisationId(String organisationId) {
		this.organisationId = organisationId;
	}

	/* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("JWTPrincipal {")
        	   .append("userId:").append(userId).append(",")
               .append("name:").append(name).append(",")
               .append("email:").append(email).append(",")
               .append("organisationId:").append(organisationId).append(",")
               .append("roles:").append(roles.toString())
               .append("permissions:").append(permissions.toString())
               .append("}");
        return builder.toString();
    }
}