package com.isco.upc.app.domain;

public class Contact {

	private String email;
	private String firstName;
	private String lastName;
	private Boolean emailDisabled;

	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public Boolean getEmailDisabled() {
		return emailDisabled;
	}
	public void setEmailDisabled(Boolean emailDisabled) {
		this.emailDisabled = emailDisabled;
	}
	
	
}
