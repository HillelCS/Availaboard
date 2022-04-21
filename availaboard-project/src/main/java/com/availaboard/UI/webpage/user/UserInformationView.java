package com.availaboard.UI.webpage.user;

import com.availaboard.UI.ViewAuthorization;
import com.availaboard.engine.resource.Permission;

public class UserInformationView implements ViewAuthorization {

	/**
	 * Requires a User {@link Permission}
	 */
	@Override
	public Permission getRequiredPermission() {
		return Permission.User;
	}

}
