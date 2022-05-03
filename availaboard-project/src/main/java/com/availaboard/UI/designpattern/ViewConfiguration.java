package com.availaboard.UI.designpattern;

import org.springframework.stereotype.Component;

public interface ViewConfiguration extends ViewType {
    /**
     * Add all the components to the view
     */
    void addAll();

}
