package com.availaboard.UI.frontend_functionality;

import com.availaboard.UI.application_structure.observable.ViewFactory;
import com.availaboard.engine.resource.Permission;
import com.availaboard.engine.resource.Resource;
import com.availaboard.engine.resource.ResourceFieldLoader;
import com.availaboard.engine.resource.Status;
import com.availaboard.engine.security.AccessControl;
import com.availaboard.engine.security.AccessControlFactory;
import com.availaboard.engine.sql_connection.AvailaboardSQLConnection;
import com.availaboard.engine.sql_connection.NameExistsException;
import com.availaboard.engine.sql_connection.ResourceDoesNotExistException;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Stream;

/**
 * Used to instantiate grid with the type <code>E</code>. The {@link Grid}
 * contains a <code>Name</code> and a <code>Status</code> column. It also has a
 * pop-up window displaying information about the {@link Resource} for each
 * item.
 *
 * @param <E> The <code>Class</code> that is used to tell the {@link Grid} which
 *            Fields must be loaded.
 */
@CssImport("./styles/webpage-styles/availaboard-grid.css")
public class ResourceGrid<E extends Resource> extends Grid {

    /**
     *
     */
    private static final long serialVersionUID = -2025064138726788718L;
    private final AccessControl accessControl = AccessControlFactory.getInstance().createAccessControl();
    private final AvailaboardSQLConnection db = new AvailaboardSQLConnection();
    private String content;


    /**
     * <p>
     * A {@link VerticalLayout} created to be added to a {@link Dialog}. The Method
     * iterates through every <code> Field </code> in the {@link Resource} with the
     * {@link ResourceFieldLoader} and displays the <code>Value</code> ("nickname")
     * <code> Field </code> in it, followed by a colon then the <code>Field</code>
     * value.
     *
     * @param dialog The {@link Dialog} that this specific {@link VerticalLayout}
     *               should be added to.
     * @param res    The {@link Resource} with the fields that will be added to the
     *               {@link VerticalLayout}.
     * @return A {@link VerticalLayout} with all the {@link ResourceFieldLoader}'s
     * <code> Field </code> names and value's added.
     */
    private VerticalLayout createDialogLayout(final Dialog dialog, final Resource res) {

        final VerticalLayout dialogLayout = new VerticalLayout();

        final H2 headline = new H2(res.getName());
        headline.getStyle().set("margin", "0").set("font-size", "2.5em").set("font-weight", "bold");
        final HorizontalLayout header = new HorizontalLayout(headline);
        header.getElement().getClassList().add("draggable");
        header.setSpacing(false);
        header.getStyle().set("border-bottom", "1px solid var(--lumo-contrast-20pct)").set("cursor", "move");
        header.getStyle().set("padding", "var(--lumo-space-m) var(--lumo-space-l)").set("margin",
                "calc(var(--lumo-space-s) * -1) calc(var(--lumo-space-l) * -1) 0");

        final VerticalLayout fieldLayout = new VerticalLayout();
        final Field[] resourceFields = res.getClass().getDeclaredFields();

        final Stream<Field> stream = Arrays.stream(resourceFields);

        stream.forEach(field -> {
            if (field.isAnnotationPresent(ResourceFieldLoader.class)) {
                try {
                    field.setAccessible(true);
                    final ResourceFieldLoader fieldLoader = field.getAnnotation(ResourceFieldLoader.class);
                    final Label label = new Label(fieldLoader.value() + ": " + field.get(res));
                    fieldLayout.add(label);

                } catch (final IllegalArgumentException | IllegalAccessException e1) {
                    e1.printStackTrace();
                }
            }
        });

        fieldLayout.setSpacing(false);
        fieldLayout.setPadding(false);

        final Button finishedButton = new Button("Done", e -> dialog.close());
        finishedButton.setAutofocus(true);
        final HorizontalLayout buttonLayout = new HorizontalLayout(finishedButton);
        dialogLayout.add(header, fieldLayout, buttonLayout);
        dialogLayout.setPadding(false);

        dialogLayout.getStyle().set("width", "350px");

        return dialogLayout;
    }

    /**
     * Creates a new {@link Grid} with two columns; <code>Name</code> and
     * <code>Status</code>. Then it loads all of the {@link Resource}'s and add's
     * them to the grid. If the {@link CurrentUser} has {@link Permission#Admin} then
     * they get the delete button added to the grid.
     *
     * @param res The type of {@link Resource} that the {@link Grid} use's to load
     *            the {@link Resource}'s.
     * @return A {@link Grid} with two columns and all of the {@link Resource} of
     * type <code>E</code> added.
     */

    public Grid<E> loadGrid(final Class<? extends Resource> res) {
        try {
            final Grid<E> grid = new Grid<>();
            final Column<E> nameColumn;
            final Column<E> statusColumn;

            if (accessControl.isUserInRole(Permission.Admin)) {
                nameColumn = grid.addComponentColumn(this::dialogPopupButtonWithDeleteButton).setHeader("Name").setWidth("50%")
                        .setFlexGrow(1).setTextAlign(ColumnTextAlign.CENTER);
                statusColumn = grid.addComponentColumn(this::statusPopupButton).setHeader("Status").setWidth("50%")
                        .setFlexGrow(1).setTextAlign(ColumnTextAlign.CENTER);
            } else {
                nameColumn = grid.addComponentColumn(this::dialogPopupButton).setHeader("Name").setWidth("50%")
                        .setFlexGrow(1).setTextAlign(ColumnTextAlign.CENTER);
                statusColumn = grid.addComponentColumn(VaadinComponentUtilitys::statusLabel).setHeader("Status").setWidth("50%")
                        .setFlexGrow(1).setTextAlign(ColumnTextAlign.CENTER);
            }


            grid.addClassName("availaboard-grid");
            grid.setAllRowsVisible(true);
            grid.setItems((Collection<E>) (db.loadResources(res)));

            final HeaderRow headerRow = grid.prependHeaderRow();
            final Div simpleNameCell = new Div();
            simpleNameCell.setText(res.getSimpleName());
            simpleNameCell.getElement().getStyle().set("text-align", "center");
            headerRow.join(nameColumn, statusColumn).setComponent(simpleNameCell);

            return grid;
        } catch (final IllegalArgumentException | SecurityException e) {

        }
        return null;
    }

    /**
     * Confirms the User would like to delete the selected {@link Resource}.
     *
     * @param dialog
     * @param res    The {@link Resource} being deleted
     * @return A {@link ConfirmDialog} that pops up confirming the User would like to delete the selected {@link Resource}.
     */
    private VerticalLayout createConfirmDeleteDialog(final Dialog dialog, final Resource res) {
        final VerticalLayout dialogLayout = new VerticalLayout();

        final Label headline = new Label("Are you sure you would like to delete this item? (" + res.getName() + ")");
        headline.getStyle().set("margin", "0").set("font-size", "1.5em");

        final Button cancelButton = new Button("Cancel", event -> dialog.close());
        final Button confirmButton = new Button("Confirm", event -> {
            try {
                db.deleteResourceFromDatabase(res);
            } catch (ResourceDoesNotExistException e) {
                throw new RuntimeException(e);
            }
            ViewFactory.getViewControllerInstance().notifiyObservers();
            dialog.close();
        });

        confirmButton.setAutofocus(true);

        confirmButton.setClassName("confirm-button");

        cancelButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        confirmButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);

        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(cancelButton, confirmButton);
        horizontalLayout.addClassName("button-container");

        dialogLayout.add(headline, horizontalLayout);
        dialogLayout.setHorizontalComponentAlignment(FlexComponent.Alignment.END, horizontalLayout);

        dialog.setWidth("400px");
        dialog.setHeight("250px");

        return dialogLayout;

    }

    /**
     * Shows {@link Dialog} with all the {@link Resource} information.
     *
     * @param res The {@link Resource} that's information is added to a
     *            {@link Dialog}.
     * @return A {@link Button} that opens a {@link Dialog} when clicked.
     */
    private Button dialogPopupButton(final Resource res) {
        final Dialog dialog = new Dialog();
        dialog.getElement().setAttribute("aria-label", res.getName());
        final VerticalLayout dialogLayout = createDialogLayout(dialog, res);
        dialog.add(dialogLayout);
        dialog.setModal(true);
        dialog.setDraggable(true);
        final Button button = new Button(res.getName(), e -> dialog.open());
        button.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        button.addClassName("popup-button");
        return button;
    }

    private Button confirmDeletePopupButton(final Resource res) {
        final Dialog dialog = new Dialog();
        dialog.getElement().setAttribute("aria-label", res.getName());
        final VerticalLayout dialogLayout = createConfirmDeleteDialog(dialog, res);
        dialog.add(dialogLayout);
        dialog.setModal(true);
        dialog.setDraggable(true);
        final Button button = new Button(new Icon(VaadinIcon.TRASH), e -> dialog.open());
        button.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        return button;
    }

    /**
     * Adds both the dialogPopupButton() and confirmDeleteDialog()
     * {@link Button}'s to a {@link HorizontalLayout}.
     *
     * @param res The selected {@link Resource}.
     * @return A {@link HorizontalLayout} of the dialogPopupButton() and confirmDeleteDialog()
     * * {@link Button}s.
     */
    private HorizontalLayout dialogPopupButtonWithDeleteButton(final Resource res) {
        HorizontalLayout layout = new HorizontalLayout();
        layout.add(confirmDeletePopupButton(res), dialogPopupButton(res));
        return layout;
    }

    private Button statusPopupButton(final Resource res) {
        final Dialog dialog = new Dialog();
        final VerticalLayout dialogLayout = createStatusDialogLayout(dialog, res);
        dialog.add(dialogLayout);
        dialog.setModal(true);
        dialog.setDraggable(true);
        final Button button = new Button(VaadinComponentUtilitys.statusLabel(res), e -> dialog.open());
        button.addClassName("status-popup-button");
        return button;
    }

    private VerticalLayout createStatusDialogLayout(final Dialog dialog, final Resource res) {

        final VerticalLayout dialogLayout = new VerticalLayout();


        final Select<Status> statusField = new Select<>();
        statusField.setLabel("Status");
        statusField.setItems(Status.AVAILABLE, Status.BUSY);
        statusField.setValue(res.getStatus());

        final Button confirmButton = new Button("Confirm", event -> {
            try {
                res.setStatus(statusField.getValue());
                db.updateResourceInDatabase(res);
            } catch (NameExistsException e) {
                throw new RuntimeException(e);
            }
            ViewFactory.getViewControllerInstance().notifiyObservers();
            dialog.close();
        });

        confirmButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
        dialogLayout.add(statusField, confirmButton);

        dialog.setWidth("300px");
        dialog.setHeight("200px");

        return dialogLayout;
    }
}
