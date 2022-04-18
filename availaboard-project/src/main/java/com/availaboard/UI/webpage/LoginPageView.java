package com.availaboard.UI.webpage;

import com.availaboard.engine.sql_connection.AvailaboardSQLConnection;
import com.availaboard.engine.sql_connection.InvalidCredentialsException;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Login")
@Route("/login")
public class LoginPageView extends VerticalLayout {
	AvailaboardSQLConnection db = new AvailaboardSQLConnection();

	public LoginPageView() {
		setAlignItems(Alignment.CENTER);
		TextField username = new TextField("Username");
		PasswordField password = new PasswordField("Password");

		username.setWidth("30%");
		password.setWidth("30%");

		username.setMaxWidth("400px");
		password.setMaxWidth("400px");

		Button loginButton = new Button("Login");

		loginButton.addClickListener(event -> {
			try {
				db.authenticate(username.getValue(), password.getValue());
				loginButton.getUI().ifPresent(ui -> ui.navigate(AvailaboardView.class));
			} catch (InvalidCredentialsException e) {
				Notification notification = new Notification("Invalid username or password");
				notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
				notification.open();
			}
		});

		loginButton.setWidth("20%");
		loginButton.setMaxWidth("300px");

		add(username, password, loginButton);
	}
}
