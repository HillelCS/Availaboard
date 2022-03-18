package com.availaboard.utilitys;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;

public class ConfigPropReader {

	public ArrayList<String> getPropValues() {
		ArrayList<String> result = new ArrayList<String>();
		try {
			Properties prop = new Properties();
			File file = new File("C:\\Programming Projects\\Java\\Availaboard\\Availaboard\\availaboard-project\\res\\config.properties");
			FileInputStream ip = new FileInputStream(file);
			prop.load(ip);

			result.add(prop.getProperty("username"));
			result.add(prop.getProperty("password"));
			result.add(prop.getProperty("url"));

		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(result.get(0) + "FILEFILEFILEFILE");
		return result;
	}
}
