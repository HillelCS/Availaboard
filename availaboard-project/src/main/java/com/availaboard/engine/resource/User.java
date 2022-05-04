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

    public void setEmail(final String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public Permission getPermissions() {
        return permissions;
    }

    public void setPermissions(final Permission permissions) {
        this.permissions = permissions;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
        setName(username);
    }
}
