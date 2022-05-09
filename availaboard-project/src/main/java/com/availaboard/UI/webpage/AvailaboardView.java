package com.availaboard.UI.webpage;

import com.availaboard.UI.frontend_functionality.ResourceGrid;
import com.availaboard.UI.view_structure.Observer;
import com.availaboard.UI.view_structure.Subject;
import com.availaboard.UI.view_structure.ViewConfiguration;
import com.availaboard.UI.view_structure.ViewFactory;
import com.availaboard.engine.resource.Resource;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AssignableTypeFilter;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Set;

@PageTitle("Availaboard")
@CssImport("./styles/webpage-styles/availaboard.css")
@Theme(themeClass = Lumo.class, variant = Lumo.DARK)
@Route(value = AvailaboardView.VIEWNAME, layout = MainLayout.class)
public class AvailaboardView extends VerticalLayout implements AppShellConfigurator, Observer {

    protected static final String VIEWNAME = "/";
    /**
     *
     */
    private static final long serialVersionUID = -4432887017833022089L;
    private final VerticalLayout layout = new VerticalLayout();


    /**
     * Adds all of the {@link ResourceGrid}'s to a {@link VerticalLayout}.
     */
    private void addGridsToLayout() {
        getResourceGrids().forEach(grid -> {
            layout.add(grid);
            layout.setHorizontalComponentAlignment(Alignment.CENTER, grid);
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
        final ArrayList<Grid<Resource>> arr = new ArrayList<>();
        final ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
        provider.addIncludeFilter(new AssignableTypeFilter(Resource.class));

        final Set<BeanDefinition> components = provider.findCandidateComponents("com/availaboard/engine/resource");
        for (final BeanDefinition component : components) {
            try {
                final Resource res = (Resource) Class.forName(component.getBeanClassName()).getConstructor().newInstance();
                final ResourceGrid<Resource> grid = new ResourceGrid<>(res.getClass());
                final Grid<Resource> resGrid = grid.loadGrid(res.getClass());
                arr.add(resGrid);

            } catch (final ClassNotFoundException | IllegalAccessException | IllegalArgumentException
                           | InvocationTargetException | SecurityException | InstantiationException
                           | NoSuchMethodException ignored) {

            }
        }
        return arr;
    }

    @Override
    public void initialize() {
        addGridsToLayout();
        add(layout);
    }

    @Override
    public String viewName() {
        return VIEWNAME;
    }

    @Override
    public void update() {
        layout.removeAll();
        addGridsToLayout();
    }

    @Override
    public void register(Subject subject) {
        subject.addObserver(this);
    }

    @Override
    public void unregister(Subject subject) {
        subject.addObserver(this);
    }

    @Override
    public Subject getSubject() {
        return ViewFactory.createViewControllerInstance();
    }
}
