package com.availaboard.UI.webpage;

import com.availaboard.UI.webpage.user.UserInformationView;
import com.availaboard.engine.resource.Permission;
import com.availaboard.engine.resource.Status;
import com.availaboard.engine.resource.User;
import com.availaboard.engine.security.AccessControl;
import com.availaboard.engine.security.AccessControlFactory;
import com.availaboard.engine.sql_connection.AvailaboardSQLConnection;
import com.availaboard.engine.sql_connection.UsernameExistsException;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep.LabelsPosition;
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

	AvailaboardSQLConnection db = new AvailaboardSQLConnection();
	private FormLayout layout = new FormLayout();
	private H1 createNewAccountLabel = new H1("Create A New Account");
	private AccessControl accessControl;

	public CreateNewAccountView() {

		TextField usernameField = new TextField();
		PasswordField passwordField = new PasswordField();
		PasswordField confirmPasswordField = new PasswordField();
		TextField firstNameField = new TextField();
		TextField lastNameField = new TextField();
		TextField emailField = new TextField();

		accessControl = AccessControlFactory.getInstance().createAccessControl();

		Button submitButton = new Button("Create an account", event -> {
			if (passwordField.getValue().equals(confirmPasswordField.getValue())) {
				User tempUser = new User();
				tempUser.setUsername(usernameField.getValue());
				tempUser.setPassword(passwordField.getValue());
				tempUser.setFirstName(firstNameField.getValue());
				tempUser.setLastName(lastNameField.getValue());
				tempUser.setEmail(emailField.getValue());
				tempUser.setPermissions(Permission.User);
				tempUser.setStatus(Status.AVAILABLE);
				tempUser.setName(firstNameField.getValue());
				try {
					db.insertResourceIntoDatabase(tempUser);
					accessControl.signIn(usernameField.getValue(), passwordField.getValue());
					getUI().get().navigate(UserInformationView.VIEWNAME);
				} catch (UsernameExistsException e) {
					// Set username exists error message here
				}
			} else {
				// Set password and confirm password fields are not the same error here
			}
		});

		layout.addFormItem(usernameField, "Username");
		layout.addFormItem(passwordField, "Password");
		layout.addFormItem(confirmPasswordField, "Confirm Password");
		layout.addFormItem(firstNameField, "First Name");
		layout.addFormItem(lastNameField, "Last Name");
		layout.addFormItem(emailField, "Email");

		layout.setResponsiveSteps(new ResponsiveStep("0", 1));

		add(createNewAccountLabel);
		add(layout);
		add(submitButton);
	}
}
