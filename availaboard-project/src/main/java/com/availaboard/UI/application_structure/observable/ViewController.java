package com.availaboard.UI.application_structure.observable;

import com.availaboard.UI.application_structure.view_structure.ViewObserver;

import java.util.ArrayList;
import java.util.Iterator;
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
        for (Iterator<ViewObserver> iterator = observerList.iterator(); iterator.hasNext(); ) {
            ViewObserver observer = iterator.next();
            if (observer.getUI().isPresent()) {
                observer.update();
            } else {
                iterator.remove();
            }
        }
    }
}
