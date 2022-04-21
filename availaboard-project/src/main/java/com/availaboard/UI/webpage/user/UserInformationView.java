package com.availaboard.UI.webpage.user;

import java.util.stream.Stream;

import com.availaboard.UI.ViewAuthorization;
import com.availaboard.engine.resource.Permission;

public class UserInformationView implements ViewAuthorization {

	/**
	 * Requires a User {@link Permission} or an {@link Permission} to access the view.
	 */
	@Override
	public Stream<Permission> getRequiredPermission() {
		return Stream.of(Permission.User, Permission.Admin);
	}

}
