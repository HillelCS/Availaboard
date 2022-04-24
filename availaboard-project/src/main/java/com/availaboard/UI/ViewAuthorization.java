package com.availaboard.UI;

import java.lang.reflect.InvocationTargetException;
import java.util.stream.Stream;

import com.availaboard.UI.webpage.MainLayout;
import com.availaboard.UI.webpage.admin.AdminView;
import com.availaboard.engine.resource.Permission;
import com.availaboard.engine.security.AccessControl;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.router.RouteConfiguration;

/**
 * Used to tell if the current user has to have some {@link Permission} to
 * access the View. Implementations of it specify which views must have what
 * permissions.
 */
public interface ViewAuthorization {
	Stream<Permission> getRequiredPermission();
	String getViewName();
}
