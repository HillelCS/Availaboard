package com.availaboard.UI.webpage;

import javax.annotation.security.PermitAll;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;

import com.availaboard.engine.security.AccessControl;
import com.availaboard.engine.security.AccessControlFactory;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterListener;
import com.vaadin.flow.router.Route;

@Route("/admin")
public class AdminView extends VerticalLayout {
	private AccessControl accessControl;

	public static final String VIEW_NAME = "admin";

	public AdminView() {
		
	}
}
