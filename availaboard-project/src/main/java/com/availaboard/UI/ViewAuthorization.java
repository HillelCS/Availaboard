package com.availaboard.UI;

import java.util.stream.Stream;

import org.springframework.stereotype.Component;

import com.availaboard.engine.resource.Permission;

/**
 * Used to tell if the current user has to have some {@link Permission} to
 * access the View. Classes that implement it override the {@link ViewAuthorization #getRequiredPermission()}
 * and adds all the permissions needed to access that class to a {@link Stream}. 
 */
public interface ViewAuthorization {

	/**
	 * @return A {@link Stream} of {@link Permission}'s that the {@link CurrentUser}
	 *         needs to successfully access the View it's navigating to.
	 */
	Stream<Permission> getRequiredPermission();
	String getViewName();
}
