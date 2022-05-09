package com.availaboard.engine.security;

/**
 * Used to create a new instance of a {@link BasicAccessControl} and a
 * {@link AccessControl}.
 */
public class AccessControlFactory {

    private static final AccessControlFactory INSTANCE = new AccessControlFactory();
    private final AccessControl accessControl = new BasicAccessControl();

    /**
     * Constructor declared private because sessions should be created statically.
     */
    private AccessControlFactory() {

    }

    /**
     * Creates a new instance of a {@link BasicAccessControl}.
     *
     * @return A new instance of a {@link BasicAccessControl}.
     */
    public static AccessControlFactory getInstance() {
        return AccessControlFactory.INSTANCE;
    }

    /**
     * Used to create a new {@link AccessControl}
     *
     * @return A new {@link AccessControl} Object.
     */
    public AccessControl createAccessControl() {
        return accessControl;
    }
}