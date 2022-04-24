package com.availaboard.engine.security;

import java.io.Serializable;
import java.util.stream.Stream;

import com.availaboard.engine.resource.Permission;

/**
 * Regulates {@link User} sessions.
 */

public interface AccessControl extends Serializable {

	boolean isUserInRole(Permission permission);

	boolean isUserInRole(Stream<Permission> stream);

	boolean isUserSignedIn();

	boolean signIn(String username, String password);

	void signOut();
}