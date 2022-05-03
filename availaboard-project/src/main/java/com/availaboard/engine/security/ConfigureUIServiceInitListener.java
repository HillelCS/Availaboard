package com.availaboard.engine.security;

import com.availaboard.UI.designpattern.ViewAuthorization;
import com.availaboard.UI.webpage.LoginView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;

@Component
public class ConfigureUIServiceInitListener implements VaadinServiceInitListener {

    /**
     *
     */
    private static final long serialVersionUID = 6272827526606103857L;

    private final AccessControl accessControl = AccessControlFactory.getInstance().createAccessControl();

    /**
     * Reroutes the user if (s)he is not authorized to access the view.
     *
     * @param event before navigation event with event details
     */
    private void beforeEnter(BeforeEnterEvent event) {
        if (!isUserAuthorized(event.getNavigationTarget())) {
            event.rerouteTo(LoginView.class);
        }
    }

    /**
     * Checks if a {@link User} is authorized to view the page they are trying to
     * access. It does this by first checking if the user is signed in and the page
     * they are attempting to go to implements the {@link ViewAuthorization}
     * interface. If so, it then checks if the implementation of the method
     * {@link ViewAuthorization#getRequiredPermission()} is equal to the
     * {@link CurrentUser}'s {@link Permission}. If that sequence of events succeeds
     * the method returns <code>True</code>.
     *
     * @param target The view the {@link User} is trying to access.
     * @return <code>True</code> if the {@link CurrentUser} has sufficient
     * permissions to access the page they are trying to go to.
     * <code>False</code> if otherwise.
     */
    private boolean isUserAuthorized(Class<?> target) {
        if (!ViewAuthorization.class.isAssignableFrom(target)) {
            return true;
        }
        if (accessControl.isUserSignedIn()) {
            try {
                ViewAuthorization auth = (ViewAuthorization) target.getDeclaredConstructor().newInstance();
                if (accessControl.isUserInRole(auth.getRequiredPermission())) {
                    return true;
                }
            } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					 | InvocationTargetException | NoSuchMethodException | SecurityException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public void serviceInit(ServiceInitEvent event) {
        // Add's a reroute listener to every page in the service initialization
        event.getSource().addUIInitListener(uiEvent -> {
            final UI ui = uiEvent.getUI();
            ui.addBeforeEnterListener(this::beforeEnter);
        });
    }
}