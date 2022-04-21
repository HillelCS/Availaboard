package com.availaboard.engine.security;

import com.availaboard.engine.resource.Permission;
import com.availaboard.engine.resource.User;
import com.availaboard.engine.sql_connection.AvailaboardSQLConnection;
import com.availaboard.engine.sql_connection.InvalidCredentialsException;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.VaadinSession;

/**
 * Implementation of the {@link AccessControl} interface.
 */
public class BasicAccessControl implements AccessControl {

	/**
	 *
	 */
	private static final long serialVersionUID = 824667217356429947L;
	private static AvailaboardSQLConnection db = new AvailaboardSQLConnection();



	/**
	 * Checks if the {@link CurrentUser} is in a {@link Permission}.
	 * 
	 * @param role The {@link Permission} that the {@link CurrentUser} is being checked against.
	 */

	@Override
	public boolean isUserInRole(Permission permission) {
		if(CurrentUser.get().getPermissions() == permission) return true;
		return false;
	}

	@Override
	public boolean isUserSignedIn() {
		return !(CurrentUser.get() == null);
	}

	/**
	 * Checks if a username and password are valid. If the username and password are
	 * authenticated then the {@link CurrentUser} is set to the {@link User} with
	 * that <code>username</code>. If the database throws an
	 * {@link InvalidCredentialsException} then the method returns false.
	 *
	 * @param username the username being passed in by the {@link User} logging in.
	 * @param password the password being passed in by the {@link User} logging in.
	 * @throws InvalidCredentialsException if the username and/or password are
	 * invalid.
	 */

	@Override
	public boolean signIn(String username, String password) {
		if ((username == null) || username.isEmpty()) {
			return false;
		}
		try {
			BasicAccessControl.db.authenticate(username, password);
			CurrentUser.set(BasicAccessControl.db.createUserWithUsername(username));
			return true;
		} catch (InvalidCredentialsException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public void signOut() {
		VaadinSession.getCurrent().getSession().invalidate();
		UI.getCurrent().navigate("");
	}
}