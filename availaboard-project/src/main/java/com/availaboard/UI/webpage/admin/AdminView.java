package com.availaboard.UI.webpage.admin;

import com.availaboard.UI.application_structure.observable.Observer;
import com.availaboard.UI.application_structure.observable.Subject;
import com.availaboard.UI.application_structure.observable.ViewFactory;
import com.availaboard.UI.application_structure.view_structure.ViewAuthorization;
import com.availaboard.UI.application_structure.view_structure.ViewObserver;
import com.availaboard.UI.frontend_functionality.AdminResourceGrid;
import com.availaboard.UI.frontend_functionality.ResourceGrid;
import com.availaboard.UI.webpage.MainLayout;
import com.availaboard.engine.resource.Permission;
import com.availaboard.engine.resource.Resource;
import com.availaboard.engine.resource.User;
import com.availaboard.engine.sql_connection.AvailaboardSQLConnection;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import java.util.Optional;
import java.util.stream.Stream;

import static com.availaboard.engine.resource.Permission.Admin;

@Route(value = AdminView.VIEWNAME, layout = MainLayout.class)
@CssImport("./styles/webpage-styles/admin-view.css")
public class AdminView extends VerticalLayout implements ViewAuthorization, ViewObserver {

    protected static final String VIEWNAME = "Admin";
    /**
     *
     */
    private static final long serialVersionUID = -118322660015469075L;

    private final Grid adminGrid;

    private final AvailaboardSQLConnection db = new AvailaboardSQLConnection();
    final AdminResourceGrid adminResourceGrid = new AdminResourceGrid();

    public AdminView() {
        adminGrid = adminResourceGrid.loadGrid();
        adminGrid.addClassName("admin-resource-grid");
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
        return Stream.of(Admin);
    }


    @Override
    public void initialize() {
        add(adminGrid);
    }

    @Override
    public String viewName() {
        return AdminView.VIEWNAME;
    }


    @Override
    public void update() {
        adminGrid.setItems(db.loadResources(User.class, false));
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
