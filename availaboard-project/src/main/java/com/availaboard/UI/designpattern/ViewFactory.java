package com.availaboard.UI.designpattern;

import com.availaboard.UI.webpage.user.UserInformationView;

import javax.annotation.Nullable;
import java.lang.reflect.InvocationTargetException;

public class ViewFactory {

    public static final ViewConfiguration createViewConfigInstance(Class<UserInformationView> userInformationViewClass) {
        try {
            ViewConfiguration viewConfig = (ViewConfiguration) cl.getConstructor().newInstance();
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
        return null;
    }
    @Nullable
    public static final ViewAuthorization createViewAuthInstance(Class<? extends Class> aClass) {
        try {
            ViewAuthorization viewAuth = (ViewAuthorization) cl.getConstructor().newInstance();
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
        return null;
    }
}

