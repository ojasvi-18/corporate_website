package com.zillious.corporate_website.ui.navigation;

import java.net.URLEncoder;

import com.zillious.corporate_website.ui.beans.UIBean;
import com.zillious.corporate_website.ui.security.ZilliousSecurityWrapperRequest;
import com.zillious.corporate_website.ui.security.ZilliousSecurityWrapperResponse;

public enum WebsitePages {
    /*
     * Common pages
     */
    NO_PAGE("", false),
    // Redirect request
    REDIRECT_URL("", false),
    // Provide JSP page path from base
    JSP_PAGE_PATH("", false),

    /*
     * Main Simple Pages
     */
    HOME_PAGE("index.jsp", false),

    ABOUT_PAGE("about.jsp", false),

    PRODUCTS_PAGE("products.jsp", false),

    SERVICES_PAGE("services.jsp", false),

    CAREERS_PAGE("careers.jsp", false),

    CONTACT_PAGE("contact.jsp", false),

    PROFILE_PAGE("profle.jsp", false),

    TEAMS_PAGE("teams.jsp", false),

    EMPLOYEE_PAGE("employees.jsp", false),

    /*
     * Misc Simple pages
     */
    ERROR_PAGE("error.jsp", false),

    PRIVACY_PAGE("privacy.jsp", false),

    TERMS_PAGE("terms.jsp", false),

    UNSUPPORTED_BROWSER("browsers.jsp", false),

    SETUP_POPUP_PAGE("setup_popup.jsp", false),

    ATTENDANCE_TRACKER_PAGE("attendance_tracker.jsp", false),

    LOGIN_PAGE("login.jsp", false),

    MANAGE_USERS_PAGE("manage_users.jsp", false),

    /*
     * Complex Actions
     */

    ;

    /*
     * Static Variables and Functions
     */
    public static final String  BASE_PATH          = "/WEB-INF/pages/";
    private static final String REDIRECT_URL_ATTR  = "REDIRECT_URL_ATTR";
    private static final String JSP_PAGE_PATH_ATTR = "VISIBLE_TEMP_PAGE_ATTR";

    /*
     * Object Variables and Functions
     */
    private String              m_jspName          = "";
    private boolean             m_cachePage        = true;

    WebsitePages(String jspName, boolean cache) {
        m_jspName = jspName;
        m_cachePage = cache;
    }

    public boolean isCachePage() {
        return m_cachePage;
    }

    public String getJspFilePath() {
        return m_jspName;
    }

    public static final WebsitePages setRedirectUrlInRequest(ZilliousSecurityWrapperRequest request, String url) {
        request.setAttribute(REDIRECT_URL_ATTR, url);
        return WebsitePages.REDIRECT_URL;
    }

    public static final WebsitePages setRedirectUrlInRequest(ZilliousSecurityWrapperRequest request, String url,
            String fwdUrl) {
        try {
            fwdUrl = URLEncoder.encode(fwdUrl, "UTF-8");
        } catch (Exception e) {
        }
        if (url.indexOf("?") == -1) {
            request.setAttribute(REDIRECT_URL_ATTR, url + "?" + UIBean.FWD_URL_PARAM + "=" + fwdUrl);
        } else {
            request.setAttribute(REDIRECT_URL_ATTR, url + "&" + UIBean.FWD_URL_PARAM + "=" + fwdUrl);
        }
        return WebsitePages.REDIRECT_URL;
    }

    public static final String getRedirectUrlInRequest(ZilliousSecurityWrapperRequest request) {
        return (String) request.getAttribute(REDIRECT_URL_ATTR);
    }

    public String getActualJspFilePath(ZilliousSecurityWrapperRequest request) {
        return BASE_PATH + getJspFilePath();
    }

    public static final WebsitePages setJspPagePathInRequest(ZilliousSecurityWrapperRequest request, String jspPath) {
        request.setAttribute(JSP_PAGE_PATH_ATTR, jspPath);
        return WebsitePages.JSP_PAGE_PATH;
    }

    public static final String getJspPagePathInRequest(ZilliousSecurityWrapperRequest request) {
        return (String) request.getAttribute(JSP_PAGE_PATH_ATTR);
    }

    public static WebsitePages setRedirectUrlInRequest(ZilliousSecurityWrapperRequest request,
            ZilliousSecurityWrapperResponse response, WebsiteActions action) {
        return setRedirectUrlInRequest(request, action.getActionURL(request, response, null, true));
    }
}
