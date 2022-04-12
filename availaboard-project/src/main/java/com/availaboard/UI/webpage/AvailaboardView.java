package com.availaboard.UI.webpage;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Set;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AssignableTypeFilter;

import com.availaboard.UI.frontend_functionality.ResourceGrid;
import com.availaboard.engine.resource.Resource;
import com.availaboard.engine.sql_connection.AvailaboardSQLConnection;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;

@PageTitle("Availaboard")
@CssImport("./styles/webpage-styles/availaboard.css")
@Route("")
@Theme(value = Lumo.class, variant = Lumo.DARK)
public class AvailaboardView extends VerticalLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	VerticalLayout layout = new VerticalLayout();
	AvailaboardSQLConnection db = new AvailaboardSQLConnection();

	Button loginButton = new Button("Login");
	Button createAccountButton = new Button("Create an Account");

	/*
	 * Uses a stream to add all of the grids to the layout and centers them.
	 */
	public AvailaboardView() {
		getResourceGrids().stream().forEach(grid -> {
			layout.add(grid);
			layout.setHorizontalComponentAlignment(Alignment.CENTER, grid);
		});

		createAccountButton.addClassName("create-account-button");
		loginButton.addClassName("login-button");

		createAccountButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void onComponentEvent(ClickEvent<Button> event) {
				UI.getCurrent().navigate("/create-account");
			}
		});

		loginButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void onComponentEvent(ClickEvent<Button> event) {
				UI.getCurrent().navigate("/login");
			}
		});

		HorizontalLayout horLayout = new HorizontalLayout();
		horLayout.add(loginButton, createAccountButton);

		layout.add(horLayout);
		layout.setHorizontalComponentAlignment(Alignment.CENTER, horLayout);
		add(layout);
	}

	/*
	 * Returns a list of the grids. It does this automatically by getting every
	 * subclass of Resource and creating a Grid with it. It then adds all of them to
	 * an ArrayList and returns. NoSuchMethodException happens every time because
	 * the ClassPathScanningCandidateComponentProvider iterates through the
	 * superclass too, except the superclass was made private so it can't create a
	 * grid for it. Supposedly it could, but if it did it would break the code
	 * because the Resource object does not extend itself which is a requirement for
	 * basically everything. A component is simply a class that extends Resource.
	 */
	private ArrayList<Grid<Resource>> getResourceGrids() {
		ArrayList<Grid<Resource>> arr = new ArrayList<>();
		ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
		provider.addIncludeFilter(new AssignableTypeFilter(Resource.class));

		Set<BeanDefinition> components = provider.findCandidateComponents("com/availaboard/engine/resource");
		for (BeanDefinition component : components) {
			try {
				Resource res = (Resource) Class.forName(component.getBeanClassName()).getConstructor().newInstance();
				ResourceGrid<Resource> grid = new ResourceGrid<>(res.getClass());
				Grid<Resource> resGrid = grid.loadGrid(res.getClass());
				arr.add(resGrid);
			} catch (ClassNotFoundException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | SecurityException | InstantiationException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {

			}
		}
		return arr;
	}
}
