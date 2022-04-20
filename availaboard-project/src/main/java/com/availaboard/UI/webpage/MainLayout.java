package com.availaboard.UI.webpage;

import com.availaboard.UI.webpage.admin.AdminView;
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
@CssImport(value = "./styles/webpage-styles/menu-buttons.css", themeFor = "vaadin-button")
public class MainLayout extends AppLayout implements RouterLayout {

	private final Button logoutButton;
	private final RouterLink availaboardButton;
	private final RouterLink loginButton;

	public MainLayout() {
		final DrawerToggle drawerToggle = new DrawerToggle();
		drawerToggle.addClassName("menu-toggle");
		addToNavbar(drawerToggle);
		final HorizontalLayout top = new HorizontalLayout();
		top.setDefaultVerticalComponentAlignment(Alignment.CENTER);
		top.setClassName("menu-header");

		final Label title = new Label("Availaboard");
		top.add(title);
		top.add(title);
		addToNavbar(top);

		logoutButton = createMenuButton("Logout", VaadinIcon.SIGN_OUT.create());
		logoutButton.addClickListener(e -> logout());
		logoutButton.getElement().setAttribute("title", "Logout (Ctrl+L)");

		availaboardButton = createMenuLink(AvailaboardView.class, "Availaboard Grids");
		loginButton = createMenuLink(LoginView.class, "Login");

	}

	private void logout() {
		AccessControlFactory.getInstance().createAccessControl().signOut();
	}

	private RouterLink createMenuLink(Class<? extends Component> viewClass, String caption) {
		final RouterLink routerLink = new RouterLink(null, viewClass);
		routerLink.setClassName("menu-link");
		routerLink.add(new Span(caption));
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
		if (accessControl.isUserAdmin(AccessControl.ADMIN_ROLE_NAME)
				&& !RouteConfiguration.forSessionScope().isRouteRegistered(AdminView.class)) {
			RouteConfiguration.forSessionScope().setRoute(AdminView.VIEW_NAME, AdminView.class, MainLayout.class);
		}
	}

	@Override
	protected void onAttach(AttachEvent attachEvent) {
		VerticalLayout verticalLayout = new VerticalLayout();
		super.onAttach(attachEvent);
		verticalLayout.add(loginButton);
		verticalLayout.add(availaboardButton);
		final AccessControl accessControl = AccessControlFactory.getInstance().createAccessControl();
		if (accessControl.isUserSignedIn()) {
			registerAdminViewIfApplicable(accessControl);
			attachEvent.getUI().addShortcutListener(() -> logout(), Key.KEY_L, KeyModifier.CONTROL);
			verticalLayout.add(createMenuLink(AdminView.class, AdminView.VIEW_NAME));
			verticalLayout.add(logoutButton);
		}

		verticalLayout.setSizeFull();
		verticalLayout.setAlignItems(Alignment.START);
		addToDrawer(verticalLayout);
	}

}