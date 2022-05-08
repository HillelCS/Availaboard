package com.availaboard.UI.view_structure;

import com.vaadin.flow.router.Route;

public interface ViewType {
     /**
      * Get's the Views {@link Route} path.
      * @return The View's {@link Route} path.
      */
     String viewName();

}
