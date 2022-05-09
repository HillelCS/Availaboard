package com.availaboard.UI.webpage;

import com.availaboard.UI.application_structure.view_structure.ViewConfiguration;
import com.availaboard.UI.webpage.admin.AdminView;
import com.availaboard.UI.webpage.user.UserInformationView;
import com.availaboard.engine.resource.Permission;
import com.availaboard.engine.security.AccessControl;
import com.availaboard.engine.security.AccessControlFactory;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

/**
 * UI content when the user is not logged in yet.
 */
@Route(value = LoginView.VIEWNAME, layout = MainLayout.class)
@PageTitle("Login")
public class LoginView extends FlexLayout implements ViewConfiguration {

    /**
     *
     */

    protected static final String VIEWNAME = "login";
    private static final long serialVersionUID = -6633815459114206330L;
    private AccessControl accessControl;
    private final LoginForm loginForm = new LoginForm();
    private final FlexLayout centeringLayout = new FlexLayout();

    private void buildUI() {
        setSizeFull();
        loginForm.addLoginListener(this::login);
        centeringLayout.setSizeFull();
        centeringLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        centeringLayout.setAlignItems(Alignment.CENTER);
        centeringLayout.add(loginForm);
    }

    private void login(final LoginForm.LoginEvent event) {
        if (accessControl.signIn(event.getUsername(), event.getPassword())) {
            if (accessControl.isUserInRole(Permission.Admin)) {
                getUI().get().navigate(AdminView.class);
            } else if (accessControl.isUserInRole(Permission.User)) {
                getUI().get().navigate(UserInformationView.class);
            } else {
                getUI().get().navigate("/");
            }
        } else {
            event.getSource().setError(true);
        }
    }

    @Override
    public void initialize() {
        accessControl = AccessControlFactory.getInstance().createAccessControl();
        buildUI();
        add(centeringLayout);
    }

    @Override
    public String viewName() {
        return null;
    }

}