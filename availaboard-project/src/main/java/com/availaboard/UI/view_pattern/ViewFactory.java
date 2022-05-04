package com.availaboard.UI.view_pattern;

import com.vaadin.flow.component.Component;

import java.lang.reflect.InvocationTargetException;

/**
 * A Factory class used to create {@link ViewType}, {@link ViewConfiguration}, and
 * {@link ViewAuthorization}.
 */
public class ViewFactory {

    public static String getViewName(final Class<? extends Component> cl) {
        try {
            final ViewType viewType = (ViewType) cl.getConstructor().newInstance();
            return viewType.viewName();
        } catch (final InvocationTargetException | InstantiationException | IllegalAccessException |
                       NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }


    public static ViewConfiguration createViewConfigInstance(final Class<? extends ViewConfiguration> cl) {
        try {
            return cl.getConstructor().newInstance();
        } catch (final InvocationTargetException | InstantiationException | IllegalAccessException |
                       NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }


    public static ViewAuthorization createViewAuthInstance(final Class<? extends ViewAuthorization> cl) {
        try {
            return cl.getConstructor().newInstance();
        } catch (final InvocationTargetException | InstantiationException | IllegalAccessException |
                       NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }


    public static ViewType createViewTypeInstance(final Class<? extends ViewType> cl) {
        try {
            return cl.getConstructor().newInstance();
        } catch (final InvocationTargetException | InstantiationException | IllegalAccessException |
                       NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}

