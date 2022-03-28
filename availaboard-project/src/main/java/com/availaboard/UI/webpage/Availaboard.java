package com.availaboard.UI.webpage;

import com.availaboard.UI.frontend_functionality.ResourceGrid;
import com.availaboard.engine.resource.Resource;
import com.availaboard.engine.resource.User;
import com.availaboard.engine.sql_connection.AvailaboardSQLConnection;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;

@PageTitle("Availaboard")
@Route("/")
@Theme(value = Lumo.class, variant = Lumo.DARK)
public class Availaboard extends VerticalLayout {

	Grid<Resource> grid = new Grid<Resource>(Resource.class);
	VerticalLayout layout = new VerticalLayout();
	AvailaboardSQLConnection db = new AvailaboardSQLConnection();

	/*
	 * Constructs the Availaboard grids. To create a new grid
	 * with a type of Resource it is very simple. First, 
	 * create the class, extends it from Resource (mandatory) and specify the fields that should be loaded
	 * with the @ResourceFieldLoader annotation. Then, create a new 
	 * ResourceGrid, pass in the type of object that you created as a 
	 * generic type and pass in that object as a class in the parameter
	 * for the constructor. Finally call the loadGrid() function which
	 * return a Grid object with all of the objects loaded to it. 
	 * Then add the grid to the layout. 
	 * 
	 */
	public Availaboard() {
		ResourceGrid<User> userGrid = new ResourceGrid<User>(User.class);
		Grid<User> userGridLoader = userGrid.loadGrid();
		layout.add(userGridLoader);
		layout.setHorizontalComponentAlignment(Alignment.CENTER, userGridLoader);
		add(layout);
	}

}
