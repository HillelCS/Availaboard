package com.availaboard.UI.webpage.user;

import com.availaboard.UI.application_structure.view_structure.ViewAuthorization;
import com.availaboard.UI.frontend_functionality.ResourceGrid;
import com.availaboard.UI.application_structure.observable.Observer;
import com.availaboard.UI.application_structure.observable.Subject;
import com.availaboard.UI.application_structure.observable.ViewFactory;
import com.availaboard.UI.webpage.MainLayout;
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
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

import java.util.stream.Stream;

@CssImport("./styles/webpage-styles/user-information-view.css")
@Route(value = UserInformationView.VIEWNAME, layout = MainLayout.class)
public class UserInformationView extends VerticalLayout implements ViewAuthorization, Observer {

    protected static final String VIEWNAME = "user-information";

    private User user;

    /**
     *
     */
    private static final long serialVersionUID = -8469495034991926228L;
    private Label usernameLabel;
    private Label statusLabel;

    private final TextField usernameField = new TextField("Username");
    private final PasswordField passwordField = new PasswordField("Password");
    private final TextField firstNameField = new TextField("First Name");
    private final TextField lastNameField = new TextField("Last Name");
    private final TextField emailField = new TextField("Email");

    Select<Status> select = new Select<>();

    private final Notification successNotification = createNotification("Successfully updated User", NotificationVariant.LUMO_SUCCESS);
    private final Notification usernameExistsNotification = createNotification("Another User already has this Username", NotificationVariant.LUMO_ERROR);

    private final AvailaboardSQLConnection db = new AvailaboardSQLConnection();

    private Button applyButton;
    private final FormLayout layout = new FormLayout();

    private final VerticalLayout userStatusContainer = new VerticalLayout();
    private AccessControl accessControl;

    private void setUpUserProfile() {
        statusLabel = ResourceGrid.statusLabel(user);
        usernameLabel = new Label(user.getUsername());
        usernameLabel.addClassName("username-label");
        statusLabel.addClassName("status-label");
        userStatusContainer.addClassName("username-status-container");
        userStatusContainer.setAlignItems(FlexComponent.Alignment.CENTER);
        userStatusContainer.add(usernameLabel, statusLabel);
        setHorizontalComponentAlignment(FlexComponent.Alignment.CENTER, userStatusContainer);
    }

    private void setUpUserFields() {
        applyButton = new Button("Apply Changes", event -> {
            user.setUsername(usernameField.getValue());
            user.setPassword(passwordField.getValue());
            user.setFirstName(firstNameField.getValue());
            user.setLastName(lastNameField.getValue());
            user.setEmail(emailField.getValue());
            user.setStatus(select.getValue());

            try {

                db.updateResourceInDatabase(user);
                successNotification.open();
                subject.notifiyObservers();

            } catch (NameExistsException e) {
                usernameExistsNotification.open();
            }
        });

        select.setLabel("Status");
        select.setItems(Status.AVAILABLE, Status.BUSY);

        select.setValue(user.getStatus());
        usernameField.setValue(user.getUsername());
        passwordField.setValue(user.getPassword());
        firstNameField.setValue(user.getFirstName());
        lastNameField.setValue(user.getLastName());
        emailField.setValue(user.getEmail());
        layout.add(firstNameField, lastNameField, emailField, usernameField, passwordField, select);
        layout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1), new FormLayout.ResponsiveStep("500px", 2));

        layout.addClassName("user-field-container");
    }


    /**
     * Requires a User {@link Permission} or an {@link Permission} to access the
     * view.
     */
    @Override
    public Stream<Permission> getRequiredPermission() {
        return Stream.of(Permission.User, Permission.Admin);
    }

    @Override
    public void initialize() {
        accessControl = AccessControlFactory.getInstance().createAccessControl();
        user = accessControl.getCurrentUser();

        setUpUserProfile();
        setUpUserFields();

        add(userStatusContainer, layout, applyButton);

    }
    @Override
    public String viewName() {
        return VIEWNAME;
    }

    private Notification createNotification(final String text, NotificationVariant variant) {
        final Notification notification = new Notification();
        notification.addThemeVariants(variant);
        final Div notificationText = new Div(new Text(text));
        final Button closeButton = new Button(new Icon("lumo", "cross"));
        closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        closeButton.getElement().setAttribute("aria-label", "Close");
        closeButton.addClickListener(event -> notification.close());
        final HorizontalLayout layout = new HorizontalLayout(notificationText, closeButton);
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        notification.add(layout);
        return notification;
    }

    @Override
    public void update() {
        usernameLabel.setText(user.getUsername());
        statusLabel.removeAll();
        statusLabel.add(ResourceGrid.statusLabel(user));
    }

    @Override
    public void register(Subject subject) {
        subject.addObserver(this);
    }

    @Override
    public void unregister(Subject subject) {
        subject.removeObserver(this);
    }

    @Override
    public Subject getSubject() {
        return ViewFactory.createViewControllerInstance();
    }
}
