package com.availaboard.UI.view_pattern;

import javax.annotation.Nullable;
import java.lang.reflect.InvocationTargetException;

public class ViewFactory {

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
}

