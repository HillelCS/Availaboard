package com.availaboard.UI.webpage.user;

import com.availaboard.UI.frontend_functionality.ResourceGrid;
import com.availaboard.UI.view_pattern.ViewAuthorization;
import com.availaboard.UI.view_pattern.ViewConfiguration;
import com.availaboard.UI.webpage.MainLayout;
import com.availaboard.engine.resource.Permission;
import com.availaboard.engine.security.AccessControl;
import com.availaboard.engine.security.AccessControlFactory;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import java.util.stream.Stream;

@CssImport("./styles/webpage-styles/user-information-view.css")
@Route(value = UserInformationView.VIEWNAME, layout = MainLayout.class)
public class UserInformationView extends VerticalLayout implements ViewAuthorization, ViewConfiguration {

    protected static final String VIEWNAME = "user-information";

    /**
     *
     */
    private static final long serialVersionUID = -8469495034991926228L;
    private Label usernameLabel;
    private Label statusLabel;

    private final VerticalLayout userStatusContainer = new VerticalLayout();
    private final AccessControl accessControl;

    public UserInformationView() {
        accessControl = AccessControlFactory.getInstance().createAccessControl();
        setUpUserProfile();
    }

    private void setUpUserProfile() {
        statusLabel = ResourceGrid.statusLabel(accessControl.getCurrentUser());
        usernameLabel = new Label(accessControl.getCurrentUser().getUsername());

        usernameLabel.addClassName("username-label");
        statusLabel.addClassName("status-label");
        userStatusContainer.addClassName("username-status-container");
        userStatusContainer.setAlignItems(Alignment.CENTER);
        userStatusContainer.add(usernameLabel, statusLabel);
        setHorizontalComponentAlignment(Alignment.START, userStatusContainer);
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
    public void addAll() {
        add(userStatusContainer);
    }

    @Override
    public String viewName() {
        return VIEWNAME;
    }

}
