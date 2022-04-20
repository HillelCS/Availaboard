package com.availaboard.engine.security;

import com.availaboard.engine.resource.Resource;

/**
 * Used to create a new instance of a {@link BasicAccessControl} and a
 * {@link accessControl}.
 */
public class AccessControlFactory {
	
    private static final AccessControlFactory INSTANCE = new AccessControlFactory();
    private final AccessControl accessControl = new BasicAccessControl();

    /**
     * Constructor declared private because sessions
     * should be created statically.
     */
    private AccessControlFactory() {
    }

    /**
     * Creates a new instance of a {@link BasicAccessControl}.
     * @return A new instance of a {@link BasicAccessControl}.
     */
    public static AccessControlFactory getInstance() {
        return INSTANCE;
    }

    /**
     * Used to create a new {@link accessControl}
     * @return A new {@link accessControl} Object.
     */
    public AccessControl createAccessControl() {
        return accessControl;
    }
}