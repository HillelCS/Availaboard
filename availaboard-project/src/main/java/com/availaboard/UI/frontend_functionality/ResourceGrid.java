package com.availaboard.UI.frontend_functionality;

import java.awt.List;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.availaboard.engine.resource.Resource;
import com.availaboard.engine.resource.ResourceFieldLoader;
import com.availaboard.engine.resource.Status;
import com.availaboard.engine.resource.User;
import com.availaboard.engine.sql_connection.AvailaboardSQLConnection;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.RouteParam;
import com.vaadin.flow.router.RouteParameters;
import com.vaadin.flow.router.RouterLink;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@CssImport("./styles/webpage-styles/availaboard-grid.css")
public class ResourceGrid<E extends Resource> extends Grid {
	/**
	 *
	 */
	private static final long serialVersionUID = -4590209736315740168L;
	AvailaboardSQLConnection db = new AvailaboardSQLConnection();

	private Class<? extends Resource> type;

	public ResourceGrid(Class<? extends Resource> type) {
		this.type = type;
	}

	/*
	 * Returns the grid for the Availaboard. The type of object that will be loaded
	 * is specified in the constructor. It automatically adds to columns, Name and
	 * Status. Since all objects being passed in have to extend from the Resource
	 * object they will have those fields. Each columns have corresponding methods
	 * to make sure they do the functionality needed. The Name class takes the user
	 * to a unique web page based off of the UserID. The Status column uses a method
	 * to change the color of the Status to Red, if the text is "Busy" or green if
	 * the text is "Available." Finally, the items are loaded to the grid with the
	 * grid.setItems() method. They are loaded from the database,
	 * (AvailaboardSQLConnection) and casted to a collection because the grid
	 * requires a collection object, but the database method returns an arraylist.
	 * The type of the object is passed as a parameter which will then load the
	 * right type of object from the database.
	 */

	public Grid<E> loadGrid(Class<? extends Resource> res) {
		try {
			Grid<E> grid = new Grid<>();
			Column<E> nameColumn = grid.addComponentColumn(E -> popUpResource(E)).setHeader("Name").setWidth("50%")
					.setFlexGrow(0).setTextAlign(ColumnTextAlign.CENTER);
			Column<E> statusColumn = grid.addComponentColumn(E -> statusLabel(E)).setHeader("Status").setWidth("50%")
					.setFlexGrow(0).setTextAlign(ColumnTextAlign.CENTER);
			grid.addClassName("availaboard-grid");
			grid.setAllRowsVisible(true);
			grid.setItems((Collection<E>) (db.loadResources(type)));


			HeaderRow headerRow = grid.prependHeaderRow();
			Div simpleNameCell = new Div();
			simpleNameCell.setText(res.getSimpleName());
			simpleNameCell.getElement().getStyle().set("text-align", "center");
			headerRow.join(nameColumn, statusColumn).setComponent(simpleNameCell);

			return grid;
		} catch (IllegalArgumentException | SecurityException e) {

		}
		return null;

	}

	/*
	 * Adds a CSS class to the label depending off of if it is available or busy.
	 */
	private Label statusLabel(Resource res) {
		Label label = new Label();
		label.setText(res.getStatus().toString());
		String labelClassName = res.getStatus() == Status.AVAILABLE ? "label-available" : "label-busy";
		label.addClassName(labelClassName);
		return label;
	}

	/*
	 * Takes the user to a unique web page by passing the ResourcePage class @Route
	 * annotation a RouteParamaters object with the ID of the specific Resource.
	 */
	private Button popUpResource(Resource res) {
		Dialog dialog = new Dialog();
		dialog.getElement().setAttribute("aria-label", res.getName());
		VerticalLayout dialogLayout = createDialogLayout(dialog, res);
		dialog.add(dialogLayout);
		dialog.setModal(true);

		dialog.setDraggable(true);
		dialog.setMaxWidth("450px");
		Button button = new Button(res.getName(), e -> dialog.open());
		button.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
		return button;
	}

	private static VerticalLayout createDialogLayout(Dialog dialog, Resource resourceObj) {
		AvailaboardSQLConnection db = new AvailaboardSQLConnection();
		Resource res = db.createResourceWithID(resourceObj.getId(), resourceObj.getClass());

				
		H2 headline = new H2(res.getName());
		headline.getStyle().set("margin", "0").set("font-size", "2.5em").set("font-weight", "bold");
		HorizontalLayout header = new HorizontalLayout(headline);
		header.getElement().getClassList().add("draggable");
		header.setSpacing(false);
		header.getStyle().set("border-bottom", "1px solid var(--lumo-contrast-20pct)").set("cursor", "move");
		header.getStyle().set("padding", "var(--lumo-space-m) var(--lumo-space-l)").set("margin",
				"calc(var(--lumo-space-s) * -1) calc(var(--lumo-space-l) * -1) 0");
        
	 
		VerticalLayout fieldLayout = new VerticalLayout();
		Field[] resourceFields = res.getClass().getDeclaredFields();
		Stream<Field> stream = Arrays.stream(resourceFields);
		stream.forEach(field -> {
			if (field.isAnnotationPresent(ResourceFieldLoader.class)) {
				try {
					field.setAccessible(true);
					Label label = new Label(res.getFieldName(field.getName()) + ": " + field.get(res));					
					fieldLayout.add(label);
				} catch (IllegalArgumentException e1) {
					e1.printStackTrace();
				} catch (IllegalAccessException e1) {
					e1.printStackTrace();
				}
			}
		});

		fieldLayout.setSpacing(false);
		fieldLayout.setPadding(false);

		Button finishedButton = new Button("Done", e -> dialog.close());
		HorizontalLayout buttonLayout = new HorizontalLayout(finishedButton);

		VerticalLayout dialogLayout = new VerticalLayout(header, fieldLayout, buttonLayout);
		dialogLayout.setPadding(false);

		dialogLayout.getStyle().set("width", "300px").set("max-width", "450px");

		return dialogLayout;
	}
}
