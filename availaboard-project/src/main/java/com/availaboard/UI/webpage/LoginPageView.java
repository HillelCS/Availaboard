package com.availaboard.UI.webpage;

import com.availaboard.engine.sql_connection.AvailaboardSQLConnection;
import com.availaboard.engine.sql_connection.InvalidCredentialsException;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
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
		TextField username = new TextField("Username");
		PasswordField password = new PasswordField("Password");
		add(new H1("Welcome"), username, password, new Button("Login", event -> {
			try {
				db.authenticate(username.getValue(), password.getValue());
			} catch (InvalidCredentialsException e) {
				e.printStackTrace();
			}
		}));
	}
}
