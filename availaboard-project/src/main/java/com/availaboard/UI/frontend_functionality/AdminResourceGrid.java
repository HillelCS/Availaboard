package com.availaboard.UI.frontend_functionality;

import com.availaboard.UI.application_structure.observable.ViewFactory;
import com.availaboard.engine.resource.Permission;
import com.availaboard.engine.resource.User;
import com.availaboard.engine.sql_connection.AvailaboardSQLConnection;
import com.availaboard.engine.sql_connection.NameExistsException;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;

import java.util.Collection;

@CssImport("./styles/webpage-styles/admin-availaboard-grid.css")
public class AdminResourceGrid extends Grid {

    private final AvailaboardSQLConnection db = new AvailaboardSQLConnection();

    public Grid<User> loadGrid() {
        try {
            final Grid<User> grid = new Grid<>();
            final Column<User> nameColumn = grid.addColumn(User::getUsername).setHeader("Name").setWidth("40%")
                    .setFlexGrow(1).setTextAlign(ColumnTextAlign.CENTER);
            final Column<User> permissionColumn = grid.addComponentColumn(this::permissionPopButton).setHeader("Status").setWidth("25%")
                    .setFlexGrow(1).setTextAlign(ColumnTextAlign.CENTER);
            final Column<User> visibilityColumn = grid.addComponentColumn(this::visibilityPopupButton).setHeader("Visibility").setWidth("35%")
                    .setFlexGrow(1).setTextAlign(ColumnTextAlign.CENTER);


            grid.addClassName("availaboard-grid");
            grid.setAllRowsVisible(true);
            grid.setItems((Collection<User>) (db.loadResources(User.class, false)));

            final HeaderRow headerRow = grid.prependHeaderRow();
            final Div simpleNameCell = new Div();
            simpleNameCell.setText("Users");
            simpleNameCell.getElement().getStyle().set("text-align", "center");
            headerRow.join(nameColumn, permissionColumn, visibilityColumn).setComponent(simpleNameCell);

            return grid;
        } catch (final IllegalArgumentException | SecurityException e) {

        }
        return null;
    }

    private Button permissionPopButton(final User user) {
        final Dialog dialog = new Dialog();
        final VerticalLayout dialogLayout = createPermissionDialogLayout(dialog, user);
        dialog.add(dialogLayout);
        dialog.setModal(true);
        dialog.setDraggable(true);
        final Button button = new Button(user.getPermissions().toString(), e -> dialog.open());
        button.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        button.addClassName("popup-button");
        return button;
    }

    private Button visibilityPopupButton(final User user) {
        final Dialog dialog = new Dialog();
        final VerticalLayout dialogLayout = createGridVisibilityDialogLayout(dialog, user);
        dialog.add(dialogLayout);
        dialog.setModal(true);
        dialog.setDraggable(true);
        final Button button = new Button(String.valueOf(user.isVisibleInGrid()), e -> dialog.open());
        button.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        button.addClassName("popup-button");
        return button;
    }

    private VerticalLayout createGridVisibilityDialogLayout(Dialog dialog, User user) {

        final VerticalLayout dialogLayout = new VerticalLayout();

        final Select<Boolean> visibilityField = new Select<>();
        visibilityField.setLabel("Visible in grid");
        visibilityField.setItems(true, false);
        visibilityField.setValue(user.isVisibleInGrid());

        final Button confirmButton = new Button("Confirm", event -> {
            try {
                user.setVisibleInGrid(visibilityField.getValue());
                db.updateResourceInDatabase(user);
            } catch (NameExistsException e) {
                throw new RuntimeException(e);
            }
            ViewFactory.getViewControllerInstance().notifiyObservers();
            dialog.close();
        });

        confirmButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
        dialogLayout.add(visibilityField, confirmButton);

        dialog.setWidth("300px");
        dialog.setHeight("200px");

        return dialogLayout;
    }

    private VerticalLayout createPermissionDialogLayout(Dialog dialog, User user) {

        final VerticalLayout dialogLayout = new VerticalLayout();

        final Select<Permission> permissionField = new Select<>();
        permissionField.setLabel("Permission");
        permissionField.setItems(Permission.User, Permission.Admin);
        permissionField.setValue(user.getPermissions());

        final Button confirmButton = new Button("Confirm", event -> {
            try {
                user.setPermissions(permissionField.getValue());
                db.updateResourceInDatabase(user);
            } catch (NameExistsException e) {
                throw new RuntimeException(e);
            }
            ViewFactory.getViewControllerInstance().notifiyObservers();
            dialog.close();
        });

        confirmButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
        dialogLayout.add(permissionField, confirmButton);

        dialog.setWidth("300px");
        dialog.setHeight("200px");

        return dialogLayout;
    }
}
