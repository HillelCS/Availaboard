package com.availaboard.engine.resource;

import com.availaboard.engine.sql_connection.Column;
import com.availaboard.engine.sql_connection.Table;

@Table("Equipment")
public class Equipment extends Resource {

	@ResourceFieldLoader("Model")
	@Column("MODEL")
	private String model;
	@Column("EQUIPMENT_TYPE")
	@ResourceFieldLoader("Equipment Type")
	private String equipmentType;
	@ResourceFieldLoader("Contact")
	@Column("CONTACT")
	private String contact;

	public String getContact() {
		return contact;
	}

	public String getEquipmentType() {
		return equipmentType;
	}

	public String getModel() {
		return model;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public void setEquipment_type(String equipment_type) {
		this.equipmentType = equipment_type;
	}

	public void setModel(String model) {
		this.model = model;
	}
}
