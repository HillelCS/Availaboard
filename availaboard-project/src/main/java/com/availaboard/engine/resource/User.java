package com.availaboard.engine.resource;

public class User extends Resource {

	static @ResourceFieldLoader("First Name")
	private String firstName;
	static @ResourceFieldLoader("Last Name")
	private String lastName;
	@ResourceFieldLoader("Email Address")
	private String email;
	@ResourceFieldLoader("User Name")
	private String username;

	private String password;


	
	// REMOVE LATER, only here for testing
	@ResourceFieldLoader("Permissions")
	private Permission permissions;



	@FieldExcludedFromDatabase
	private boolean isLoggedIn;

	public boolean isLoggedIn() {
		return isLoggedIn;
	}

	public void setLoggedIn(boolean isLoggedIn) {
		this.isLoggedIn = isLoggedIn;
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
