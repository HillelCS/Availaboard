package com.availaboard.engine.resource;

public class Equipment extends Resource {

	@ResourceFieldLoader("Model")
	private String model;
	@ResourceFieldLoader("Equipment Type")
	private String equipment_type;
	@ResourceFieldLoader("Contact")
	private String contact;

	public String getContact() {
		return contact;
	}

	public String getEquipment_type() {
		return equipment_type;
	}

	public String getModel() {
		return model;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public void setEquipment_type(String equipment_type) {
		this.equipment_type = equipment_type;
	}

	public void setModel(String model) {
		this.model = model;
	}

	@Override
	public String toString() {
		return "Equipment";
	}

}
