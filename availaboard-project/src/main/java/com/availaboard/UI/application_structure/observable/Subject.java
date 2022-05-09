package com.availaboard.UI.application_structure.observable;

/**
 * Indicates the class is a Subject. It is used to communicate
 * with {@link Observer}'s.
 */
public interface Subject {

    /**
     * Adds an {@link Observer} to the Subject.
     * @param observer {@link Observer} being added to the Subject.
     */
    void addObserver(Observer observer);
    /**
     * Removes an {@link Observer} from the Subject.
     * @param observer {@link Observer} being removed from the Subject.
     */
    void removeObserver(Observer observer);

    /**
     * Calls the update() method on all the {@link Observer}'s.
     */
    void notifiyObservers();

}
