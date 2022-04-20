package com.availaboard.engine.security;

import com.availaboard.engine.resource.User;
import com.vaadin.flow.server.VaadinRequest;
import com.vaadin.flow.server.VaadinService;

/**
 * Class for retrieving and setting the current {@link User} of the current
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
	 * Returns the current {@link User} stored in the current session, null if no {@link User} is stored.
	 *
	 * @throws IllegalStateException if the current session cannot be accessed.
	 */
	public static User get() {
		User currentUser = (User) CurrentUser.getCurrentRequest().getWrappedSession()
				.getAttribute(CurrentUser.CURRENT_USER_SESSION_ATTRIBUTE_KEY);
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
	 * Set's the {@link CurrentUser} session as a {@link User} being passed in. If
	 * the {@link User} passed in is {@code null} it removed the {@link User} from the current
	 * session.
	 *
	 * @throws IllegalStateException if the current session cannot be accessed.
	 */
	public static void set(User currentUser) {
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