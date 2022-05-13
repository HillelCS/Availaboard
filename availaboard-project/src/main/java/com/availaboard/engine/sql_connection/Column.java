package com.availaboard.engine.sql_connection;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

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
