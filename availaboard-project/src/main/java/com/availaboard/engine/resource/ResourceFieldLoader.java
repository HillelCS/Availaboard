package com.availaboard.engine.resource;

import java.lang.annotation.Retention;

import java.lang.annotation.RetentionPolicy;
/*
 * Use this annotation for any field that you want to load into a 
 * grid. The value should be the "nickname" of the field. The nickname  
 * is the name that will be displayed in the resource grid.
 */

@Retention(RetentionPolicy.RUNTIME)
public @interface ResourceFieldLoader {
	String value();
}
