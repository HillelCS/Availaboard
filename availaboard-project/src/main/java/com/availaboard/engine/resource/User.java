package com.availaboard.engine.resource;

import java.util.HashMap;

public class User extends Resource {

	@FieldExcludedFromDatabase
	private static HashMap<String, String> fieldList = new HashMap<String, String>();

	static {
		fieldList.put("firstName", "First Name");
		fieldList.put("lastName", "Last Name");
		fieldList.put("email", "Email");
		fieldList.put("username", "Username");
	}

	@Override
	public String getFieldName(String fieldName) {
		return fieldList.get(fieldName);
	}

	@ResourceFieldLoader
	private String firstName;
	@ResourceFieldLoader
	private String lastName;
	@ResourceFieldLoader
	private String email;
	@ResourceFieldLoader
	private String username;

	private String password;

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
