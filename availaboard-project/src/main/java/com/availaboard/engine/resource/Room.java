package com.availaboard.engine.resource;

import com.availaboard.engine.sql_connection.Column;
import com.availaboard.engine.sql_connection.Table;

@Table("room")
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

    public void setContact(final String contact) {
        this.contact = contact;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(final String location) {
        this.location = location;
    }
}
