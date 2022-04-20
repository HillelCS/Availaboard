package com.availaboard.UI.webpage;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route(value = "/admin", layout = MainLayout.class)
public class AdminView extends VerticalLayout {

	public static final String VIEW_NAME = "admin";

	public AdminView() {
		
	}
}
