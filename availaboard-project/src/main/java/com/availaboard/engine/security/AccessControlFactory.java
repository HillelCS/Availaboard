package com.availaboard.engine.security;

/**
 * Used to create a new instance of a {@link BasicAccessControl} and a
 * {@link accessControl}.
 */
public class AccessControlFactory {

	private static final AccessControlFactory INSTANCE = new AccessControlFactory();

	/**
	 * Creates a new instance of a {@link BasicAccessControl}.
	 *
	 * @return A new instance of a {@link BasicAccessControl}.
	 */
	public static AccessControlFactory getInstance() {
		return AccessControlFactory.INSTANCE;
	}

	private final AccessControl accessControl = new BasicAccessControl();

	/**
	 * Constructor declared private because sessions should be created statically.
	 */
	private AccessControlFactory() {
	}

	/**
	 * Used to create a new {@link accessControl}
	 *
	 * @return A new {@link accessControl} Object.
	 */
	public AccessControl createAccessControl() {
		return accessControl;
	}
}