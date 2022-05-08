package com.availaboard.UI.view_pattern;

import com.vaadin.flow.component.Component;

import java.lang.reflect.InvocationTargetException;

/**
 * A Factory class used to create {@link ViewType}, {@link ViewConfiguration}, and
 * {@link ViewAuthorization}.
 */
public class ViewFactory {
    public static ViewType createViewTypeInstance(final Class<? extends Component> cl) {
        try {
            return (ViewType) cl.getConstructor().newInstance();
        } catch (final InvocationTargetException | InstantiationException | IllegalAccessException |
                       NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}

