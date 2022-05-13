package com.availaboard.engine.sql_connection;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Indicates that class should be a Table.
 */
@Retention(RUNTIME)
@Target(ElementType.TYPE)
public @interface Table {
    /**
     * @return The table name
     */
    String value();
}
