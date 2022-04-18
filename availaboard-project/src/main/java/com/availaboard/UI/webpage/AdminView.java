package com.availaboard.UI.webpage;

import javax.annotation.security.PermitAll;

import org.springframework.security.access.annotation.Secured;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.router.Route;

@PermitAll
@Route("admin")
public class AdminView extends UI {
	public AdminView() {
		System.out.println("hello world!");
	}
}
