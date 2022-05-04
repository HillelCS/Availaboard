package com.availaboard.utilitys;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Properties;

/**
 * Reads the config.properties file.
 */
public class ConfigPropReader {

    /**
     * @return An {@link ArrayList} of the <code>username</code>,
     * <code>password</code>, and <code>URL</code> for the database.
     */
    public static ArrayList<String> getPropValues() {
        final ArrayList<String> result = new ArrayList<>();
        try {
            final Properties prop = new Properties();
            final File file = new File("src/main/resources/config.properties");
            final FileInputStream ip = new FileInputStream(file);
            prop.load(ip);
            result.add(prop.getProperty("username"));
            result.add(prop.getProperty("password"));
            result.add(prop.getProperty("url"));
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}