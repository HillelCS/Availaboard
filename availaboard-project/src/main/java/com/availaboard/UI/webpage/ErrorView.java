package com.availaboard.UI.webpage;

import com.availaboard.UI.view_pattern.ViewConfiguration;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;

import javax.servlet.http.HttpServletResponse;

/**
 * View shown when trying to navigate to a view that does not exist using
 */
@ParentLayout(MainLayout.class)
public class ErrorView extends VerticalLayout implements HasErrorParameter<NotFoundException>, ViewConfiguration {

    private static final long serialVersionUID = 2387496440562208964L;
    private final Span explanation = new Span();
    private final H1 header = new H1("You got lost!");
    private final RouterLink availaboardButton = new RouterLink("Maybe you want to head back to the Main Page?",
            AvailaboardView.class);

    @Override
    public void addAll() {
        add(header);
        add(availaboardButton);
        add(explanation);
    }

    @Override
    public int setErrorParameter(final BeforeEnterEvent event, final ErrorParameter<NotFoundException> parameter) {
        explanation.setText("Could not navigate to '" + event.getLocation().getPath() + "'.");
        return HttpServletResponse.SC_NOT_FOUND;
    }
}