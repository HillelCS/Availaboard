package com.availaboard.engine.resource;

public class User extends Resource {
	
	private String firstName;
	private String lastName;
	private String email;
	
	private String fullname = firstName + " " + lastName;
	
	
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
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		super.setContact(email);
		this.email = email;
	}
	
	@Override
	public String toString() {
		return this.getClass().toString();
	}
}
