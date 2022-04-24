package com.availaboard.UI;

import java.util.stream.Stream;

import com.availaboard.engine.resource.Permission;

/**
 * Used to tell if the current user has to have some {@link Permission} to
 * access the View. Implementations of it specify which views must have what
 * permissions.
 */
public interface ViewAuthorization {
	Stream<Permission> getRequiredPermission();

	String getViewName();
}
