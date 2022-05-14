package com.availaboard.UI.frontend_functionality;

import com.availaboard.engine.resource.Resource;
import com.availaboard.engine.resource.Status;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

/**
 * A class used to create Vaadin {@link Component}'s that are
 * used throughout the application.
 */
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

    public static Notification createNotification(final String text, NotificationVariant variant, int time) {
        final Notification notification = new Notification();
        notification.addThemeVariants(variant);
        final Div notificationText = new Div(new Text(text));
        final Button closeButton = new Button(new Icon("lumo", "cross"));
        closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        closeButton.getElement().setAttribute("aria-label", "Close");
        closeButton.addClickListener(event -> notification.close());
        final HorizontalLayout layout = new HorizontalLayout(notificationText, closeButton);
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        notification.add(layout);
        notification.setDuration(time);
        return notification;
    }
}
