package com.zillious.corporate_website.i18n;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.log4j.Logger;

public enum Language {

    ENGLISH("en", "English", false, "Eng", true),

    ARABIC("ar", "Arabic", true, "العربية", true),

    HINDI("hi", "Hindi", false, "हिंदी", false), ;

    private String        m_code;
    private String        m_displayName;
    private boolean       m_rtl;
    private String        m_displayNameInLanguage;
    private boolean       m_hasPropertyFile;

    private static String s_directionLeftToRight = "ltr";
    private static String s_directionRightToLeft = "rtl";
    private static Logger s_logger               = Logger.getLogger(Language.class);

    Language(String code, String displayName, boolean directionLtR, String displayInLanguage, boolean hasProperty) {
        m_code = code;
        m_displayName = displayName;
        m_rtl = directionLtR;
        m_displayNameInLanguage = displayInLanguage;
        m_hasPropertyFile = hasProperty;

    }

    private static Language getLanguageFromCode(String code, boolean ischeckPropertyFile) {
        if (code == null) {
            return null;
        }
        for (Language lang : Language.values()) {
            if ((!ischeckPropertyFile || (ischeckPropertyFile && lang.m_hasPropertyFile))
                    && code.trim().equals(lang.m_code)) {
                return lang;
            }
        }
        return null;
    }

    public String getDisplayName() {
        return m_displayName;
    }

    public String getCode() {
        return m_code;
    }

    public boolean getRTLDirection() {
        return m_rtl;
    }

    public static String getDirectionString(boolean isDirectionRTL) {
        if (isDirectionRTL) {
            return s_directionRightToLeft;
        } else {
            return s_directionLeftToRight;
        }
    }

    public static String getLanguageJSON() {
        StringBuilder json = new StringBuilder("[");
        int i = 0;
        for (Language lang : Language.values()) {
            json.append("{'code':'").append(lang.getCode()).append("', 'name':'").append(lang.getDisplayName())
                    .append("', 'langDir':'").append(lang.getRTLDirection()).append("'}");
            if (i < Language.values().length - 1) {
                json.append(",");
            }
            i++;
        }
        json.append("]}");
        json.append("]");
        String jsonString = json.toString();
        s_logger.debug("Language JSON: " + jsonString);
        return jsonString;
    }

    public static Map<String, String> getLanguageMapForSelection(String selectedLanguage) throws Exception {
        Map<String, String> map = new LinkedHashMap<String, String>();
        for (Language lang : Language.values()) {
            if (lang.m_hasPropertyFile && !selectedLanguage.equals(lang.m_code)) {
                map.put(lang.m_code, new String(lang.m_displayNameInLanguage.getBytes("UTF-8"), "UTF-8"));
            }
        }
        return map;
    }

    public String getDisplayNameInLanguage() {
        return m_displayNameInLanguage;
    }

    public void setDisplayNameInLanguage(String displayNameInLanguage) {
        m_displayNameInLanguage = displayNameInLanguage;
    }

    public boolean isHasPropertyFile() {
        return m_hasPropertyFile;
    }

    public void setHasPropertyFile(boolean hasPropertyFile) {
        m_hasPropertyFile = hasPropertyFile;
    }

    public static Language getLanguageFromCodeWithPropertyFileCheck(String language) {
        return getLanguageFromCode(language, true);
    }

}
