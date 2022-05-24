package com.availaboard.UI.application_structure.view_structure;

import com.availaboard.UI.application_structure.observable.Observer;
import com.availaboard.UI.application_structure.observable.Subject;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeLeaveEvent;
import com.vaadin.flow.router.BeforeLeaveObserver;

import java.util.Optional;

/**
 * Used for Views that want to become an Observer. It extends {@link ViewConfiguration} because
 *  * many classes also use that interface which uses the BeforeEnterObserver interface
 *  * causing conflicts. So, you do <bold>not</bold> have to implement the {@link ViewConfiguration}
 *  * interface if you implement this interface.
 */
public interface ViewObserver extends Observer, BeforeLeaveObserver, ViewConfiguration  {

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

    Optional<UI> getUI();
}
