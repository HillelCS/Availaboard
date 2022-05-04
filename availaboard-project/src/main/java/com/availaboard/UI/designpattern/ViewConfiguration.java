package com.availaboard.UI.designpattern;

import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;

public interface ViewConfiguration extends ViewType, BeforeEnterObserver {
    /**
     * Add all the components to the view.
     */
    void addAll();

    /**
     * Calls the addAll() method before the page loads up.
     *
     * @param event before navigation event with event details
     */
    @Override
    default void beforeEnter(BeforeEnterEvent event) {
        addAll();
    }
}
