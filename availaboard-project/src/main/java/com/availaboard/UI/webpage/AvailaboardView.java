package com.availaboard.UI.webpage;

import com.availaboard.UI.frontend_functionality.ResourceGrid;
import com.availaboard.UI.application_structure.observable.Observer;
import com.availaboard.UI.application_structure.observable.Subject;
import com.availaboard.UI.application_structure.observable.ViewFactory;
import com.availaboard.engine.resource.Equipment;
import com.availaboard.engine.resource.Resource;
import com.availaboard.engine.resource.Room;
import com.availaboard.engine.resource.User;
import com.availaboard.engine.sql_connection.AvailaboardSQLConnection;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;

@PageTitle("Availaboard")
@CssImport("./styles/webpage-styles/availaboard.css")
@Theme(themeClass = Lumo.class, variant = Lumo.DARK)
@Route(value = AvailaboardView.VIEWNAME, layout = MainLayout.class)
public class AvailaboardView extends VerticalLayout implements AppShellConfigurator, Observer {

    protected static final String VIEWNAME = "/";
    /**
     *
     */
    private static final long serialVersionUID = -4432887017833022089L;

    AvailaboardSQLConnection db = new AvailaboardSQLConnection();

    Div container = new Div();

    private final Grid<Resource> userGrid = createResourceGrid(User.class);
    private final Grid<Resource> equipmentGrid = createResourceGrid(Equipment.class);
    private final Grid<Resource> roomGrid = createResourceGrid(Room.class);

    public AvailaboardView() {
        container.addClassName("grid-container");
        container.add(gridLayout());
    }

    private FormLayout gridLayout() {
        final FormLayout layout = new FormLayout();
        layout.add(userGrid);
        layout.add(equipmentGrid);
        layout.add(roomGrid);
        layout.setResponsiveSteps(new FormLayout.ResponsiveStep("0px", 1), new FormLayout.ResponsiveStep("750px", 3));
        layout.addClassName("grid-form-layout");
        return layout;
    }

    private Grid<Resource> createResourceGrid(Class<? extends Resource> res) {
        final ResourceGrid<Resource> grid = new ResourceGrid<>();
        return grid.loadGrid(res);
    }

    @Override
    public void initialize() {
        add(container);
    }

    @Override
    public String viewName() {
        return VIEWNAME;
    }

    @Override
    public void update() {
        userGrid.setItems((Collection) (db.loadResources(User.class)));
        equipmentGrid.setItems((Collection) (db.loadResources(Equipment.class)));
        roomGrid.setItems((Collection) (db.loadResources(Room.class)));
    }

    @Override
    public void register(Subject subject) {
        subject.addObserver(this);
    }

    @Override
    public void unregister(Subject subject) {
        subject.addObserver(this);
    }

    @Override
    public Subject getSubject() {
        return ViewFactory.getViewControllerInstance();
    }

    @Override
    public Optional<UI> getUI() {
        return super.getUI();
    }
}
