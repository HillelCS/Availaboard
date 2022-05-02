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

    /**
     *
     */
    private static final long serialVersionUID = -4432887017833022089L;
    AvailaboardSQLConnection db = new AvailaboardSQLConnection();

    public AvailaboardView() {
        addGridsToLayout();
    }

    private void addGridsToLayout() {
        getResourceGrids().stream().forEach(grid -> {
            add(grid);
            setHorizontalComponentAlignment(Alignment.CENTER, grid);
        });
    }

    /**
     * Iterates through every subclass of {@link Resource} and creates a
     * {@link ResourceGrid} with each of them. It then adds them all to an
     * {@link ArrayList}.
     *
     * @return An {@link ArrayList} of each subclass of {@link Resource} added as a
     * type to a {@link ResourceGrid}.
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
                     | InvocationTargetException | SecurityException | InstantiationException
                     | NoSuchMethodException e) {

            }
        }
        return arr;
    }
}
