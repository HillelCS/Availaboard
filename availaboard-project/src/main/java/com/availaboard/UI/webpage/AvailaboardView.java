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
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;

@PageTitle("Availaboard")
@CssImport("./styles/webpage-styles/availaboard.css")
@Theme(themeClass = Lumo.class, variant = Lumo.DARK)
@Route(value = "", layout = MainLayout.class)
public class AvailaboardView extends VerticalLayout implements AppShellConfigurator {

	AvailaboardSQLConnection db = new AvailaboardSQLConnection();

	/*
	 * Uses a stream to add all of the grids to the layout and centers them. Also
	 * uses layouts to properly position everything on the grid.
	 */
	public AvailaboardView() {

		getResourceGrids().stream().forEach(grid -> {
			add(grid);
			setHorizontalComponentAlignment(Alignment.CENTER, grid);
		});
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
