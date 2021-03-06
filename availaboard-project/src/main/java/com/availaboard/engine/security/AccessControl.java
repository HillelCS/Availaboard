package com.availaboard.engine.security;

import com.availaboard.engine.resource.Permission;
import com.availaboard.engine.resource.User;

import java.io.Serializable;
import java.util.stream.Stream;

/**
 * Regulates {@link User} sessions.
 */

public interface AccessControl extends Serializable {

    User getCurrentUser();
    boolean isUserInRole(Permission permission);

    boolean isUserInRole(Stream<Permission> stream);

    boolean isUserSignedIn();

    boolean signIn(String username, String password);

    void signOut();
}