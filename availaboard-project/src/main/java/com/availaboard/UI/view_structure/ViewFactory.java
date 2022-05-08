package com.availaboard.UI.view_structure;

import com.vaadin.flow.component.Component;

import java.lang.reflect.InvocationTargetException;

/**
 * A Factory class used to create {@link ViewType}, {@link ViewConfiguration}, and
 * {@link ViewAuthorization}.
 */
public class ViewFactory {
    /**
     * A useful Factory method used to create a {@link ViewType} <code>Object</code> with a Class
     * that extends {@link Component}.
     * @param cl The Class that is being used to instantiate the {@link ViewType}.
     * @return A {@link ViewType} that is instantiated with a Class that extends {@link Component}.
     */
    public static ViewType createViewTypeInstance(final Class<? extends Component> cl) {
        try {
            return (ViewType) cl.getConstructor().newInstance();
        } catch (final InvocationTargetException | InstantiationException | IllegalAccessException |
                       NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}

