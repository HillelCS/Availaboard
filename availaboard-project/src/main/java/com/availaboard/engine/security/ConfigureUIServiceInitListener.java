package com.availaboard.engine.security;

import com.availaboard.UI.webpage.LoginView;
import com.availaboard.UI.webpage.admin.AdminView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;
import org.springframework.stereotype.Component;
import com.availaboard.UI.webpage.admin.*;

@Component
public class ConfigureUIServiceInitListener implements VaadinServiceInitListener {

	private AccessControl accessControl = AccessControlFactory.getInstance().createAccessControl();

	@Override
	public void serviceInit(ServiceInitEvent event) {
		event.getSource().addUIInitListener(uiEvent -> {
			final UI ui = uiEvent.getUI();
			ui.addBeforeEnterListener(this::beforeEnter);
		});
	}

	/**
	 * Reroutes the user if (s)he is not authorized to access the view.
	 *
	 * @param event before navigation event with event details
	 */
	private void beforeEnter(BeforeEnterEvent event) {
		if (AbstractAdminView.class.isAssignableFrom(event.getNavigationTarget()) && !accessControl.isUserSignedIn()) {
			event.rerouteTo(LoginView.class);
		}
	}
}