package com.availaboard.UI.frontend_functionality;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Stream;

import com.availaboard.UI.webpage.ResourcePage;
import com.availaboard.engine.resource.Resource;
import com.availaboard.engine.resource.Status;
import com.availaboard.engine.resource.User;
import com.availaboard.engine.sql_connection.AvailaboardSQLConnection;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.router.RouteParameters;
import com.vaadin.flow.router.RouterLink;
import com.google.common.reflect.TypeToken;
import java.lang.reflect.Type;

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

	private Label statusLabel(Resource res) {
		Label label = new Label();
		label.setText(res.getStatus().toString());
		String labelClassName = res.getStatus() == Status.AVAILABLE ? "label-available" : "label-busy";
		label.addClassName(labelClassName);
		return label;
	}

	private RouterLink resourceLink(Resource res) {
		RouterLink link = new RouterLink(res.getName(), ResourcePage.class,
				new RouteParameters("resourceID", String.valueOf(res.getId())));
		return link;
	}

	private ComponentRenderer<ResourceInformationLoader, E> resourceDetailRenderer() {
	    return new ComponentRenderer(ResourceInformationLoader::new,
	    		ResourceInformationLoader::setResources);
	}
}


