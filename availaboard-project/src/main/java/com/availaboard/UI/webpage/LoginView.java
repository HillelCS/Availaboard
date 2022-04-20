package com.availaboard.UI.webpage;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.availaboard.engine.security.AccessControl;
import com.availaboard.engine.security.AccessControlFactory;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.router.RouteConfiguration;

/**
 * UI content when the user is not logged in yet.
 */
@Route("login")
@PageTitle("Login")
public class LoginView extends FlexLayout {

	private AccessControl accessControl;

	public LoginView() {
		accessControl = AccessControlFactory.getInstance().createAccessControl();
		buildUI();
	}

	
	private void buildUI() {
		setSizeFull();
		LoginForm loginForm = new LoginForm();
		loginForm.addLoginListener(this::login);

		FlexLayout centeringLayout = new FlexLayout();
		centeringLayout.setSizeFull();
		centeringLayout.setJustifyContentMode(JustifyContentMode.CENTER);
		centeringLayout.setAlignItems(Alignment.CENTER);
		centeringLayout.add(loginForm);
		add(centeringLayout);
	}


	private void login(LoginForm.LoginEvent event) {
		if (accessControl.signIn(event.getUsername(), event.getPassword())) {
			getUI().get().navigate("/admin");
		} else {
			event.getSource().setError(true);
		}
	}
}