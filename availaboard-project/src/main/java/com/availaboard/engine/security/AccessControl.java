package com.availaboard.engine.security;

import java.io.Serializable;

/**
 * Regulates {@link User} sessions.
 */

public interface AccessControl extends Serializable {

	String ADMIN_ROLE_NAME = "admin";

	String getPrincipalName();

	boolean isUserAdmin(String role);

	boolean isUserSignedIn();

	boolean signIn(String username, String password);

	void signOut();
}