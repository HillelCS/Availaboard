package com.availaboard.engine.resource;

import com.availaboard.engine.sql_connection.Column;
import com.availaboard.engine.sql_connection.Table;

@Table("ROOM")
public class Room extends Resource {

	@ResourceFieldLoader("Contact")
	@Column("CONTACT")
	private String contact;
	@ResourceFieldLoader("Location")
	@Column("LOCATION")
	private String location;

	public String getContact() {
		return contact;
	}

	public String getLocation() {
		return location;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public void setLocation(String location) {
		this.location = location;
	}
}
