package com.availaboard.UI.webpage;

import com.availaboard.engine.security.AccessControl;
import com.availaboard.engine.security.AccessControlFactory;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyModifier;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouteConfiguration;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.VaadinService;

/**
 * The main layout. Contains the navigation menu.
 */
@CssImport("./styles/webpage-styles/main-layout.css")
public class MainLayout extends AppLayout implements RouterLayout {

	private final Button logoutButton;
	private final RouterLink availaboardButton;
	private final RouterLink loginButton;

	public MainLayout() {

		// Header of the menu (the navbar)

		// menu toggle
		final DrawerToggle drawerToggle = new DrawerToggle();
		drawerToggle.addClassName("menu-toggle");
		addToNavbar(drawerToggle);

		// image, logo
		final HorizontalLayout top = new HorizontalLayout();
		top.setDefaultVerticalComponentAlignment(Alignment.CENTER);
		top.setClassName("menu-header");

		final Label title = new Label("Availaboard");
		top.add(title);
		top.add(title);
		addToNavbar(top);

		// Create logout button but don't add it yet; admin view might be added
		// in between (see #onAttach())
		logoutButton = createMenuButton("Logout", VaadinIcon.SIGN_OUT.create());
		logoutButton.addClickListener(e -> logout());
		logoutButton.getElement().setAttribute("title", "Logout (Ctrl+L)");

		availaboardButton = createMenuLink(AvailaboardView.class, "Availaboard Grids", VaadinIcon.GRID.create());
		loginButton = createMenuLink(LoginView.class, "Login", VaadinIcon.ANGLE_UP.create());

	}

	private void logout() {
		AccessControlFactory.getInstance().createAccessControl().signOut();
	}

	private RouterLink createMenuLink(Class<? extends Component> viewClass, String caption, Icon icon) {
		final RouterLink routerLink = new RouterLink(null, viewClass);
		routerLink.setClassName("menu-link");
		routerLink.add(icon);
		routerLink.add(new Span(caption));
		icon.setSize("24px");
		return routerLink;
	}

	private Button createMenuButton(String caption, Icon icon) {
		final Button routerButton = new Button(caption);
		routerButton.setClassName("menu-link");
		routerButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
		routerButton.setIcon(icon);
		icon.setSize("24px");
		return routerButton;
	}

	private void registerAdminViewIfApplicable(AccessControl accessControl) {
		// register the admin view dynamically only for any admin user logged in
		if (accessControl.isUserInRole(AccessControl.ADMIN_ROLE_NAME)
				&& !RouteConfiguration.forSessionScope().isRouteRegistered(AdminView.class)) {
			RouteConfiguration.forSessionScope().setRoute(AdminView.VIEW_NAME, AdminView.class, MainLayout.class);
			// as logout will purge the session route registry, no need to
			// unregister the view on logout
		}
	}

	@Override
	protected void onAttach(AttachEvent attachEvent) {
		VerticalLayout verticalLayout = new VerticalLayout();

		super.onAttach(attachEvent);

		// add the admin view menu item if user has admin role
		final AccessControl accessControl = AccessControlFactory.getInstance().createAccessControl();
		if (accessControl.isUserSignedIn()) {
			// Create extra navigation target for admins
			registerAdminViewIfApplicable(accessControl);
			// The link can only be created now, because the RouterLink checks
			// that the target is valid.
			attachEvent.getUI().addShortcutListener(() -> logout(), Key.KEY_L, KeyModifier.CONTROL);
			verticalLayout.add(createMenuLink(AdminView.class, AdminView.VIEW_NAME, VaadinIcon.DOCTOR.create()));
			verticalLayout.add(logoutButton);
		}

		// Finally, add logout button for all users
		verticalLayout.add(loginButton);
		verticalLayout.add(availaboardButton);
		verticalLayout.setSizeFull();
		verticalLayout.setAlignItems(Alignment.START);
		addToDrawer(verticalLayout);
	}

}