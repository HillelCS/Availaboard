package com.availaboard.engine.resource;

public class Room extends Resource {

	@ResourceFieldLoader("Contact")
	private String contact;
	@ResourceFieldLoader("Location")
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

	@Override
	public String toString() {
		return "Room";
	}
}
