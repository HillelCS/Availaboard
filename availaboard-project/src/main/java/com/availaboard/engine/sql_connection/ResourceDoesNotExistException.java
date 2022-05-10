package com.availaboard.engine.sql_connection;

import com.availaboard.engine.resource.Resource;

/**
 * Thrown when the program attempts to access a {@link Resource} from the database that does not exist.
 */
public class ResourceDoesNotExistException extends Exception {
}
