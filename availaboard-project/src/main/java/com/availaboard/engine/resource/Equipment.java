package com.availaboard.engine.resource;

public class Equipment extends Resource {

	@ResourceFieldLoader("Model")
	private String model;
	@ResourceFieldLoader("Equipment Type")
	private String equipment_type;
	@ResourceFieldLoader("Contact")
	private String contact;

	private int equipment_type_ID;


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
