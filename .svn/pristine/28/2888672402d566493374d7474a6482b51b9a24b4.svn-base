package com.zillious.corporate_website.ui.session;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;

import com.zillious.corporate_website.i18n.Country;
import com.zillious.corporate_website.i18n.Language;
import com.zillious.corporate_website.logger.Logger;
import com.zillious.corporate_website.portal.ui.UserRoles;
import com.zillious.corporate_website.portal.ui.model.User;
import com.zillious.corporate_website.ui.exception.AccessException;
import com.zillious.corporate_website.ui.security.ZilliousSecurityWrapperRequest;
import com.zillious.corporate_website.ui.security.ZilliousSecurityWrapperResponse;
import com.zillious.corporate_website.utils.DateUtility;
import com.zillious.corporate_website.utils.StringUtility;

public class SessionStore {
    private static final Logger s_logger            = Logger.getInstance(SessionStore.class);

    // DO NOT CHANGE WITHOUT ISO APPROVAL
    private static final int    SESSION_TIMEOUT     = 60 * 15;

    private static final int    REMEMBER_ME_TIMEOUT = 60 * 90;

    public static void sessionListenerDestroy(HttpSession session) {
        try {
            User rootLoggedInUser = null;

            // Check logged in user
            if (rootLoggedInUser == null) {
                rootLoggedInUser = (User) session.getAttribute(SessionObject.LOGGED_IN_USER.name());
            }

            // Logout user
            // if (rootLoggedInUser != null && rootLoggedInUser.getUserId() > 0)
            // {
            // UserLogin.storeLogout(null, rootLoggedInUser);
            // }

        } catch (Exception e) {
            s_logger.error("Error in logging out user due to session listener", e);
        }
    }

    public static void setSessionVariblesToDefault(ZilliousSecurityWrapperRequest request,
            ZilliousSecurityWrapperResponse response) {
        HttpSession session = getSession(request);
        // Get Attribs to Persist
        for (SessionObject sO : SessionObject.values()) {
            if (sO.isPersistAcrossInvalidation()) {
                Object val = session.getAttribute(sO.name());
                if (val != null) {
                    session.setAttribute(sO.name(), null);
                }
            }
        }
    }

    public static Map<SessionObject, Object> getAttributesToPersist(HttpSession session) {
        // Get Attribs to Persist
        Map<SessionObject, Object> persistedAttribs = new HashMap<SessionStore.SessionObject, Object>(1);
        for (SessionObject sO : SessionObject.values()) {
            if (sO.isPersistAcrossInvalidation()) {
                Object val = session.getAttribute(sO.name());
                if (val != null) {
                    persistedAttribs.put(sO, val);
                }
            }
        }
        return persistedAttribs;
    }

    public static void setAttributesToPersist(ZilliousSecurityWrapperRequest request,
            Map<SessionObject, Object> persistedAttribs) {
        // Set Attributes to Persist
        for (SessionObject sO : persistedAttribs.keySet()) {
            Serializable val = (Serializable) persistedAttribs.get(sO);
            setSessionVariable(request, sO, val);
        }
    }

    public static void invalidateSession(ZilliousSecurityWrapperRequest request) {
        HttpSession session = getSession(request);
        Map<SessionObject, Object> persistedAttribs = getAttributesToPersist(session);
        session.invalidate();

        // Set Attribs to Persist
        session = getSession(request);
        setAttributesToPersist(request, persistedAttribs);
    }

    public static String getSessionId(ZilliousSecurityWrapperRequest request) {
        HttpSession session = request.getSession(false);
        return (session == null) ? null : session.getId();
    }

    public static User getLoggedInUser(ZilliousSecurityWrapperRequest request) {
        return (User) getSessionVariable(request, SessionObject.LOGGED_IN_USER);
    }

    /**
     * It is very important to make sure that the existing session is
     * invalidated using SessionStore.invalidateSession function before new
     * login user is set. This is required to prevent session-id implanting by
     * hackers.
     * 
     * @param request
     * @param response
     * @param user
     */
    public static void setLoggedInUser(ZilliousSecurityWrapperRequest request,
            ZilliousSecurityWrapperResponse response, User user) {
        setSessionVariable(request, SessionObject.LOGGED_IN_USER, user);
        setNewRandomCsrfTokens(request, response);
    }

    public static void setLastUserroleCookie(ZilliousSecurityWrapperResponse response, UserRoles ur) {
        Cookie cookie = new Cookie("UR", ur.serialize());
        cookie.setPath("/");
        cookie.setSecure(true);
        cookie.setMaxAge(DateUtility.APPROX_SECONDS_IN_YEAR * 3);
        response.addCookie(cookie);
    }

    public static UserRoles getLastUserroleValue(ZilliousSecurityWrapperRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if ("UR".equals(c.getName())) {
                    return UserRoles.deserialize(c.getValue());
                }
            }
        }
        return null;
    }

    private static Serializable getSessionVariable(ZilliousSecurityWrapperRequest request, SessionObject key) {
        HttpSession session = getSession(request);
        return (Serializable) (session == null ? null : session.getAttribute(key.name()));
    }

    private static void setSessionVariable(ZilliousSecurityWrapperRequest request, SessionObject key, Serializable value) {
        HttpSession session = getSession(request);
        session.setAttribute(key.name(), value);
    }

    public static void setCurrentUITab(ZilliousSecurityWrapperRequest request,
            ZilliousSecurityWrapperResponse response, String uiTab) {
        setSessionVariable(request, SessionObject.CURRENT_UI_TAB, uiTab);
    }

    public static String getCurrentUITab(ZilliousSecurityWrapperRequest request) {
        return (String) getSessionVariable(request, SessionObject.CURRENT_UI_TAB);
    }

    public static void setNewRandomCsrfTokens(ZilliousSecurityWrapperRequest request,
            ZilliousSecurityWrapperResponse response) {
        setNewRandomCsrfUrlToken(request, response);
        setNewRandomCsrfPostToken(request, response);
    }

    private static void setNewRandomCsrfUrlToken(ZilliousSecurityWrapperRequest request,
            ZilliousSecurityWrapperResponse response) {
        setSessionVariable(request, SessionObject.CSRF_URL_TOKEN, User.generateRandomCSRFToken(30));
    }

    public static String getCsrfUrlToken(ZilliousSecurityWrapperRequest request) {
        return (String) getSessionVariable(request, SessionObject.CSRF_URL_TOKEN);
    }

    public static boolean compareCsrfUrlToken(ZilliousSecurityWrapperRequest request, String compareWith)
            throws AccessException {
        String token = getCsrfUrlToken(request);
        if (token != null && !StringUtility.equalsIgnoreCaseWithTrimAndBothNullCheck(token, compareWith)) {
            return false;
        }
        return true;
    }

    private static void setNewRandomCsrfPostToken(ZilliousSecurityWrapperRequest request,
            ZilliousSecurityWrapperResponse response) {
        setSessionVariable(request, SessionObject.CSRF_POST_TOKEN, User.generateRandomCSRFToken(100));
    }

    public static String getCsrfPostToken(ZilliousSecurityWrapperRequest request) {
        return (String) getSessionVariable(request, SessionObject.CSRF_POST_TOKEN);
    }

    public static boolean compareCsrfPostToken(ZilliousSecurityWrapperRequest request, String compareWith)
            throws AccessException {
        String token = getCsrfPostToken(request);
        if (token != null && !StringUtility.equalsIgnoreCaseWithTrimAndBothNullCheck(token, compareWith)) {
            return false;
        }
        return true;
    }

    private static HttpSession getSession(ZilliousSecurityWrapperRequest request) {
        int sessionTimeout = SESSION_TIMEOUT;
        HttpSession session = request.getSession(true);
        if (session == null) {
            return null;
        }
        session.setMaxInactiveInterval(sessionTimeout);
        return session;
    }

    /**
     * This method will set the user selected country from the UI in the
     * cookies. Used for Internationalization.
     * 
     * @param response
     * @param country
     * 
     */
    public static void setCountryCookie(ZilliousSecurityWrapperResponse response, Country country) {
        if (country != null) {
            Cookie cookie = new Cookie("COUNTRY", country.getCode());
            cookie.setPath("/");
            cookie.setSecure(true);
            cookie.setMaxAge(DateUtility.APPROX_SECONDS_IN_YEAR * 3);
            response.addCookie(cookie);
        }
    }

    /**
     * This method will set the user selected language from the UI in the
     * cookies. Used for Internationalization.
     * 
     * @param response
     * @param country
     * 
     */
    public static void setLanguageCookie(ZilliousSecurityWrapperResponse response, Language language) {
        if (language != null) {
            Cookie cookie = new Cookie("LANGUAGE", language.getCode());
            cookie.setPath("/");
            cookie.setSecure(true);
            cookie.setMaxAge(DateUtility.APPROX_SECONDS_IN_YEAR * 3);
            response.addCookie(cookie);
        }
    }

    public static Language getLanguageCookie(ZilliousSecurityWrapperRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if ("LANGUAGE".equals(c.getName())) {
                    return Language.getLanguageFromCodeWithPropertyFileCheck(c.getValue());
                }
            }
        }
        return null;
    }

    public static Country getCountryCookie(ZilliousSecurityWrapperRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if ("COUNTRY".equals(c.getName())) {
                    return Country.getCountryFromCode(c.getValue());
                }
            }
        }
        return null;
    }

    public static void setLanguageInSession(ZilliousSecurityWrapperRequest request,
            ZilliousSecurityWrapperResponse response, Language language) {
        if (language != null)
            setSessionVariable(request, SessionObject.LANGUAGE, language.getCode());
    }

    public static String getLanguageFromSession(ZilliousSecurityWrapperRequest request) {
        if (request == null) {
            return null;
        }
        return (String) getSessionVariable(request, SessionObject.LANGUAGE);
    }

    public static void setCountryInSession(ZilliousSecurityWrapperRequest request,
            ZilliousSecurityWrapperResponse response, Country country) {
        if (country != null)
            setSessionVariable(request, SessionObject.COUNTRY, country.getCode());
    }

    public static String getCountryFromSession(ZilliousSecurityWrapperRequest request) {
        if (request == null) {
            return null;
        }
        return (String) getSessionVariable(request, SessionObject.COUNTRY);
    }

    private SessionStore() {
    }

    private static enum SessionObject {
        LOGGED_IN_USER(false),

        CURRENT_UI_TAB(false),

        CSRF_URL_TOKEN(false),

        CSRF_POST_TOKEN(false),

        LANGUAGE(false),

        COUNTRY(false),

        ;

        private boolean m_presistAcrossInvalidation = false;

        SessionObject(boolean persist) {
            m_presistAcrossInvalidation = persist;
        }

        public boolean isPersistAcrossInvalidation() {
            return m_presistAcrossInvalidation;
        }
    }

    public static void removeLoggedInUser(ZilliousSecurityWrapperRequest zilliousRequest) {
        removeSessionVariable(zilliousRequest, SessionObject.LOGGED_IN_USER);
    }

    private static void removeSessionVariable(ZilliousSecurityWrapperRequest zilliousRequest, SessionObject key) {
        HttpSession session = getSession(zilliousRequest);
        session.removeAttribute(key.name());
    }
}
