package com.availaboard.UI.view_structure;

public interface Subject {
    void addObserver(Observer observer);
    void removeObserver(Observer observer);
    void notifiyObservers();

}
