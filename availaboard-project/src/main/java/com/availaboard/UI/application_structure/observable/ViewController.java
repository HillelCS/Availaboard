package com.availaboard.UI.application_structure.observable;

import com.availaboard.UI.application_structure.view_structure.ViewObserver;

import java.util.ArrayList;
import java.util.List;

/**
 * A concrete implementation of the {@link Subject} interface.
 */
public class ViewController implements Subject {
    List<ViewObserver> observerList = new ArrayList();

    @Override
    public void addObserver(Observer observer) {
        observerList.add((ViewObserver) observer);
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