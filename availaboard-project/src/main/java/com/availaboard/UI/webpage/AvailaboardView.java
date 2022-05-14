package com.availaboard.UI.webpage;

import com.availaboard.UI.application_structure.observable.Observer;
import com.availaboard.UI.application_structure.observable.Subject;
import com.availaboard.UI.application_structure.observable.ViewFactory;
import com.availaboard.UI.frontend_functionality.ResourceGrid;
import com.availaboard.engine.resource.*;
import com.availaboard.engine.security.AccessControl;
import com.availaboard.engine.security.AccessControlFactory;
import com.availaboard.engine.sql_connection.AvailaboardSQLConnection;
import com.availaboard.engine.sql_connection.NameExistsException;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;

import java.lang.reflect.Field;
import java.util.Arrays;
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

    private final AvailaboardSQLConnection db = new AvailaboardSQLConnection();

    private final Div container = new Div();

    private final AccessControl accessControl;

    private final Grid<Resource> userGrid = createResourceGrid(User.class);
    private final Grid<Resource> equipmentGrid = createResourceGrid(Equipment.class);
    private final Grid<Resource> roomGrid = createResourceGrid(Room.class);

    public AvailaboardView() {
        accessControl = AccessControlFactory.getInstance().createAccessControl();
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

    /**
     * Adds the addResourceButton if the {@link CurrentUser} is an Admin.
     * The addResourceButton creates a NewResourceDialogLayout when it is clicked.
     * It passes in the {@link Resource} selected in the
     * {@link Select}.
     */
    private void addResourceButtonIfApplicable() {
        if (accessControl.isUserInRole(Permission.Admin)) {

            final Select<String> select = new Select<>();
            final HorizontalLayout horizontalLayout = new HorizontalLayout();

            final Button addResourceButton = new Button(new Icon(VaadinIcon.PLUS), event -> {
                final Dialog dialog = new Dialog();
                final VerticalLayout dialogLayout;
                dialogLayout = createNewResourceDialogLayout(dialog, ResourceFactory.createResource(select.getValue()));
                dialog.add(dialogLayout);
                dialog.setModal(true);
                dialog.setDraggable(true);
                dialog.open();
            });

            select.setItems(Room.class.getSimpleName(), Equipment.class.getSimpleName());
            select.setValue(Room.class.getSimpleName());

            horizontalLayout.add(addResourceButton, select);
            add(horizontalLayout);
            setHorizontalComponentAlignment(Alignment.END, horizontalLayout);
        }
    }


    /**
     * <p>
     * A {@link VerticalLayout} created to be added to a {@link Dialog}. The Method
     * iterates through every <code> Field </code> in the {@link Resource} with the
     * {@link ResourceFieldLoader} and displays it's label as the Field's "nickname,"
     * and provides a {@link TextField} for the User to input that Fields information in.
     * When the values of the TextFields changes it set's that value to the value of the
     * dialogResource.
     *
     * The dialogResource is cloned so the res being passed in is not mutated.
     * If the add button is pressed then the dialogResource is inserted into the
     * database. If the cancel button is hit then the Dialog is closed.
     *
     * @param dialog The {@link Dialog} that this specific {@link VerticalLayout}
     *               should be added to.
     * @param res    The {@link Resource} with the fields that will be added to the
     *               {@link VerticalLayout}.
     * @return A {@link VerticalLayout} with all the {@link ResourceFieldLoader}'s
     * <code> Field </code> names and value's added.
     */
    private VerticalLayout createNewResourceDialogLayout(final Dialog dialog, final Resource res) {

        final Resource dialogResource = ResourceFactory.createResource(res.getClass().getSimpleName());

        final VerticalLayout dialogLayout = new VerticalLayout();

        final H2 headline = new H2("Create a new " + res.getClass().getSimpleName());
        headline.getStyle().set("margin", "0").set("font-size", "2.5em").set("font-weight", "bold");
        final HorizontalLayout header = new HorizontalLayout(headline);
        header.getElement().getClassList().add("draggable");
        header.setSpacing(false);
        header.getStyle().set("border-bottom", "1px solid var(--lumo-contrast-20pct)").set("cursor", "move");
        header.getStyle().set("padding", "var(--lumo-space-m) var(--lumo-space-l)").set("margin", "calc(var(--lumo-space-s) * -1) calc(var(--lumo-space-l) * -1) 0");

        final VerticalLayout fieldLayout = new VerticalLayout();
        final Field[] resourceFields = res.getClass().getDeclaredFields();

        final TextField nameField = new TextField("Name");
        final Select<Status> statusField = new Select<>();
        statusField.setLabel("Status");
        statusField.setItems(Status.AVAILABLE, Status.BUSY);
        statusField.setValue(Status.AVAILABLE);

        fieldLayout.add(nameField, statusField);

        final Stream<Field> stream = Arrays.stream(resourceFields);

        stream.forEach(field -> {
            if (field.isAnnotationPresent(ResourceFieldLoader.class)) {
                try {
                    field.setAccessible(true);
                    final ResourceFieldLoader fieldLoader = field.getAnnotation(ResourceFieldLoader.class);
                    TextField textField = new TextField(fieldLoader.value());
                    textField.addValueChangeListener(change -> {
                        try {
                            field.set(dialogResource, change.getValue());
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException(e);
                        }
                    });
                    fieldLayout.add(textField);
                } catch (final IllegalArgumentException e1) {
                    e1.printStackTrace();
                }
            }
        });

        fieldLayout.setSpacing(false);
        fieldLayout.setPadding(false);

        final Button cancelButton = new Button("Cancel", e -> dialog.close());
        final Button finishedButton = new Button("Add", e -> {
            try {
                dialogResource.setName(nameField.getValue());
                dialogResource.setStatus(statusField.getValue());
                db.insertResourceIntoDatabase(dialogResource);
                ViewFactory.getViewControllerInstance().notifiyObservers();
                dialog.close();
            } catch (NameExistsException ex) {
                //TODO add error notificaiton
                throw new RuntimeException(ex);
            }
        });
        final HorizontalLayout buttonLayout = new HorizontalLayout(finishedButton, cancelButton);
        dialogLayout.add(header, fieldLayout, buttonLayout);
        dialogLayout.setPadding(false);

        dialogLayout.getStyle().set("width", "350px");

        return dialogLayout;
    }

    @Override
    public void initialize() {
        addResourceButtonIfApplicable();
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
