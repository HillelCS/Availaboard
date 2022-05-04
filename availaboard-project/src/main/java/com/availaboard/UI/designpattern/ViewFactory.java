package com.availaboard.UI.designpattern;

import javax.annotation.Nullable;
import java.lang.reflect.InvocationTargetException;

public class ViewFactory {

    public static final ViewConfiguration createViewConfigInstance(Class<? extends ViewConfiguration> cl) {
        try {
            ViewConfiguration viewConfig = cl.getConstructor().newInstance();
            return viewConfig;
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    @Nullable
    public static final ViewAuthorization createViewAuthInstance(Class<? extends ViewAuthorization> cl) {
        try {
            ViewAuthorization viewAuth = cl.getConstructor().newInstance();
            return viewAuth;
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}

