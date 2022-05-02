package com.availaboard.engine.resource;

/**
 * An Object that all Object types being loaded into the Availaboard Grid must
 * extend.
 */

public class Resource {

    private String name;
    private Status status;
    private int id;

    /**
     * Sole constructor. (For invocation by subclass constructors, typically
     * implicit.)
     */
    protected Resource() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}