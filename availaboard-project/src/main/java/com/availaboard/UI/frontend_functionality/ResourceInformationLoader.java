package com.availaboard.UI.frontend_functionality;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Stream;

import com.availaboard.engine.resource.Resource;
import com.availaboard.engine.sql_connection.AvailaboardSQLConnection;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.function.ValueProvider;
import com.google.common.reflect.TypeToken;
import java.lang.reflect.Type;

public class ResourceInformationLoader<E extends Resource> extends FormLayout {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4590209736315740168L;
	AvailaboardSQLConnection db = new AvailaboardSQLConnection();
	Grid<E> grid = new Grid<E>();

	private Class<E> type;

	public ResourceInformationLoader(Class<E> type) {
		this.type = type;
		for (Field field : type.getDeclaredFields()) {
			if (field.isAnnotationPresent(ResourceFieldLoader.class)) {

				

			}

		}
		add(grid);
	}
}

//grid.addColumn(Resource -> (Resource).getContact()).setHeader("Contact").setWidth("33.333%").setFlexGrow(0)
