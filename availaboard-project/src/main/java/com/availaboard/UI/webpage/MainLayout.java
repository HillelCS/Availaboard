package com.availaboard.UI.webpage;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Stream;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AssignableTypeFilter;

import com.availaboard.UI.ViewAuthorization;
import com.availaboard.engine.resource.Permission;
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
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouteConfiguration;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.flow.router.RouterLink;

/**
 * The main layout. Contains the navigation menu.
 */
@CssImport("./styles/webpage-styles/main-layout.css")
public class MainLayout extends AppLayout implements RouterLayout {

	/**
	 *
	 */
	private static final long serialVersionUID = -1788874174184456733L;
	private final Button logoutButton;
	private final RouterLink availaboardButton;
	private final RouterLink loginButton;
	private final RouterLink createAccountButton;
	private final AccessControl accessControl = AccessControlFactory.getInstance().createAccessControl();

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

		logoutButton = createMenuButton("Logout");
		logoutButton.addClickListener(e -> logout());
		logoutButton.getElement().setAttribute("title", "Logout (Ctrl+L)");

		availaboardButton = createMenuLink(AvailaboardView.class, "Availability");
		loginButton = createMenuLink(LoginView.class, "Login");
		createAccountButton = createMenuLink(CreateNewAccountView.class, "Create New Account");

	}

	private Button createMenuButton(String caption) {
		final Button routerButton = new Button(caption);
		routerButton.setClassName("menu-link");
		routerButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
		return routerButton;
	}

	private RouterLink createMenuLink(final Class<? extends Component> viewClass, final String caption) {
		final RouterLink routerLink = new RouterLink(caption, viewClass);
		routerLink.setClassName("menu-link");
		return routerLink;
	}

	/**
	 * Get's every class that implements the {@link ViewAuthorization} interface and
	 * has a {@link ViewAuthorization#getRequiredPermission()} property that matches
	 * the {@link CurrentUser}'s {@link Permission} property. It it creates a
	 * {@link RouterLink} for each of those classes and add's them to an
	 * {@link ArrayList}.
	 *
	 * @return A {@link RouterLink} for each <code>Class</code> that contains the
	 *         {@link CurrentUser}'s {@link Permission} in it's
	 *         {@link ViewAuthorization getRequiredPermission()}.
	 */
	@SuppressWarnings("unchecked")
	private Stream<RouterLink> getAllAuthorizedViews() {
		ArrayList<RouterLink> arr = new ArrayList<>();
		ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
		provider.addIncludeFilter(new AssignableTypeFilter(ViewAuthorization.class));

		Set<BeanDefinition> components = provider.findCandidateComponents("com/availaboard/UI/webpage");
		for (BeanDefinition component : components) {
			try {
				ViewAuthorization auth = (ViewAuthorization) Class.forName(component.getBeanClassName())
						.getDeclaredConstructor().newInstance();

				if (accessControl.isUserInRole(auth.getRequiredPermission())) {
					arr.add(createMenuLink((Class<? extends Component>) Class.forName(component.getBeanClassName()),
							auth.getViewName()));
					registerView(accessControl, auth.getRequiredPermission(),
							(Class<? extends Component>) Class.forName(component.getBeanClassName()));
				}

			} catch (ClassNotFoundException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | SecurityException | InstantiationException
					| NoSuchMethodException e) {
				e.printStackTrace();
			}
		}
		return arr.stream();
	}

	private void logout() {
		AccessControlFactory.getInstance().createAccessControl().signOut();
	}

	@Override
	protected void onAttach(AttachEvent attachEvent) {
		final VerticalLayout verticalLayout = new VerticalLayout();
		super.onAttach(attachEvent);
		verticalLayout.add(availaboardButton);

		if (accessControl.isUserSignedIn()) {
			getAllAuthorizedViews().forEach(routerLink -> {
				verticalLayout.add(routerLink);
			});

			verticalLayout.add(logoutButton);
			attachEvent.getUI().addShortcutListener(() -> logout(), Key.KEY_L, KeyModifier.CONTROL);

		} else {
			verticalLayout.add(loginButton);
			verticalLayout.add(createAccountButton);
		}

		verticalLayout.setSizeFull();
		verticalLayout.setAlignItems(Alignment.START);
		addToDrawer(verticalLayout);
	}

	/**
	 * Registers a View so Users can access it.
	 *
	 * @param accessControl The current accessControl
	 * @param permissions   The permissions required to access that View
	 * @param cl            The view being registered
	 */
	@SuppressWarnings("unchecked")
	private void registerView(AccessControl accessControl, Stream<Permission> permissions,
			Class<? extends Component> cl) {
		if (accessControl.isUserInRole(permissions) && !RouteConfiguration.forSessionScope().isRouteRegistered(cl)) {
			try {
				ViewAuthorization auth = (ViewAuthorization) cl.getDeclaredConstructor().newInstance();
				RouteConfiguration.forSessionScope().setRoute(auth.getViewName(), cl, MainLayout.class);
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
			}
		}
	}
}