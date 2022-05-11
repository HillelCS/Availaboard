package com.availaboard.UI.application_structure.observable;

import com.availaboard.UI.application_structure.view_structure.ViewAuthorization;
import com.availaboard.UI.application_structure.view_structure.ViewConfiguration;
import com.availaboard.UI.application_structure.view_structure.ViewType;
import com.vaadin.flow.component.Component;

import java.lang.reflect.InvocationTargetException;

/**
 * A Factory class used to create {@link ViewType}, {@link ViewConfiguration}, and
 * {@link ViewAuthorization}.
 */
public class ViewFactory {

    private static final Subject subject = new ViewController();

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

    /**
     * Creates an implementation of the {@link Subject} interface with a
     * {@link ViewController}. This is created statically so all the Views
     * can communicate with the same {@link Subject}.
     * @return A static {@link Subject} concretely created with a {@link ViewController}.
     */
    public static Subject getViewControllerInstance() {
        return subject;
    }
}

