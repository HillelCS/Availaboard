package com.availaboard.UI;

<<<<<<< Updated upstream

=======
import com.availaboard.engine.resource.Resource;
import com.availaboard.engine.resource.Status;
>>>>>>> Stashed changes
import com.availaboard.engine.sql_connection.AvailaboardSQLConnection;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
<<<<<<< Updated upstream
=======
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
>>>>>>> Stashed changes
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

<<<<<<< Updated upstream
public class Availaboard extends VerticalLayout {
	
	Grid grid = new Grid();
=======
@Route("")
@CssImport("./styles/availaboard-style.css")
public class Availaboard extends VerticalLayout {

	Grid<Resource> grid = new Grid<Resource>(Resource.class, false);
>>>>>>> Stashed changes
	VerticalLayout layout = new VerticalLayout();
	AvailaboardSQLConnection db = new AvailaboardSQLConnection();

	public Availaboard() {
		
	}
<<<<<<< Updated upstream
=======

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
>>>>>>> Stashed changes
}
