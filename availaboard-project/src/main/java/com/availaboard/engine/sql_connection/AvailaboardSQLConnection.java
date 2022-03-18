package com.availaboard.engine.sql_connection;

import java.util.ArrayList;

import com.availaboard.engine.resource.Resource;
import com.availaboard.engine.resource.Status;
import com.availaboard.utilitys.ConfigPropReader;

public class AvailaboardSQLConnection {
	ConfigPropReader config = new ConfigPropReader();

	public final String username = config.getPropValues().get(0);
	private final String password = config.getPropValues().get(1);
	private final String url =  config.getPropValues().get(2);

	/*
	 * Load all resources from the database
	 */
	public ArrayList<Resource> loadResources() {
		ArrayList<Resource> arr = new ArrayList<Resource>();
		Resource res = new Resource();
		res.setName("Sam Jacobson");
		res.setStatus(Status.AVAILABLE);
		arr.add(res);
		return arr;
	}
}
