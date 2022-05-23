package com.availaboard.engine.resource;

/**
 * A factory used to create {@link Resource}'s.
 */
public class ResourceFactory {
    public static Resource createResource(String name) {
        if(name.equals(Equipment.class.getSimpleName())) {
            return new Equipment();
        } else  if(name.equals(Room.class.getSimpleName())) {
            return new Room();
        } else  if(name.equals(User.class.getSimpleName())) {
            return new User();
        } else {
            return null;
        }
    }
}
