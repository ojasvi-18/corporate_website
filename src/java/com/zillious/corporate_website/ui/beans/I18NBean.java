package com.zillious.corporate_website.ui.beans;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import com.zillious.corporate_website.i18n.Country;
import com.zillious.corporate_website.i18n.Language;
import com.zillious.corporate_website.ui.navigation.WebsitePages;
import com.zillious.corporate_website.ui.security.ZilliousSecurityWrapperRequest;
import com.zillious.corporate_website.ui.security.ZilliousSecurityWrapperResponse;
import com.zillious.corporate_website.ui.session.SessionStore;

public class I18NBean {
    private static final String                PROPERTY_FILE_NAME = "messages";
    private static final String                FOLDERPREFIX       = "language";
    private static Map<String, ResourceBundle> s_resourceBundles  = new HashMap<String, ResourceBundle>();
    private static Logger                      s_logger           = Logger.getLogger(I18NBean.class);

    public static void addOrUpdateLocaleSettings(ZilliousSecurityWrapperRequest request,
            ZilliousSecurityWrapperResponse response, Language languageFromCode, boolean isForceUpdate) {
        Language languageCookie = SessionStore.getLanguageCookie(request);
        if (isForceUpdate || languageCookie == null) {
            SessionStore.setLanguageCookie(response, languageFromCode);
        }

        String langFromSession = SessionStore.getLanguageFromSession(request);

        if (isForceUpdate || langFromSession == null) {
            SessionStore.setLanguageInSession(request, response, languageFromCode);
        }
    }

    public static String getSelectedLanguage(ZilliousSecurityWrapperRequest request) {
        String language = null;
        language = SessionStore.getLanguageFromSession(request) != null ? (SessionStore.getLanguageFromSession(request))
                : (SessionStore.getLanguageCookie(request) != null ? SessionStore.getLanguageCookie(request).getCode()
                        : Language.ENGLISH.getCode());
        return language;

    }

    public static String getSelectedCountry(ZilliousSecurityWrapperRequest request) {
        String country = null;
        country = SessionStore.getCountryFromSession(request) != null ? (SessionStore.getCountryFromSession(request))
                : (SessionStore.getCountryCookie(request) != null ? SessionStore.getCountryCookie(request).getCode()
                        : Country.INDIA.getCode());
        return country;
    }

    public static WebsitePages changeLocale(ZilliousSecurityWrapperRequest request,
            ZilliousSecurityWrapperResponse response, String[] queryParams) throws Exception {
        // String country = request.getParameter("country_i18n", "country_i18n",
        // ZilliousSecurityRequestType.ENUM_ALPHA);
        if (queryParams != null && queryParams.length > 0) {
            String language = queryParams[0];
            Language languageFromCode = Language.getLanguageFromCodeWithPropertyFileCheck(language);
            if (languageFromCode != null) {
                addOrUpdateLocaleSettings(request, response, languageFromCode, true);
            } else {
                s_logger.info("Illegal language code: " + queryParams[0] + " from: " + request.getRemoteAddr());
            }
        }
        return WebsitePages.HOME_PAGE;
    }

    public static Locale getSelectedLocale(ZilliousSecurityWrapperRequest request) {
        return new Locale(getSelectedLanguage(request), getSelectedCountry(request));
    }

    public static ResourceBundle getLocaleBundleFromRequest(ZilliousSecurityWrapperRequest zilliousRequest) {
        String languageCode = getSelectedLanguage(zilliousRequest);
        // String countryCode = getSelectedCountry(zilliousRequest);
        String key = /*countryCode + "_" + */languageCode;
        ResourceBundle resourceBundle = s_resourceBundles.get(key);
        if (resourceBundle == null) {
            Locale locale = new Locale(languageCode);
            resourceBundle = ResourceBundle.getBundle(FOLDERPREFIX + "." + PROPERTY_FILE_NAME, locale);
            s_resourceBundles.put(key, resourceBundle);
        }
        return resourceBundle;
    }
}
