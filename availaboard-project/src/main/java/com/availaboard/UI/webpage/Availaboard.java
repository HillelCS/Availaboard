package com.availaboard.UI.webpage;

import com.availaboard.UI.frontend_functionality.ResourceGrid;
import com.availaboard.engine.resource.Resource;
import com.availaboard.engine.resource.Status;
import com.availaboard.engine.resource.User;
import com.availaboard.engine.sql_connection.AvailaboardSQLConnection;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteParameters;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;

@PageTitle("Availaboard")
@Route("")

@Theme(value = Lumo.class, variant = Lumo.DARK)

public class Availaboard extends VerticalLayout {

	Grid<Resource> grid = new Grid<Resource>(Resource.class, false);
	VerticalLayout layout = new VerticalLayout();
	AvailaboardSQLConnection db = new AvailaboardSQLConnection();

	public Availaboard() {
		ResourceGrid<User> userGrid = new ResourceGrid<User>(User.class);
		Grid<User> userGridLoader = userGrid.loadGrid();
		layout.add(userGridLoader);
		layout.setHorizontalComponentAlignment(Alignment.CENTER, userGridLoader);
		add(layout);
	}

}
