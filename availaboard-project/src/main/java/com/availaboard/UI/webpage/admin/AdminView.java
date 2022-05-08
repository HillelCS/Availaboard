package com.availaboard.UI.webpage.admin;

import com.availaboard.UI.view_structure.ViewAuthorization;
import com.availaboard.UI.view_structure.ViewConfiguration;
import com.availaboard.UI.webpage.MainLayout;
import com.availaboard.engine.resource.Permission;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import java.util.stream.Stream;

@Route(value = AdminView.VIEWNAME, layout = MainLayout.class)
public class AdminView extends VerticalLayout implements ViewAuthorization, ViewConfiguration {

    protected static final String VIEWNAME = "admin";
    /**
     *
     */
    private static final long serialVersionUID = -118322660015469075L;

    public AdminView() {

    }

    /**
     * Requires an Admin {@link Permission} to access the view.
     */
    @Override
    public Stream<Permission> getRequiredPermission() {
        return Stream.of(Permission.Admin);
    }


    @Override
    public void addAll() {

    }

    @Override
    public String viewName() {
        return AdminView.VIEWNAME;
    }


}
