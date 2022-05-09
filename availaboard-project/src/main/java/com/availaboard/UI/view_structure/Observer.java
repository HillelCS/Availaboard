package com.availaboard.UI.view_structure;

import com.availaboard.engine.resource.Resource;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.BeforeLeaveEvent;
import com.vaadin.flow.router.BeforeLeaveObserver;

public interface Observer extends BeforeLeaveObserver, ViewConfiguration {
    void update();

    void register(Subject subject);

    void unregister(Subject subject);

    Subject getSubject();

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
