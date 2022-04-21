package com.availaboard.engine.security;

import org.springframework.stereotype.Component;

import com.availaboard.UI.webpage.LoginView;
import com.availaboard.UI.webpage.admin.AbstractAdminView;
import com.availaboard.engine.resource.Permission;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;

@Component
public class ConfigureUIServiceInitListener implements VaadinServiceInitListener {

	/**
	 *
	 */
	private static final long serialVersionUID = 6272827526606103857L;
	private AccessControl accessControl = AccessControlFactory.getInstance().createAccessControl();

	/**
	 * Reroutes the user if (s)he is not authorized to access the view.
	 *
	 * @param event before navigation event with event details
	 */
	private void beforeEnter(BeforeEnterEvent event) {
		if (AbstractAdminView.class.isAssignableFrom(event.getNavigationTarget()) && !accessControl.isUserSignedIn() && !accessControl.isUserInRole(Permission.Admin)) {
			event.rerouteTo(LoginView.class);
		}
	}

	@Override
	public void serviceInit(ServiceInitEvent event) {
		event.getSource().addUIInitListener(uiEvent -> {
			final UI ui = uiEvent.getUI();
			ui.addBeforeEnterListener(this::beforeEnter);
		});
	}
}