package com.zillious.corporate_website.i18n;

import com.zillious.corporate_website.logger.Logger;

public enum Country {
    INDIA("IN", "India", new Language[] { Language.ENGLISH }, 0),

    UAE("AE", "UAE", new Language[] { Language.ENGLISH, Language.ARABIC }, 1),

    ;

    private String        m_code;
    private String        m_displayName;
    private Language[]    m_languages;
    private Language      m_defaultLanguage;
    private static Logger s_logger = Logger.getInstance(Country.class);

    @Override
    public String toString() {
        return m_code + ":" + m_displayName;
    }

    Country(String code, String displayName, Language[] languages, int defLangIndex) {
        m_code = code;
        m_displayName = displayName;
        m_languages = languages;
        m_defaultLanguage = m_languages[defLangIndex];
    }

    public static Country getCountryFromCode(String code) {
        if (code == null) {
            return null;
        }
        for (Country country : Country.values()) {
            if (code.trim().equals(country.m_code)) {
                return country;
            }
        }
        s_logger.info("The country for country code: " + code + " is not supported. Defaulting to India");
        return INDIA;

    }

    public String getDisplayName() {
        return m_displayName;
    }

    public String getCode() {
        return m_code;
    }

    public Language[] getLanguages() {
        return m_languages;
    }

    public void setLanguages(Language[] languages) {
        m_languages = languages;
    }

    public Language getDefaultLanguage() {
        return m_defaultLanguage;
    }

    public void setDefaultLanguage(Language defaultLanguage) {
        m_defaultLanguage = defaultLanguage;
    }

    public void setCode(String code) {
        m_code = code;
    }

    public void setDisplayName(String displayName) {
        m_displayName = displayName;
    }

    public static String getCountryLanguageJSON() {
        StringBuilder json = new StringBuilder("[");
        for (int j = 0; j < Country.values().length; j++) {
            Country country = Country.values()[j];
            json.append("{'country':'").append(country.getCode()).append("', lang:[");
            for (int i = 0; i < country.getLanguages().length; i++) {
                Language lang = country.getLanguages()[i];
                json.append("{'code':'").append(lang.getCode()).append("', 'name':'").append(lang.getDisplayName())
                        .append("', 'langDir':'").append(lang.getRTLDirection()).append("'}");
                if (i < country.getLanguages().length - 1) {
                    json.append(",");
                }
            }
            json.append("]}");
            if (j < Country.values().length - 1) {
                json.append(",");
            }
        }
        json.append("]");
        String jsonString = json.toString();
        s_logger.debug("Country-Language JSON: " + jsonString);
        return jsonString;
    }
}
