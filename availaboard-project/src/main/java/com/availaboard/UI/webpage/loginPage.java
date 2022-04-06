package com.availaboard.UI.webpage;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;

@PageTitle("Login")
@Route("/login")
@Theme(value = Lumo.class, variant = Lumo.DARK)
public class loginPage extends UI {

}
