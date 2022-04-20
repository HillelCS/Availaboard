package com.availaboard.engine.security;

import java.io.Serializable;

import com.availaboard.engine.resource.Permission;

/**
 * Regulates {@link User} sessions.
 */

public interface AccessControl extends Serializable {

	String getPrincipalName();

	boolean isUserInRole(Permission permission);

	boolean isUserSignedIn();

	boolean signIn(String username, String password);

	void signOut();
}