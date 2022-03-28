package com.availaboard.UI.frontend_functionality;

import java.lang.reflect.Field;
import java.util.ArrayList;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.textfield.TextField;

public class ResourceInformationLoader extends FormLayout {
	ArrayList<TextField> textFieldList = new ArrayList<TextField>();
	private static final long serialVersionUID = -4590209736315740168L;
	AvailaboardSQLConnection db = new AvailaboardSQLConnection();
	Grid grid = new Grid();
	Class type;

	public ResourceInformationLoader(Class type) {
		this.type = type;
		for (Field field : type.getDeclaredFields()) {
			if (field.isAnnotationPresent(ResourceFieldLoader.class)) {
				textFieldList.add(new TextField(field.getName()));
			}
		}
		textFieldList.stream().forEach(field -> {
			field.setReadOnly(true);
			add(field);
		});

		add(grid);
	}

	public void setResources(Class resource) {
		textFieldList.stream().forEach(field -> {
			for (Field field1 : type.getDeclaredFields()) {
				if (field1.isAnnotationPresent(ResourceFieldLoader.class)) {
					if(field1.getName().equals(field.getLabel())) {
						try {
							field.setValue(field1.get(resource).toString());
						} catch (IllegalArgumentException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		});
			
		
	}
}
