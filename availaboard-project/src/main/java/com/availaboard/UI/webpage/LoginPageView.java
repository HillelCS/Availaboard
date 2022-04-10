package com.availaboard.UI.webpage;

import com.availaboard.engine.sql_connection.AvailaboardSQLConnection;
import com.availaboard.engine.sql_connection.InvalidCredentialsException;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;

@PageTitle("Login")
@Route("/login")
@Theme(value = Lumo.class, variant = Lumo.DARK)
@CssImport("./styles/webpage-styles/login-view-styles.css")
public class LoginPageView extends VerticalLayout {
	AvailaboardSQLConnection db = new AvailaboardSQLConnection();

	public LoginPageView() {
		TextField username = new TextField("Username");
		PasswordField password = new PasswordField("Password");
		H1 welcomeText = new H1("Welcome");
		Button loginButton = new Button("Login", event -> {
			try {
				db.authenticate(username.getValue(), password.getValue());
				UI.getCurrent().navigate("/");
			} catch (InvalidCredentialsException e) {
				Notification notification = Notification.show("Invalid username or password");
				notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
			}
		});

		username.setWidth("40%");
		password.setWidth("40%");
		username.addClassName("usernameField");
		password.addClassName("passwordField");
		loginButton.setWidth("30%");
		loginButton.addClassName("loginButton");

		add(welcomeText, username, password, loginButton);
		setAlignItems(Alignment.CENTER);
	}
}
