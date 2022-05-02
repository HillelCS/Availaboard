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
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep.LabelsPosition;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

@CssImport("./styles/webpage-styles/create-new-account-view.css")
@Route(value = "create-account", layout = MainLayout.class)
public class CreateNewAccountView extends VerticalLayout {

	/**
	 *
	 */
	private static final long serialVersionUID = 1778993837532264134L;

	private static final AvailaboardSQLConnection db = new AvailaboardSQLConnection();
	private static final FormLayout layout = new FormLayout();
	private static final H1 createNewAccountLabel = new H1("Create A New Account");
	private static final Label usernameExistsErrorLabel = new Label("This username already exists");
	private static final TextField usernameField = new TextField("Username");
	private static final PasswordField passwordField = new PasswordField("Password");
	private static final PasswordField confirmPasswordField = new PasswordField("Confirm Password");
	private static final TextField firstNameField = new TextField("First Name");
	private static final TextField lastNameField = new TextField("Last Name");
	private static final TextField emailField = new TextField("Email");
	/**
	 * A button that gets all the fields values and creates a {@link User} Object
	 * with it. It then inserts the {@link User} Object into the
	 * <code>database</code> and signs the {@link CurrentUser} in as the
	 * {@link User} Object created.
	 */
	private static Button submitButton;
	private AccessControl accessControl;

	public CreateNewAccountView() {

		usernameExistsErrorLabel.setVisible(false);

		submitButton = new Button("Create an account", event -> {
			if (passwordField.getValue().equals(confirmPasswordField.getValue())) {
				User tempUser = new User();
				tempUser.setUsername(usernameField.getValue());
				tempUser.setPassword(passwordField.getValue());
				tempUser.setFirstName(firstNameField.getValue());
				tempUser.setLastName(lastNameField.getValue());
				tempUser.setEmail(emailField.getValue());
				tempUser.setPermissions(Permission.User);
				tempUser.setStatus(Status.AVAILABLE);
				try {
					db.insertResourceIntoDatabase(tempUser);
					accessControl.signIn(usernameField.getValue(), passwordField.getValue());
					getUI().get().navigate(UserInformationView.VIEWNAME);
				} catch (UsernameExistsException e) {
					usernameExistsErrorLabel.setVisible(true);
				}
			} else {
				// Set password and confirm password fields are not the same error here
			}
		});

		usernameExistsErrorLabel.addClassName("invalidUsernameLabel");

		accessControl = AccessControlFactory.getInstance().createAccessControl();

		layout.add(firstNameField, lastNameField, emailField, usernameField, passwordField, confirmPasswordField);
		layout.setResponsiveSteps(new ResponsiveStep("0", 1), new ResponsiveStep("500px", 2));
		layout.setColspan(usernameField, 2);
		layout.setColspan(emailField, 2);
		add(createNewAccountLabel);
		add(usernameExistsErrorLabel);
		add(layout);
		add(submitButton);
	}
}
