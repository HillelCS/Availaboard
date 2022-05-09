package com.availaboard.UI.application_structure.observable;

import com.availaboard.UI.application_structure.view_structure.ViewConfiguration;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeLeaveEvent;
import com.vaadin.flow.router.BeforeLeaveObserver;

/**
 * Used to show a class is an Observer. It extends {@link ViewConfiguration} because
 * many classes also use that interface which uses the BeforeEnterObserver interface
 * causing conflicts. So, you do <bold>not</bold> have to implement the {@link ViewConfiguration}
 * interface if you implement this interface.
 *
 */
public interface Observer extends BeforeLeaveObserver, ViewConfiguration {

    /**
     * A method called on all the Observers any time the
     * {@link Subject#notifiyObservers()} method is called.
     * This updates the View for every class that is currently
     * active.
     */
    void update();

    /**
     * Registers an Observer in a {@link Subject}.
     * @param subject The {@link Subject} that's getting an Observer added.
     */
    void register(Subject subject);
    /**
     * Unregisters an Observer from a {@link Subject}.
     * @param subject The {@link Subject} that's removing an Observer.
     */
    void unregister(Subject subject);

    /**
     * Get's the {@link Subject} that the Observer implementation is communicating with.
     * @return The {@link Subject} that the Observer implementation is communicating with.
     */
    Subject getSubject();

    /**
     * Registers the current View as an Observer to the Subject and
     * initializes the view.
     * @param event before navigation event with event details
     */
    @Override
    default void beforeEnter(final BeforeEnterEvent event) {
        register(getSubject());
        initialize();
    }

    @Override
    default void beforeLeave(final BeforeLeaveEvent event) {
        unregister(getSubject());
    }
}
