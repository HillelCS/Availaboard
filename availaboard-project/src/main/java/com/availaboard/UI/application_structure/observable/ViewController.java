package com.availaboard.UI.application_structure.observable;

import com.vaadin.flow.component.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * A concrete implementation of the {@link Subject} interface.
 */
public class ViewController implements Subject {
    List<Observer> observerList = new ArrayList();

    @Override
    public void addObserver(Observer observer) {
        observerList.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observerList.remove(observer);
    }

    @Override
    public void notifiyObservers() {
        observerList.forEach(observer -> {
            observer.getUI().ifPresent(ui -> {
                ui.access(() -> {
                    try {
                        observer.update();
                    } catch (IllegalStateException ignored) {

                    }
                });
            });
        });
    }
}
