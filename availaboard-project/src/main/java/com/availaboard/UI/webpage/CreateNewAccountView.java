package com.availaboard.UI.webpage;

import com.availaboard.UI.designpattern.ViewConfiguration;
import com.availaboard.UI.webpage.user.UserInformationView;
import com.availaboard.engine.resource.Permission;
import com.availaboard.engine.resource.Status;
import com.availaboard.engine.resource.User;
import com.availaboard.engine.security.AccessControl;
import com.availaboard.engine.security.AccessControlFactory;
import com.availaboard.engine.sql_connection.AvailaboardSQLConnection;
import com.availaboard.engine.sql_connection.UsernameExistsException;
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
@Route(value = "create-account", layout = MainLayout.class)
public class CreateNewAccountView extends VerticalLayout implements ViewConfiguration {

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
    private final Notification usernameExistsNotification = this.createErrorNotification("Username already exists!");
    private final Notification passwordDontMatchNotification = this.createErrorNotification("Passwords do not match!");
    private AccessControl accessControl;
    /**
     * A button that gets all the fields values and creates a {@link User} Object
     * with it. It then inserts the {@link User} Object into the
     * <code>database</code> and signs the  CurrentUser in as the
     * {@link User} Object created.
     */
    private final Button submitButton = new Button("Create an account", event -> {
        if (this.passwordField.getValue().equals(this.confirmPasswordField.getValue())) {
            User tempUser = new User();
            tempUser.setUsername(this.usernameField.getValue());
            tempUser.setPassword(this.passwordField.getValue());
            tempUser.setFirstName(this.firstNameField.getValue());
            tempUser.setLastName(this.lastNameField.getValue());
            tempUser.setEmail(this.emailField.getValue());
            tempUser.setPermissions(Permission.User);
            tempUser.setStatus(Status.AVAILABLE);
            try {
                this.db.insertResourceIntoDatabase(tempUser);
                this.accessControl.signIn(this.usernameField.getValue(), this.passwordField.getValue());
                this.getUI().get().navigate(UserInformationView.VIEWNAME);
            } catch (UsernameExistsException e) {
                // Username exists in database
                this.usernameExistsNotification.open();
            }
        } else {
            // passwords do not match
            this.passwordDontMatchNotification.open();
        }
    });

    public CreateNewAccountView() {
        this.accessControl = AccessControlFactory.getInstance().createAccessControl();
        layout.add(this.firstNameField, this.lastNameField, this.emailField, this.usernameField, this.passwordField, this.confirmPasswordField);
        layout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1), new FormLayout.ResponsiveStep("500px", 2));
        layout.setColspan(this.usernameField, 2);
        layout.setColspan(this.emailField, 2);
    }

    private Notification createErrorNotification(final String text) {
        final Notification notification = new Notification();
        notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
        Div notificationText = new Div(new Text(text));
        final Button closeButton = new Button(new Icon("lumo", "cross"));
        closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        closeButton.getElement().setAttribute("aria-label", "Close");
        closeButton.addClickListener(event -> notification.close());

        final HorizontalLayout layout = new HorizontalLayout(notificationText, closeButton);
        layout.setAlignItems(Alignment.CENTER);

        notification.add(layout);

        return notification;
    }

    @Override
    public void addAll() {
        this.add(createNewAccountLabel);
        this.add(layout);
        this.add(submitButton);
    }

}
