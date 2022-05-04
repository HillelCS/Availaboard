package com.availaboard.UI.webpage.user;

import com.availaboard.UI.view_pattern.ViewAuthorization;
import com.availaboard.UI.view_pattern.ViewConfiguration;
import com.availaboard.UI.webpage.MainLayout;
import com.availaboard.engine.resource.Permission;
import com.availaboard.engine.resource.Status;
import com.availaboard.engine.security.AccessControl;
import com.availaboard.engine.security.AccessControlFactory;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import java.util.stream.Stream;

@Route(value = UserInformationView.VIEWNAME, layout = MainLayout.class)
public class UserInformationView extends VerticalLayout implements ViewAuthorization, ViewConfiguration {

    protected static final String VIEWNAME = "user-information";

    /**
     *
     */
    private static final long serialVersionUID = -8469495034991926228L;
    private final Label usernameLabel = new Label();
    private final Icon statusIcon;
    private final AccessControl accessControl;

    public UserInformationView() {
        accessControl = AccessControlFactory.getInstance().createAccessControl();

        // Displays a Check Icon if the CurrentUser is Available and an X Icon if they are not.
        statusIcon = accessControl.getCurrentUser().getStatus() == Status.AVAILABLE ? new Icon(VaadinIcon.CHECK_CIRCLE_O) : new Icon(VaadinIcon.CLOSE_CIRCLE_O);
        setHorizontalComponentAlignment(Alignment.START, usernameLabel, statusIcon);
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
        add(usernameLabel, statusIcon);
    }

    @Override
    public String viewName() {
        return VIEWNAME;
    }
}
