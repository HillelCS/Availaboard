package com.availaboard.engine.security;

import java.io.Serializable;

/**
 * Regulates {@link User} sessions. 
 */

public interface AccessControl extends Serializable {

    String ADMIN_ROLE_NAME = "admin";

    boolean signIn(String username, String password);

    boolean isUserSignedIn();

    boolean isUserAdmin(String role);

    String getPrincipalName();

    void signOut();
}