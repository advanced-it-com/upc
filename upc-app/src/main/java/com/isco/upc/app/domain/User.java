package com.isco.upc.app.domain;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.isco.upc.common.enums.UserPermission;
import com.isco.upc.common.enums.UserRole;



@Document
public class User {

	@Id
	private String userId;
	private String username;
	private String password;
	private String email;
	
	private Boolean actif;
	
	private Date lastPasswordReset;
	private List<UserRole> roles;
	private List<UserPermission> permissions;
	private String companyId;

	public User() {
		super();
	}

	public User(String companyId, String username, String password, String email, Date lastPasswordReset, List<UserRole> roles, List<UserPermission> permissions) {
		this.setCompanyId(companyId);
		this.setUsername(username);
		this.setPassword(password);
		this.setEmail(email);
		this.setLastPasswordReset(lastPasswordReset);
		this.setRoles(roles);
		this.setPermissions(permissions);
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getLastPasswordReset() {
		return lastPasswordReset;
	}

	public void setLastPasswordReset(Date lastPasswordReset) {
		this.lastPasswordReset = lastPasswordReset;
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
	
	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public Boolean getActif() {
		return actif;
	}

	public void setActif(Boolean actif) {
		this.actif = actif;
	}

	@Override
	public String toString() {
		return "UserDetail [userId=" + userId + ", userName=" + username + ", password=" + password
				+ ", email=" + email + "]";
	}
	
}
