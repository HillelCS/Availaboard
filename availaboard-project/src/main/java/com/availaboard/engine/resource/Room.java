package com.availaboard.engine.resource;

public class Room extends Resource {
	
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
