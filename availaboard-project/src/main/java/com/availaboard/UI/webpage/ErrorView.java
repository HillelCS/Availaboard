package com.availaboard.UI.webpage;

import javax.servlet.http.HttpServletResponse;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.ErrorParameter;
import com.vaadin.flow.router.HasErrorParameter;
import com.vaadin.flow.router.NotFoundException;
import com.vaadin.flow.router.ParentLayout;
import com.vaadin.flow.router.RouterLink;

/**
 * View shown when trying to navigate to a view that does not exist using
 */
@ParentLayout(MainLayout.class)
public class ErrorView extends VerticalLayout implements HasErrorParameter<NotFoundException> {

    /**
     *
     */
    private static final long serialVersionUID = 2387496440562208964L;
    private static final HorizontalLayout horizontalLayout = new HorizontalLayout();
    private static Span explanation;
    private static final H1 header = new H1("You got lost!");
    private static final RouterLink availaboardButton = new RouterLink("Maybe you want to head back to the Main Page?",
            AvailaboardView.class);

    public ErrorView() {
    

        add(header);
        add(availaboardButton);

        explanation = new Span();
        add(explanation);
    }

    @Override
    public int setErrorParameter(BeforeEnterEvent event, ErrorParameter<NotFoundException> parameter) {
        explanation.setText("Could not navigate to '" + event.getLocation().getPath() + "'.");
        return HttpServletResponse.SC_NOT_FOUND;
    }
}