package com.zillious.lang;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

/**
 * @author Nishant
 * 
 */
public class ResourceManager {
    private static Logger  s_logger         = Logger.getLogger(ResourceManager.class);
    private ResourceBundle s_resourceBundle = null;

    public ResourceManager(ResourceBundle bundle) {
        s_resourceBundle = bundle;
    }

    public String transformText(String prefix, String msgID, String defaultText) {
        String resourceBundleKey = getResourceBundleKey(prefix, msgID);
        try {
            String value = s_resourceBundle.getString(resourceBundleKey);
            return new String(value.getBytes("ISO-8859-1"), "UTF-8");
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String exceptionAsString = sw.toString();
            boolean logError = (exceptionAsString.indexOf("find resource for bundle java.util.PropertyResourceBundle") == -1);
            if (logError) {
                s_logger.error("Error while getting value from the resourcebundle property file: " + resourceBundleKey,
                        e);
            } else {
                s_logger.info("Could not find value for key: " + resourceBundleKey + ", language: "
                        + s_resourceBundle.getLocale().getLanguage() + ", country: "
                        + s_resourceBundle.getLocale().getCountry());
            }
            return defaultText;
        }
    }

    private String getResourceBundleKey(String prefix, String msgID) {
        StringBuilder key = new StringBuilder();
        if (prefix != null) {
            key.append(prefix).append("_");
        }
        return key.append(msgID).toString();
    }

}
