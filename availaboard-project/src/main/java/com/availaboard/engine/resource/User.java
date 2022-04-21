package com.availaboard.engine.resource;

public class User extends Resource {

	@ResourceFieldLoader("First Name")
	private String firstName;
	@ResourceFieldLoader("Last Name")
	private String lastName;
	@ResourceFieldLoader("Email Address")
	private String email;
	@ResourceFieldLoader("Username")
	private String username;

	private String password;

	private Permission permissions;
	
	@FieldExcludedFromDatabase
	private boolean isSignedIn;

	public String getEmail() {
		return email;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getPassword() {
		return password;
	}

	public Permission getPermissions() {
		return permissions;
	}

	public String getUsername() {
		return username;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setPermissions(Permission permissions) {
		this.permissions = permissions;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public String toString() {
		return "User";
	}

	public boolean isSignedIn() {
		return isSignedIn;
	}

	public void setSignedIn(boolean isSignedIn) {
		this.isSignedIn = isSignedIn;
	}
}
