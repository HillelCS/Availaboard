package com.availaboard.UI.frontend_functionality;

import com.availaboard.engine.resource.Resource;
import com.availaboard.engine.resource.Status;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Label;

@CssImport("./styles/webpage-styles/vaadin-component-utilitys.css")
public class VaadinComponentUtilitys {

    /**
     * Creates a {@link Label} with the text "Available" or "Busy"
     * depending on the {@link Resource}'s {@link Status}.
     * The color is also either Red or Green depending on it's
     * {@link Status}.
     *
     * @param res The {@link Resource} used to see if it's {@link Status} is
     *            <code>Busy</code> or <code>Available</code>
     * @return A {@link Label} with text set to the {@link Status} of the
     * {@link Resource} and corresponding color.
     */
    public static Label statusLabel(final Resource res) {
        final Label label = new Label();
        label.setText(res.getStatus().toString());
        final String labelClassName = res.getStatus() == Status.AVAILABLE ? "label-available" : "label-busy";
        label.addClassName(labelClassName);
        return label;
    }
}
