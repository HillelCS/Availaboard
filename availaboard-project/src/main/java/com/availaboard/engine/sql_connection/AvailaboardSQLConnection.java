package com.availaboard.engine.sql_connection;

import java.util.ArrayList;

import com.availaboard.engine.resource.Resource;
import com.availaboard.engine.resource.Status;

public class AvailaboardSQLConnection {
	
	
	/*
	 * Load all resources from the database
	 */
	public ArrayList<Resource> loadResources() {
		ArrayList<Resource> arr = new ArrayList<Resource>();
		Resource res = new Resource();
		res.setName("sam");
		res.setStatus(Status.AVAILABLE);
		arr.add(res);
		return arr;
	}
}
