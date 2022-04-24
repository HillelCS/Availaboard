package com.availaboard.UI.webpage.admin;

import java.util.stream.Stream;

import com.availaboard.UI.ViewAuthorization;
import com.availaboard.UI.webpage.MainLayout;
import com.availaboard.engine.resource.Permission;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route(value = AdminView.VIEWNAME, layout = MainLayout.class)
public class AdminView extends VerticalLayout implements ViewAuthorization {

	/**
	 *
	 */
	private static final long serialVersionUID = -118322660015469075L;
	public static final String VIEWNAME = "admin";

	public AdminView() {

	}

	/**
	 * Requires an Admin {@link Permission} to access the view.
	 */
	@Override
	public Stream<Permission> getRequiredPermission() {
		return Stream.of(Permission.Admin);
	}

	@Override
	public String viewName() {
		return AdminView.VIEWNAME;
	}
}
