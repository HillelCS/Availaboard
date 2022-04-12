package com.availaboard.engine.resource;

import java.util.HashMap;

public class Resource {

	private String name;
	private Status status;
	private int id;

	/*
	 * This HashMap has two Strings for the Key and Value. The Key is the field
	 * name, and the value is the fields nickname. The fields nicknames are
	 * statically created in each subclass. (Statically created so they don't get
	 * recreated each time the class is instantiated)
	 */

	@FieldExcludedFromDatabase
	private static HashMap<String, String> fieldList = new HashMap<String, String>();

	/*
	 * Takes in a String of a field name and returns the nickname of a field in each
	 * subclasses nickname list.
	 */

	public String getFieldName(String fieldName) {
		return fieldList.get(fieldName);
	}

	protected Resource() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}