package com.availaboard.UI;


import com.availaboard.engine.resource.Resource;
import com.availaboard.engine.sql_connection.AvailaboardSQLConnection;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.function.SerializableBiConsumer;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.router.Route;

@Route("")
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
		grid.addColumn(Resource -> (Resource).getStatus()).setHeader("Status");
		grid.setItems(db.loadResources());
		add(grid);
	}

	private final SerializableBiConsumer<Span, Resource> statusComponentUpdater = (span, resource) -> {
	    boolean isAvailable = "Available".equals(resource.getStatus().toString());
	    String theme = String
	            .format("badge %s", isAvailable ? "success" : "error");
	    span.getElement().setAttribute("theme", theme);
	    span.setText(resource.getStatus().toString());
	};
}
