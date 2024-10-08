package com.zillious.corporate_website.ui.navigation;

import java.util.ArrayList;
import java.util.List;

import com.zillious.corporate_website.logger.Logger;
import com.zillious.corporate_website.portal.ui.UserRoles;
import com.zillious.corporate_website.ui.beans.AppBootstrapBean;
import com.zillious.corporate_website.ui.beans.AttendanceBean;
import com.zillious.corporate_website.ui.beans.AudienceBean;
import com.zillious.corporate_website.ui.beans.I18NBean;
import com.zillious.corporate_website.ui.security.ZilliousSecurityWrapperRequest;
import com.zillious.corporate_website.ui.security.ZilliousSecurityWrapperResponse;
import com.zillious.corporate_website.ui.session.SessionStore;

public enum WebsiteActions {
    /*
     * Static Pages
     */
    HOME("home", WebsitePages.HOME_PAGE, UserRoles.getAllRoles(), false),

    ABOUT("about", WebsitePages.ABOUT_PAGE, UserRoles.getAllRoles(), false),

    PRODUCTS("products", WebsitePages.PRODUCTS_PAGE, UserRoles.getAllRoles(), false),

    SERVICES("services", WebsitePages.SERVICES_PAGE, UserRoles.getAllRoles(), false),

    CAREERS("careers", WebsitePages.CAREERS_PAGE, UserRoles.getAllRoles(), false),

    PRIVACY("privacy", WebsitePages.PRIVACY_PAGE, UserRoles.getAllRoles(), false),

    TERMS("terms", WebsitePages.TERMS_PAGE, UserRoles.getAllRoles(), false),

    BROWSER_UNSUPPORTED("unsupported_browser", WebsitePages.UNSUPPORTED_BROWSER, UserRoles.getAllRoles(), false),

    /*
     * Actions
     */
    CONTACT("contact", WebsitePages.CONTACT_PAGE, UserRoles.getAllRoles(), false) {
        @Override
        public WebsitePages executeAction(ZilliousSecurityWrapperRequest request,
                ZilliousSecurityWrapperResponse response, String[] queryParams, boolean isPost) throws Exception {
            return AudienceBean.addContactRequest(request, response, queryParams);
        }
    },

    NEWSLETTER("newsletter", WebsitePages.TERMS_PAGE, UserRoles.getAllRoles(), false) {
        @Override
        public WebsitePages executeAction(ZilliousSecurityWrapperRequest request,
                ZilliousSecurityWrapperResponse response, String[] queryParams, boolean isPost) throws Exception {
            return AudienceBean.subscribeToNewsLetter(request, response, queryParams);
        }
    },

    CHANGE_LOCALE("change_locale", WebsitePages.HOME_PAGE, UserRoles.getAllRoles(), false) {
        @Override
        public WebsitePages execute(ZilliousSecurityWrapperRequest request, ZilliousSecurityWrapperResponse response,
                String[] queryParams, boolean isPost) throws Exception {
            return I18NBean.changeLocale(request, response, queryParams);
        }
    },

    SETUP_INDEX_PAGE_POPUP("setup_popup", WebsitePages.HOME_PAGE, UserRoles.getAllRoles(), false) {
        @Override
        public WebsitePages executeAction(ZilliousSecurityWrapperRequest request,
                ZilliousSecurityWrapperResponse response, String[] queryParams, boolean isPost) throws Exception {
            return AudienceBean.setupIndexPagePopup(request, response, queryParams);
        }
    },

    UPLOAD_ATTENDANCE("upattn", WebsitePages.NO_PAGE, UserRoles.getAdminRoles(), false) {
        @Override
        public WebsitePages executeAction(ZilliousSecurityWrapperRequest request,
                ZilliousSecurityWrapperResponse response, String[] queryParams, boolean isPost) throws Exception {
            return AttendanceBean.uploadAttendance(request, response, queryParams);
        }
    },

    POST_LOAD("post-load", WebsitePages.NO_PAGE, UserRoles.getAllRoles(), false) {
        @Override
        public WebsitePages executeAction(ZilliousSecurityWrapperRequest request,
                ZilliousSecurityWrapperResponse response, String[] queryParams, boolean isPost) throws Exception {
            return AppBootstrapBean.processPostLoad(request, response, queryParams, isPost);
        }
    },

    ;

    /*
     * Static Variables and Functions
     */
    private static final Logger s_logger = Logger.getInstance(WebsiteActions.class);

    public static WebsiteActions getUIAction(String action) {
        s_logger.info("WebsiteAction: " + action);

        if (action == null || action.length() < 1) {
            return HOME;
        }
        WebsiteActions[] actions = WebsiteActions.values();
        for (int i = 0; i < actions.length; i++) {
            if (actions[i].getDisplayName().equalsIgnoreCase(action)) {
                return actions[i];
            }
        }
        throw new RuntimeException("Unknown action");
    }

    public static WebsiteActions deserializeUIAction(String action) {
        if (action == null || action.length() < 1) {
            return null;
        }

        WebsiteActions[] actions = WebsiteActions.values();
        for (int i = 0; i < actions.length; i++) {
            if (actions[i].getDisplayName().equalsIgnoreCase(action)) {
                return actions[i];
            }
        }
        return null;
    }

    /*
     * Object Variables and Functions
     */
    private String          m_displayName;
    private List<UserRoles> m_defaultAllowedRoles  = new ArrayList<UserRoles>();
    private WebsitePages    m_defaultUIPage        = null;
    private boolean         m_requiresCsrfCheck    = false;
    private boolean         m_storeAccessEntry     = false;
    private boolean         m_isAllowedOnDashBoard = false;

    WebsiteActions(String displayName, List<UserRoles> allowedRoles, boolean requiresCsrfCheck,
            boolean storeAccessEntry, boolean isAllowedOnDashBoard) {
        m_displayName = displayName;
        m_defaultAllowedRoles = allowedRoles;
        m_requiresCsrfCheck = requiresCsrfCheck;
        m_storeAccessEntry = storeAccessEntry;
        m_isAllowedOnDashBoard = isAllowedOnDashBoard;
    }

    WebsiteActions(String displayName, WebsitePages page, List<UserRoles> allowedRoles, boolean isAllowedOnDashBoard) {
        this(displayName, allowedRoles, false, false, false);
        m_defaultUIPage = page;
        m_isAllowedOnDashBoard = isAllowedOnDashBoard;
    }

    public String getDisplayName() {
        return m_displayName;
    }

    public boolean isStoreAccessEntry() {
        return m_storeAccessEntry;
    }

    public boolean requiresCsrfCheck() {
        return m_requiresCsrfCheck;
    }

    public boolean isAllowedOnDashBoard() {
        return m_isAllowedOnDashBoard;
    }

    public WebsitePages executeAction(ZilliousSecurityWrapperRequest request, ZilliousSecurityWrapperResponse response,
            String[] queryParams, boolean isPost) throws Exception {
        if (s_logger.isDebugEnabled()) {
            StringBuilder buf = new StringBuilder();
            buf.append("Executing action: ").append(m_displayName).append(", params{");
            for (int i = 0; (queryParams != null) && (i < queryParams.length); i++) {
                if (i != 0) {
                    buf.append(",");
                }
                buf.append(queryParams[i]);
            }
            buf.append("}");
            s_logger.debug(buf.toString());
        }
        return execute(request, response, queryParams, isPost);
    }

    public String getActionURL(ZilliousSecurityWrapperRequest request, ZilliousSecurityWrapperResponse response) {
        return getActionURL(request, response, null, false);
    }

    public String getActionURL(ZilliousSecurityWrapperRequest request, ZilliousSecurityWrapperResponse response,
            String[] queryParams) {
        return getActionURL(request, response, queryParams, false);
    }

    public String getActionURL(ZilliousSecurityWrapperRequest request, ZilliousSecurityWrapperResponse response,
            String[] queryParams, boolean forceSSL) {
        StringBuilder b = new StringBuilder(128);
        b.append(forceSSL ? "https://" : "http://").append(request.getServerName())
                .append(WebsiteNavigation.getServletPath());

        if (requiresCsrfCheck()) {
            b.append(getCsrfUrlToken(request));
        }

        b.append("/").append(getDisplayName());
        if (queryParams != null) {
            for (int i = 0; i < queryParams.length; i++) {
                b.append("/").append(queryParams[i]);
            }
        }

        // Removed Encode URL for security reasons
        // if (response != null) {
        // return response.encodeURL(url);
        // } else {
        return b.toString();
    }

    public static String getCsrfUrlToken(ZilliousSecurityWrapperRequest request) {
        String urlToken = SessionStore.getCsrfUrlToken(request);
        if (urlToken == null) {
            return "";
        } else {
            return "/" + WebsiteNavigation.CSRF_URL_PREPEND + SessionStore.getCsrfUrlToken(request);
        }
    }

    public String getCsrfPostInputEntry(ZilliousSecurityWrapperRequest request) {
        if (requiresCsrfCheck()) {
            String csrfPostToken = SessionStore.getCsrfPostToken(request);
            if (csrfPostToken != null) {
                return "<input type=\"hidden\" name=\"" + WebsiteNavigation.CSRF_POST_PARAM + "\" value=\""
                        + csrfPostToken + "\" \\>";
            }
        }
        return "";
    }

    public String getCsrfPostParameter(ZilliousSecurityWrapperRequest request) {
        if (requiresCsrfCheck()) {
            String csrfPostToken = SessionStore.getCsrfPostToken(request);
            if (csrfPostToken != null) {
                return "&" + WebsiteNavigation.CSRF_POST_PARAM + "=" + csrfPostToken;
            }
        }
        return "";
    }

    public boolean isRoleDefaultAllowed(UserRoles role) {
        for (int i = 0; i < m_defaultAllowedRoles.size(); i++) {
            if (m_defaultAllowedRoles.add(role)) {
                return true;
            }
        }
        return false;
    }

    protected WebsitePages execute(ZilliousSecurityWrapperRequest request, ZilliousSecurityWrapperResponse response,
            String[] queryParams, boolean isPost) throws Exception {
        if (m_defaultUIPage != null) {
            return m_defaultUIPage;
        } else {
            throw new RuntimeException("Cannot rely on default execute when default page is not specified.");
        }
    }

    public String getServletPath() {
        return WebsiteNavigation.getServletPath();
    }
}
