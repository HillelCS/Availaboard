package com.availaboard.engine.resource;

import java.util.HashMap;

public class Room extends Resource {
	
	@FieldExcludedFromDatabase
	private static HashMap<String, String> fieldList = new HashMap<String, String>();

	static {
		fieldList.put("contact", "Contact");
		fieldList.put("location", "Location");
	}

	@Override
	public String getFieldName(String fieldName) {
		return fieldList.get(fieldName);
	}


	
	@ResourceFieldLoader
	private String contact;
	@ResourceFieldLoader
	private String location;

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	@Override
	public String toString() {
		return "Room";
	}
}
