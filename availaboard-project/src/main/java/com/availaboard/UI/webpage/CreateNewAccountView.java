package com.availaboard.UI.webpage;

import com.availaboard.UI.application_structure.observable.ViewFactory;
import com.availaboard.UI.application_structure.view_structure.ViewConfiguration;
import com.availaboard.UI.frontend_functionality.VaadinComponentUtilitys;
import com.availaboard.UI.webpage.user.UserInformationView;
import com.availaboard.engine.resource.Permission;
import com.availaboard.engine.resource.Status;
import com.availaboard.engine.resource.User;
import com.availaboard.engine.security.AccessControl;
import com.availaboard.engine.security.AccessControlFactory;
import com.availaboard.engine.sql_connection.AvailaboardSQLConnection;
import com.availaboard.engine.sql_connection.NameExistsException;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

@CssImport("./styles/webpage-styles/create-new-account-view.css")
@Route(value = CreateNewAccountView.VIEWNAME, layout = MainLayout.class)
public class CreateNewAccountView extends VerticalLayout implements ViewConfiguration {

    protected static final String VIEWNAME = "create-account-view";

    /**
     *
     */
    private static final long serialVersionUID = 1778993837532264134L;

    private final AvailaboardSQLConnection db = new AvailaboardSQLConnection();
    private final TextField usernameField = new TextField("Username");
    private final PasswordField passwordField = new PasswordField("Password");
    private final PasswordField confirmPasswordField = new PasswordField("Confirm Password");
    private final TextField firstNameField = new TextField("First Name");
    private final TextField lastNameField = new TextField("Last Name");
    private final TextField emailField = new TextField("Email");
    private final H1 createNewAccountLabel = new H1("Create A New Account");
    private final FormLayout layout = new FormLayout();
    private final Notification usernameExistsNotification = VaadinComponentUtilitys.createNotification("Username already exists!", NotificationVariant.LUMO_ERROR, 1000);
    private final Notification passwordDontMatchNotification = VaadinComponentUtilitys.createNotification("Passwords do not match!", NotificationVariant.LUMO_ERROR, 1000);
    private AccessControl accessControl;

    private final User tempUser = new User();


    /**
     * A button that gets all the fields values and creates a {@link User} Object
     * with it. It then inserts the {@link User} Object into the
     * <code>database</code> and signs the  CurrentUser in as the
     * {@link User} Object created.
     */

    public CreateNewAccountView() {
        accessControl = AccessControlFactory.getInstance().createAccessControl();
        setUpLayout();
    }
    private final Button submitButton = new Button("Create an account", event -> {
        if (passwordField.getValue().equals(confirmPasswordField.getValue())) {

            // User fields
            tempUser.setUsername(usernameField.getValue());
            tempUser.setPassword(passwordField.getValue());
            tempUser.setFirstName(firstNameField.getValue());
            tempUser.setLastName(lastNameField.getValue());
            tempUser.setEmail(emailField.getValue());

            // Resource fields
            tempUser.setName(firstNameField.getValue());
            tempUser.setPermissions(Permission.User);
            tempUser.setStatus(Status.AVAILABLE);

            try {
                db.insertResourceIntoDatabase(tempUser);
                accessControl.signIn(usernameField.getValue(), passwordField.getValue());
                getUI().get().navigate(ViewFactory.createViewTypeInstance(UserInformationView.class).viewName());
                ViewFactory.getViewControllerInstance().notifiyObservers();

            } catch (NameExistsException e) {
                // Username exists in database
                usernameExistsNotification.open();
            }
        } else {
            // passwords do not match
            passwordDontMatchNotification.open();
        }
    });

    private void setUpLayout() {
        layout.add(firstNameField, lastNameField, emailField, usernameField, passwordField, confirmPasswordField);
        layout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1), new FormLayout.ResponsiveStep("500px", 2));
        layout.setColspan(usernameField, 2);
        layout.setColspan(emailField, 2);
    }

    @Override
    public void initialize() {
        add(createNewAccountLabel, layout, submitButton);
    }

    @Override
    public String viewName() {
        return CreateNewAccountView.VIEWNAME;
    }
}
