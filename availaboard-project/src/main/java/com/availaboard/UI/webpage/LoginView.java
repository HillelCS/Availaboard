package com.availaboard.UI.webpage;

import com.availaboard.engine.resource.Permission;
import com.availaboard.engine.security.AccessControl;
import com.availaboard.engine.security.AccessControlFactory;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

/**
 * UI content when the user is not logged in yet.
 */
@Route(value = "login", layout = MainLayout.class)
@PageTitle("Login")
public class LoginView extends FlexLayout {

	/**
	 *
	 */
	private static final long serialVersionUID = -6633815459114206330L;
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
			if (accessControl.isUserInRole(Permission.Admin)) {
				getUI().get().navigate("/admin");
			} else {
				getUI().get().navigate("/");
			}
			UI.getCurrent().getPage().reload();
		} else {
			event.getSource().setError(true);
		}
	}
}