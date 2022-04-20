package com.availaboard.UI.webpage.admin;

import com.availaboard.UI.webpage.MainLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route(value = "/admin", layout = MainLayout.class)
public class AdminView extends VerticalLayout implements AbstractAdminView {

	/**
	 *
	 */
	private static final long serialVersionUID = -118322660015469075L;
	public static final String VIEW_NAME = "admin";

	public AdminView() {

	}
}
