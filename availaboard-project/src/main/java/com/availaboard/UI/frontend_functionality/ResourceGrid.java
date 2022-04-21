package com.availaboard.UI.frontend_functionality;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Stream;

import com.availaboard.engine.resource.Resource;
import com.availaboard.engine.resource.ResourceFieldLoader;
import com.availaboard.engine.resource.Status;
import com.availaboard.engine.sql_connection.AvailaboardSQLConnection;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

/**
 * Used to instantiate grid with the type <code>E</code>. The {@link Grid}
 * contains a <code>Name</code> and a <code>Status</code> column. It also has a
 * pop-up window displaying information about the {@link Resource} for each
 * item.
 *
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

	private AvailaboardSQLConnection db = new AvailaboardSQLConnection();

	private Class<? extends Resource> type;

	/**
	 * Used to set the type of {@link Resource} the {@link Grid} needs to load.
	 *
	 * @param type The type of {@link Resource} that the {@link Grid} use's to load
	 *             the {@link Resource}'s.
	 */
	public ResourceGrid(Class<? extends Resource> type) {
		this.type = type;
	}

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
	 *         <code> Field </code> names and value's added.
	 */
	private VerticalLayout createDialogLayout(Dialog dialog, Resource res) {
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
					ResourceFieldLoader fieldLoader = field.getAnnotation(ResourceFieldLoader.class);
					Label label = new Label(fieldLoader.value() + ": " + field.get(res));
					fieldLayout.add(label);
				} catch (IllegalArgumentException | IllegalAccessException e1) {
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

		dialogLayout.getStyle().set("width", "350px").set("max-width", "450px");

		return dialogLayout;
	}

	/**
	 * Creates a new {@link Grid} with two columns; <code>Name</code> and
	 * <code>Status</code>. Then it loads all of the {@link Resource}'s and add's
	 * them to the grid.
	 *
	 * @param res The type of {@link Resource} that the {@link Grid} use's to load
	 *            the {@link Resource}'s.
	 * @return A {@link Grid} with two columns and all of the {@link Resource} of
	 *         type <code>E</code> added.
	 */

	public Grid<E> loadGrid(Class<? extends Resource> res) {
		try {
			Grid<E> grid = new Grid<>();
			Column<E> nameColumn = grid.addComponentColumn(E -> popUpResource(E)).setHeader("Name").setWidth("50%")
					.setFlexGrow(1).setTextAlign(ColumnTextAlign.CENTER);
			Column<E> statusColumn = grid.addComponentColumn(E -> statusLabel(E)).setHeader("Status").setWidth("50%")
					.setFlexGrow(1).setTextAlign(ColumnTextAlign.CENTER);
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

	/**
	 * Shows {@link Dialog} with all the {@link Resource} information.
	 *
	 * @param res The {@link Resource} that's information is added to a
	 *            {@link Dialog}.
	 * @return A {@link Button} that opens a {@link Dialog} when clicked.
	 */
	private Button popUpResource(Resource res) {
		Dialog dialog = new Dialog();
		dialog.getElement().setAttribute("aria-label", res.getName());
		VerticalLayout dialogLayout = createDialogLayout(dialog, res);
		dialog.add(dialogLayout);
		dialog.setModal(true);
		dialog.setDraggable(true);
		Button button = new Button(res.getName(), e -> dialog.open());
		button.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
		button.addClassName("popup-button");
		return button;
	}

	/**
	 * Changes the color of the <code>Status</code> column by adding a
	 * <code> CSS Class </code> depending on whether it is <code>Busy</code> or
	 * <code>Available</code>. (Green if <code>Available</code>, red if
	 * <code>Busy</code>.
	 *
	 * @param res The {@link Resource} used to see if it's {@link Status} is
	 *            <code>Busy</code> or <code>Available</code>
	 * @return A {@link Label} with text set to the {@link Status} of the
	 *         {@link Resource} and corresponding color.
	 */
	private Label statusLabel(Resource res) {
		Label label = new Label();
		label.setText(res.getStatus().toString());
		String labelClassName = res.getStatus() == Status.AVAILABLE ? "label-available" : "label-busy";
		label.addClassName(labelClassName);
		return label;
	}
}
