package com.availaboard.UI.frontend_functionality;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import com.availaboard.engine.resource.Resource;
import com.availaboard.engine.resource.ResourceFieldLoader;
import com.vaadin.flow.component.formlayout.FormLayout;

public class ResourceInformationLoader extends FormLayout {
	private static final long serialVersionUID = -4590209736315740168L;

	
	/*
	 * Pass in the class for the fields you want to get. 
	 * The method will return a Map with the field names as a 
	 * String and the fields values as a object. To specify which fields
	 * should be fetched from the class being passed in use the 
	 * @ResourceFieldLoader annotation before the fields.
	 */
	public <E> Map<String, Object> getResourceFields(Class<?> resource) {
		Map<String, Object> map = new HashMap<String, Object>();
		Stream<Field> stream = Arrays.stream(resource.getDeclaredFields());

		stream.filter(field -> field.isAnnotationPresent(ResourceFieldLoader.class)).forEach(field -> {
			try {
				map.put(field.getName(), field.get(resource));
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}

		});

		return map;

	}
}
