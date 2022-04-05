package com.availaboard.engine.resource;

import java.util.HashMap;

public class Equipment extends Resource {

	@ResourceFieldLoader
	private String model;
	@ResourceFieldLoader
	private String equipment_type;
	@ResourceFieldLoader
	private String contact;

	private int equipment_type_ID;

	@FieldExcludedFromDatabase
	private static HashMap<String, String> fieldList = new HashMap<String, String>();

	static {
		fieldList.put("model", "Model");
		fieldList.put("contact", "Contact");
	}

	@Override
	public String getFieldName(String fieldName) {
		return fieldList.get(fieldName);
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getEquipment_type() {
		return equipment_type;
	}

	public void setEquipment_type(String equipment_type) {
		this.equipment_type = equipment_type;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public int getEquipment_type_ID() {
		return equipment_type_ID;
	}

	public void setEquipment_type_ID(int equipment_type_ID) {
		this.equipment_type_ID = equipment_type_ID;
	}

	@Override
	public String toString() {
		return "Equipment";
	}

}
