package com.availaboard.UI.view_structure;

import com.availaboard.UI.webpage.MainLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;

public interface ViewConfiguration extends ViewType, BeforeEnterObserver {
    /**
     * Add all the components to the view.
     */
    void addAll();

    /**
     * Calls the addAll() method before the page loads up. Also
     * set's the route as the getViewName() implementation and the
     * ParentLayout as the {@link MainLayout}.
     *
     * @param event before navigation event with event details
     */
    @Override
    default void beforeEnter(final BeforeEnterEvent event) {
        addAll();
    }

}
