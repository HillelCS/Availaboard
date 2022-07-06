package com.availaboard.engine.resource;

import com.availaboard.engine.sql_connection.Column;
import com.availaboard.engine.sql_connection.Table;

@Table("equipment")
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

    public void setContact(final String contact) {
        this.contact = contact;
    }

    public String getEquipmentType() {
        return equipmentType;
    }

    public String getModel() {
        return model;
    }

    public void setModel(final String model) {
        this.model = model;
    }

    public void setEquipment_type(final String equipment_type) {
        equipmentType = equipment_type;
    }
}
