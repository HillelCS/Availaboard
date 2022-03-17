package com.availaboard.UI;


import com.availaboard.engine.sql_connection.AvailaboardSQLConnection;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class Availaboard extends VerticalLayout {
	
	Grid grid = new Grid();
	VerticalLayout layout = new VerticalLayout();
	AvailaboardSQLConnection db = new AvailaboardSQLConnection();
	
	public Availaboard() {
		
	}
}
