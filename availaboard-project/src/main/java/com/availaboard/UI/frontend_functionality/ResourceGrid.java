package com.availaboard.UI.frontend_functionality;

import java.util.Collection;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.router.RouteParameters;
import com.vaadin.flow.router.RouterLink;
import com.availaboard.UI.webpage.ResourcePage;
import com.availaboard.engine.resource.Resource;
import com.availaboard.engine.resource.Status;
import com.availaboard.engine.sql_connection.AvailaboardSQLConnection;

@CssImport("./styles/webpage-styles/availaboard-grid.css")
public class ResourceGrid<E extends Resource> extends Grid {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4590209736315740168L;
	AvailaboardSQLConnection db = new AvailaboardSQLConnection();


	private Class<E> type;

	public ResourceGrid(Class<E> type) {
		this.type = type;
	}

	/*
	 * Returns the grid for the Availaboard. The type of object
	 * that will be loaded is specified in the constructor. It automatically adds to
	 * columns, Name and Status. Since all objects being passed in have to extend
	 * from the Resource object they will have those fields. Each columns
	 * have corresponding methods to make sure they do the 
	 * functionality needed. The Name class takes the user to a 
	 * unique web page based off of the UserID. The Status
	 * column uses a method to change the color of the Status
	 * to Red, if the text is "Busy" or green if the text is
	 * "Available." Finally, the items are loaded to the grid
	 * with the grid.setItems() method. They are loaded from the database,
	 * (AvailaboardSQLConnection) and casted to a collection because the grid
	 * requires a collection object, but the database method returns an
	 * arraylist. The type of the object is passed as a parameter which will
	 * then load the right type of object from the database.
	 */
	public Grid<E> loadGrid() {
		Grid<E> grid = new Grid<E>();
		grid.addComponentColumn(E -> resourceLink(E)).setHeader("Name").setWidth("50%").setFlexGrow(0)
				.setTextAlign(ColumnTextAlign.CENTER);
		grid.addComponentColumn(E -> statusLabel(E)).setHeader("Status").setWidth("50%").setFlexGrow(0)
				.setTextAlign(ColumnTextAlign.CENTER);
		grid.addClassName("availaboard-grid");
		grid.setAllRowsVisible(true);
		grid.setItems((Collection<E>)(db.loadResources(type)));
	
		return grid;
	}

	/*
	 * Adds a CSS class to the label depending off of if it is
	 * available or busy.
	 */
	private Label statusLabel(Resource res) {
		Label label = new Label();
		label.setText(res.getStatus().toString());
		String labelClassName = res.getStatus() == Status.AVAILABLE ? "label-available" : "label-busy";
		label.addClassName(labelClassName);
		return label;
	}

	/*
	 * Takes the user to a unique web page by passing the ResourcePage
	 * class @Route annotation a RouteParamaters object with the 
	 * ID of the specific Resource.  
	 */
	private RouterLink resourceLink(Resource res) {
		RouterLink link = new RouterLink(res.getName(), ResourcePage.class, new RouteParameters("resourceID", String.valueOf(res.getId())));
		return link;
	}
}



