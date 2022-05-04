package com.availaboard.UI.webpage;

import com.availaboard.UI.designpattern.ViewConfiguration;
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
@Route(value = "login", layout = MainLayout.class)
@PageTitle("Login")
public class LoginView extends FlexLayout implements ViewConfiguration {

    /**
     *
     */
    private static final long serialVersionUID = -6633815459114206330L;
    private final AccessControl accessControl;
    private final LoginForm loginForm = new LoginForm();
    private final FlexLayout centeringLayout = new FlexLayout();

    public LoginView() {
        this.accessControl = AccessControlFactory.getInstance().createAccessControl();
        this.buildUI();
    }

    private void buildUI() {
        this.setSizeFull();
        this.loginForm.addLoginListener(this::login);
        this.centeringLayout.setSizeFull();
        this.centeringLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        this.centeringLayout.setAlignItems(Alignment.CENTER);
        this.centeringLayout.add(this.loginForm);
        this.add(this.centeringLayout);
    }

    private void login(final LoginForm.LoginEvent event) {
        if (this.accessControl.signIn(event.getUsername(), event.getPassword())) {
            if (this.accessControl.isUserInRole(Permission.Admin)) {
                this.getUI().get().navigate(AdminView.VIEWNAME);
            } else if (this.accessControl.isUserInRole(Permission.User)) {
                this.getUI().get().navigate(UserInformationView.VIEWNAME);
            } else {
                this.getUI().get().navigate("/");
            }
        } else {
            event.getSource().setError(true);
        }
    }

    @Override
    public void addAll() {

    }
}