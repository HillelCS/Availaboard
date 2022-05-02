package com.availaboard.engine.sql_connection;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Indicates the Field is a Column
 */
@Retention(RUNTIME)
@Target(ElementType.FIELD)
public @interface Column {
    /**
     * @return The column name
     */
    String value();
}
