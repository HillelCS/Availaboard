package com.availaboard.UI.webpage.user;

import java.util.stream.Stream;

import com.availaboard.UI.ViewAuthorization;
import com.availaboard.UI.webpage.MainLayout;
import com.availaboard.engine.resource.Permission;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route(value = UserInformationView.VIEWNAME, layout = MainLayout.class)
public class UserInformationView extends VerticalLayout implements ViewAuthorization {

    public static final String VIEWNAME = "user-information";
    /**
     *
     */
    private static final long serialVersionUID = -8469495034991926228L;

    /**
     * Requires a User {@link Permission} or an {@link Permission} to access the
     * view.
     */
    @Override
    public Stream<Permission> getRequiredPermission() {
        return Stream.of(Permission.User, Permission.Admin);
    }

    @Override
    public String getViewName() {
        return UserInformationView.VIEWNAME;
    }

}
