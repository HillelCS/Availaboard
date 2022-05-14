package com.availaboard.engine.resource;

/**
 * A factory used to create {@link Resource}'s.
 */
public class ResourceFactory {
    public static Resource createResource(String name) {
        if(name.equals("Equipment")) {
            return new Equipment();
        } else  if(name.equals("Room")) {
            return new Room();
        } else  if(name.equals("User")) {
            return new User();
        } else {
            return new Resource();
        }
    }
}
