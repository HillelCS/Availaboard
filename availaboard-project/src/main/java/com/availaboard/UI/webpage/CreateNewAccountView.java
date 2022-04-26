package com.availaboard.UI.webpage;

import com.availaboard.engine.resource.User;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

@Route(value = "create-account", layout = MainLayout.class)
public class CreateNewAccountView extends VerticalLayout {

	/**
	 *
	 */
	private static final long serialVersionUID = 1778993837532264134L;

	private FormLayout layout = new FormLayout();
	private H1 createNewAccountLabel = new H1("Create A New Account");

	public CreateNewAccountView() {

		TextField usernameField = new TextField();
		PasswordField passwordField = new PasswordField();
		PasswordField confirmPasswordField = new PasswordField();
		TextField firstNameField = new TextField();
		TextField lastNameField = new TextField();
		TextField emailField = new TextField();

		Button submitButton = new Button("Create an account", event -> {
			if (passwordField.getValue().equals(confirmPasswordField.getValue())) {
				User tempUser = new User();
				tempUser.setUsername(usernameField.getValue());
				tempUser.setPassword(passwordField.getValue());
				tempUser.setFirstName(firstNameField.getValue());
				tempUser.setLastName(lastNameField.getValue());
				tempUser.setEmail(emailField.getValue());
			} else {
				// Set error message
			}
		});

		add(createNewAccountLabel);
		add(layout);
		add(submitButton);
	}
}
