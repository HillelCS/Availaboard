package com.availaboard.engine.security;

import com.vaadin.flow.server.VaadinRequest;
import com.vaadin.flow.server.VaadinService;

/**
 * Class for retrieving and setting the name of the current user of the current
 * session (without using JAAS). All methods of this class require that a
 * {@link VaadinRequest} is bound to the current thread.
 *
 *
 * @see VaadinService#getCurrentRequest()
 */
public final class CurrentUser {

	/**
	 * The attribute key used to store the username in the session.
	 */
	public static final String CURRENT_USER_SESSION_ATTRIBUTE_KEY = CurrentUser.class.getCanonicalName();

	/**
	 * Returns the name of the current user stored in the current session, or an
	 * empty string if no user name is stored.
	 *
	 * @throws IllegalStateException if the current session cannot be accessed.
	 */
	public static String get() {
		String currentUser = (String) CurrentUser.getCurrentRequest().getWrappedSession()
				.getAttribute(CurrentUser.CURRENT_USER_SESSION_ATTRIBUTE_KEY);
		if (currentUser == null) {
			return "";
		}
		return currentUser;
	}

	private static VaadinRequest getCurrentRequest() {
		VaadinRequest request = VaadinService.getCurrentRequest();
		if (request == null) {
			throw new IllegalStateException("No request bound to current thread.");
		}
		return request;
	}

	/**
	 * Sets the name of the current user and stores it in the current session. Using
	 * a {@code null} username will remove the username from the session.
	 *
	 * @throws IllegalStateException if the current session cannot be accessed.
	 */
	public static void set(String currentUser) {
		if (currentUser == null) {
			CurrentUser.getCurrentRequest().getWrappedSession()
					.removeAttribute(CurrentUser.CURRENT_USER_SESSION_ATTRIBUTE_KEY);
		} else {
			CurrentUser.getCurrentRequest().getWrappedSession()
					.setAttribute(CurrentUser.CURRENT_USER_SESSION_ATTRIBUTE_KEY, currentUser);
		}
	}

	private CurrentUser() {
	}
}