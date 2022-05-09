package com.availaboard.UI.view_structure;

import com.availaboard.UI.webpage.MainLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;

public interface ViewConfiguration extends ViewType, BeforeEnterObserver {
    /**
     * Use this method to set up the View. <bold>DO NOT USE THE CONSTRUCTOR</bold>
     */
    void initialize();

    /**
     * Calls the addAll() method before the page loads up. Also
     * set's the route as the getViewName() implementation and the
     * ParentLayout as the {@link MainLayout}.
     *
     * @param event before navigation event with event details
     */
    @Override
    default void beforeEnter(final BeforeEnterEvent event) {
        initialize();
    }

}
