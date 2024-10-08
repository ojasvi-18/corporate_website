package com.zillious.corporate_website.utils;

import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * @author Nishant
 * 
 */
public class ConfigStore {
    private static final String PROP_FILE = "website_app.properties";
    private static Properties   s_prop    = null;
    private static Logger       s_logger  = Logger.getLogger(ConfigStore.class);

    public static void initialise() {
        s_logger.info("Initialising properties file Config Store");
        try {
            s_prop = new Properties();
            s_prop.load(ConfigStore.class.getClassLoader().getResourceAsStream(PROP_FILE));
            s_logger.info("properties file Config Store initialised");
        } catch (IOException e) {
            s_logger.fatal("Could not load Website Application properties: ", e);
        }
    }

    public static String getStringValue(String key, String defaultVal) {
        try {
            String value = s_prop.getProperty(key);
            if (value != null && !value.trim().isEmpty()) {
                return value;
            }
        } catch (Exception e) {
            s_logger.error("Error while getting property file value for the key: " + key);
        }
        return defaultVal;
    }

}
