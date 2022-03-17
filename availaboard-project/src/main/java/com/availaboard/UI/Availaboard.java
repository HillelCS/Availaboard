package com.availaboard.UI;

import com.availaboard.engine.resource.Resource;
import com.availaboard.engine.resource.Status;
import com.availaboard.engine.sql_connection.AvailaboardSQLConnection;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.NativeButton;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.function.SerializableBiConsumer;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteParam;
import com.vaadin.flow.router.RouteParameters;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;

@PageTitle("Availaboard")
@Route("")
@CssImport("./styles/availaboard-style.css")
@Theme(value = Lumo.class, variant = Lumo.DARK)

public class Availaboard extends VerticalLayout {

	Grid<Resource> grid = new Grid<Resource>(Resource.class, false);
	VerticalLayout layout = new VerticalLayout();
	AvailaboardSQLConnection db = new AvailaboardSQLConnection();

	public Availaboard() {
		setUpGrid();
		// addLabel();
	}

	private void addLabel() {
		Label label = new Label("Availaboard");
		label.addClassName("availaboard-label");
		add(label);
	}

	private void setUpGrid() {
		grid.addComponentColumn(Resource -> resourceLink(Resource)).setHeader("Name").setWidth("33.333%")
				.setFlexGrow(0).setTextAlign(ColumnTextAlign.CENTER);
		grid.addColumn(Resource -> (Resource).getContact()).setHeader("Contact").setWidth("33.333%").setFlexGrow(0)
				.setTextAlign(ColumnTextAlign.CENTER);
		grid.addComponentColumn(Resource -> statusLabel(Resource)).setHeader("Status").setWidth("33.333%")
				.setFlexGrow(0).setTextAlign(ColumnTextAlign.CENTER);
		grid.setItems(db.loadResources());
		grid.addClassName("availaboard-grid");
		grid.setAllRowsVisible(true);
		add(grid);
		layout.setHorizontalComponentAlignment(Alignment.CENTER, grid);
	}

	private Label statusLabel(Resource res) {
		Label label = new Label();
		label.setText(res.getStatus().toString());
		String labelClassName = res.getStatus() == Status.AVAILABLE ? "label-available" : "label-busy";
		label.addClassName(labelClassName);
		return label;
	}

	private RouterLink resourceLink(Resource res) {
		NativeButton editButton = new NativeButton("Edit user details");
		editButton.addClickListener(e -> editButton.getUI().navigate(ResourcePage.class, new RouteParameters("userID", "123"))));
	}
}
