package com.availaboard.engine.resource;

import com.availaboard.UI.frontend_functionality.ResourceFieldLoader;

public class User extends Resource {
	
	@ResourceFieldLoader
	private String firstName;
	@ResourceFieldLoader
	private String lastName;
	@ResourceFieldLoader
	private String email;
	@ResourceFieldLoader
	private String username;
	
	private String password;
	
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
		this.email = email;
	}
	
	@Override
	public String toString() {
		return "User";
	}
}
