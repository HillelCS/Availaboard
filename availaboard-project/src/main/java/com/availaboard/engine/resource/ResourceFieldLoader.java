package com.availaboard.engine.resource;

import java.lang.annotation.Retention;

import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

/**
 * Adds a nickname to a Field and indicates whether it should be added to the <code>UI</code>.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ResourceFieldLoader {
	/**
	 * @return The "nickname" to be used when showing this field in the <code>UI</code>.
	 */
	String value();
}
