package com.availaboard.UI.webpage;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;

@Route("/create-account")
@Theme(value = Lumo.class, variant = Lumo.DARK)
@CssImport("./styles/webpage-styles/create-account-view-styles.css")
public class CreateNewAccountView extends VerticalLayout {

	
	
	public CreateNewAccountView() {
		
	}
	
	
	
}
