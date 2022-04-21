package com.availaboard.UI;

import com.availaboard.engine.resource.Permission;

/**
 * Used to tell if the current user has to have some {@link Permission}
 * to access the View. Implementations of it specify which views must have what
 * permissions.
 */
public interface ViewAuthorization {
	
	/**
	 * @return The {@link Permission} required to access the implementors view.
	 */
	  Permission getRequiredPermission();
}
