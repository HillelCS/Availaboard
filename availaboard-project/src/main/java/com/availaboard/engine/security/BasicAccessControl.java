package com.availaboard.engine.security;

import com.availaboard.engine.sql_connection.AvailaboardSQLConnection;
import com.availaboard.engine.sql_connection.InvalidCredentialsException;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.VaadinSession;

public class BasicAccessControl implements AccessControl {

	AvailaboardSQLConnection db = new AvailaboardSQLConnection();

	@Override
	public boolean signIn(String username, String password) {
		if (username == null || username.isEmpty())
			return false;
		try {
			db.authenticate(username, password);
			CurrentUser.set(username);
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

	@Override
	public boolean isUserInRole(String role) {
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