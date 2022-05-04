package com.availaboard.UI.view_pattern;

import com.vaadin.flow.component.Component;

import javax.annotation.Nullable;
import java.lang.reflect.InvocationTargetException;

/**
 * A Factory class used to create {@link ViewType}, {@link ViewConfiguration}, and
 * {@link ViewAuthorization}.
 */
public class ViewFactory {

    public static final String getViewName(final Class<? extends Component> cl) {
        try {
            final ViewType viewType = (ViewType) cl.getConstructor().newInstance();
            return viewType.viewName();
        } catch (final InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (final InstantiationException e) {
            throw new RuntimeException(e);
        } catch (final IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (final NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    @Nullable
    public static final ViewConfiguration createViewConfigInstance(final Class<? extends ViewConfiguration> cl) {
        try {
            final ViewConfiguration viewConfig = cl.getConstructor().newInstance();
            return viewConfig;
        } catch (final InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (final InstantiationException e) {
            throw new RuntimeException(e);
        } catch (final IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (final NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    @Nullable
    public static final ViewAuthorization createViewAuthInstance(final Class<? extends ViewAuthorization> cl) {
        try {
            final ViewAuthorization viewAuth = cl.getConstructor().newInstance();
            return viewAuth;
        } catch (final InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (final InstantiationException e) {
            throw new RuntimeException(e);
        } catch (final IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (final NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    @Nullable
    public static final ViewType createViewTypeInstance(final Class<? extends ViewType> cl) {
        try {
            final ViewType viewType = cl.getConstructor().newInstance();
            return viewType;
        } catch (final InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (final InstantiationException e) {
            throw new RuntimeException(e);
        } catch (final IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (final NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}

