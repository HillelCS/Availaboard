package com.availaboard.UI.view_structure;

import com.availaboard.engine.resource.Resource;

public interface Observer {
    void update();
    void register(Subject subject);
    Subject getSubject();
}
