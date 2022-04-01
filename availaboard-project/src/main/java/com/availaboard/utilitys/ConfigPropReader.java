
package com.availaboard.utilitys;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Properties;

public class ConfigPropReader {
	/*
	 * Loads an ArrayList of the username, password
	 * and URL needed to login to a database. 
	 * AvailaboardSQLConnection uses it. The method grabs
	 * the information from config.properties.
	 */
	public ArrayList<String> getPropValues() {
		ArrayList<String> result = new ArrayList<String>();
		try {
			Properties prop = new Properties();
			File file = new File("src/main/resources/config.properties");
			FileInputStream ip = new FileInputStream(file);
			prop.load(ip);
			result.add(prop.getProperty("username"));
			result.add(prop.getProperty("password"));
			result.add(prop.getProperty("url"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}