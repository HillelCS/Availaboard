package com.availaboard.engine.resource;

import com.availaboard.engine.sql_connection.Column;
import com.availaboard.engine.sql_connection.Table;

@Table("USER")
public class User extends Resource {

	@Column("FIRSTNAME")
	@ResourceFieldLoader("First Name")
	private String firstName;
	@Column("LASTNAME")
	@ResourceFieldLoader("Last Name")
	private String lastName;
	@Column("EMAIL")
	@ResourceFieldLoader("Email Address")
	private String email;
	@Column("USERNAME")
	@ResourceFieldLoader("Username")
	private String username;
	@Column("PASSWORD")
	private String password;
	@Column("PERMISSIONS")
	private Permission permissions;

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
}
