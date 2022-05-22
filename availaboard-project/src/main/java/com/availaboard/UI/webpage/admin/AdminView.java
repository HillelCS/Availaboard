package com.availaboard.UI.webpage.admin;

import com.availaboard.UI.application_structure.observable.Observer;
import com.availaboard.UI.application_structure.observable.Subject;
import com.availaboard.UI.application_structure.observable.ViewFactory;
import com.availaboard.UI.application_structure.view_structure.ViewAuthorization;
import com.availaboard.UI.application_structure.view_structure.ViewObserver;
import com.availaboard.UI.webpage.MainLayout;
import com.availaboard.engine.resource.Permission;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import java.util.Optional;
import java.util.stream.Stream;

@Route(value = AdminView.VIEWNAME, layout = MainLayout.class)
public class AdminView extends VerticalLayout implements ViewAuthorization, ViewObserver {

    protected static final String VIEWNAME = "Admin";
    /**
     *
     */
    private static final long serialVersionUID = -118322660015469075L;

    public AdminView() {

    }

    @Override
    public Optional<UI> getUI() {
        return super.getUI();
    }

    /**
     * Requires an Admin {@link Permission} to access the view.
     */
    @Override
    public Stream<Permission> getRequiredPermission() {
        return Stream.of(Permission.Admin);
    }


    @Override
    public void initialize() {

    }

    @Override
    public String viewName() {
        return AdminView.VIEWNAME;
    }


    @Override
    public void update() {

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
        return ViewFactory.getViewControllerInstance();
    }
}
