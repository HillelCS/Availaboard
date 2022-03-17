package com.availaboard.UI;

import com.availaboard.engine.resource.Resource;
import com.availaboard.engine.resource.Status;
import com.availaboard.engine.sql_connection.AvailaboardSQLConnection;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.function.SerializableBiConsumer;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.router.Route;

@Route("")
@CssImport("./styles/availaboard-style.css")
public class Availaboard extends VerticalLayout {

	Grid<Resource> grid = new Grid<Resource>(Resource.class, false);
	VerticalLayout layout = new VerticalLayout();
	AvailaboardSQLConnection db = new AvailaboardSQLConnection();

	public Availaboard() {
		setUpGrid();
	}

	private void setUpGrid() {
		grid.addColumn(Resource -> (Resource).getName()).setHeader("Name");
		grid.addColumn(Resource -> (Resource).getContact()).setHeader("Contact");
		grid.addComponentColumn(Resource -> statusLabel(Resource)).setHeader("Status");
		grid.setItems(db.loadResources());
		grid.addClassName("availaboard-grid");
		add(grid);
	}

	private Label statusLabel(Resource res) {
		Label label = new Label();
		label.setText(res.getStatus().toString());
		String labelClassName = res.getStatus() == Status.AVAILABLE ? "label-available" : "label-busy";
		label.addClassName(labelClassName);
		
		return label;
	}
}
