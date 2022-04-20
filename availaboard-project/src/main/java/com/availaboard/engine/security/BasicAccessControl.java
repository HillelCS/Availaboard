package com.availaboard.engine.security;

import com.availaboard.engine.resource.Resource;
import com.availaboard.engine.resource.User;
import com.availaboard.engine.sql_connection.AvailaboardSQLConnection;
import com.availaboard.engine.sql_connection.InvalidCredentialsException;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.VaadinSession;

/**
 * Implementation of the {@link AccessControl} interface.
 */
public class BasicAccessControl implements AccessControl {

	private static AvailaboardSQLConnection db = new AvailaboardSQLConnection();

	/**
	 * Checks if a username and password are valid. If the username and password are
	 * authenticated then the {@link CurrentUser} is set to the {@link Permission} 
	 * of that {@link User} and the method returns true. If the database throws an
	 * {@link InvalidCredentialsException} then the method returns false.
	 * 
	 * @param username the username being passed in by the {@link User} logging in.
	 * @param password the password being passed in by the {@link User} logging in.
	 * @throws InvalidCredentialsException if the username and/or password are invalid.
	 */
	
	@Override
	public boolean signIn(String username, String password) {
		if (username == null || username.isEmpty())
			return false;
		try {
			db.authenticate(username, password);
			CurrentUser.set(db.createUserWithUsername(username).getPermissions().toString());
			return true;
		} catch (InvalidCredentialsException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean isUserSignedIn() {
		return !CurrentUser.get().isEmpty();
	}

	/**
	 * Checks if a given user is an administrator.
	 * 
	 * @param role  The permission of the user being passed in.
	 */
	
	@Override
	public boolean isUserAdmin(String role) {
		if ("admin".equals(role)) {
			return getPrincipalName().equals("admin");
		}
		return true;
	}

	@Override
	public String getPrincipalName() {
		return CurrentUser.get();
	}

	@Override
	public void signOut() {
		VaadinSession.getCurrent().getSession().invalidate();
		UI.getCurrent().navigate("");
	}
}