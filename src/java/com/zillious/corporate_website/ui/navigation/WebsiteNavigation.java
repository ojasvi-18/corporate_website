package com.zillious.corporate_website.ui.navigation;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.zillious.corporate_website.app.WebsiteApplication;
import com.zillious.corporate_website.logger.Logger;
import com.zillious.corporate_website.ui.beans.SecurityBean;
import com.zillious.corporate_website.ui.beans.UIBean;
import com.zillious.corporate_website.ui.exception.AccessException;
import com.zillious.corporate_website.ui.exception.AccessExceptionType;
import com.zillious.corporate_website.ui.security.ZilliousSecurityRequestType;
import com.zillious.corporate_website.ui.security.ZilliousSecurityWrapperRequest;
import com.zillious.corporate_website.ui.security.ZilliousSecurityWrapperResponse;
import com.zillious.corporate_website.ui.session.SessionStore;
import com.zillious.corporate_website.utils.DateUtility;
import com.zillious.corporate_website.utils.StringUtility;

public class WebsiteNavigation extends HttpServlet {
    private static final Logger s_logger            = Logger.getInstance(WebsiteNavigation.class);
    private static final long   serialVersionUID    = 1L;

    public static final String  CSRF_URL_PREPEND    = "sec-";
    public static final String  CSRF_POST_PARAM     = "sec_token";

    private static String       s_contextURL        = "";
    private static String       s_servletURL        = "/nav";
    private static String       s_springServletName = "portal";
    private static String       s_servletSpringURL  = "/" + s_springServletName;
    private static final String WEBSITE_ACTION_ATTR = "web_action_attr";

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.GenericServlet#init(javax.servlet.ServletConfig)
     */
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        String ctxPath = config.getServletContext().getContextPath();
        if (ctxPath != null && !"/".equals(ctxPath)) {
            s_contextURL = ctxPath;
            s_servletURL = s_contextURL + s_servletURL;
        }
        s_logger.info("Context Path: " + s_contextURL);
        s_logger.info("Servlet Path: " + s_servletURL);

        WebsiteApplication.initialize(config);
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.GenericServlet#destroy()
     */
    @Override
    public void destroy() {
        WebsiteApplication.shutdown();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.
     * HttpServletRequest , javax.servlet.http.HttpServletResponse)
     */
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        doPostGet(request, response, true);
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.
     * HttpServletRequest , javax.servlet.http.HttpServletResponse)
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        doPostGet(request, response, false);
    }

    private void doPostGet(HttpServletRequest origRequest, HttpServletResponse origResponse, boolean isPost)
            throws IOException, ServletException {
        ZilliousSecurityWrapperRequest request = SecurityBean.getRequest(origRequest);
        ZilliousSecurityWrapperResponse response = SecurityBean.getResponse(origResponse);
        String action = null;
        WebsiteActions actionObj = null;
        try {
            String csrfRecieved = null;
            String uri = removeBaseFromURI(request.getRequestURI(), getServletPath());

            // Remove CSRF Token from URL
            if (uri.startsWith(CSRF_URL_PREPEND)) {
                int idx = uri.indexOf("/");
                if (idx == -1) {
                    csrfRecieved = uri.substring(CSRF_URL_PREPEND.length());
                    uri = "";
                } else {
                    csrfRecieved = uri.substring(CSRF_URL_PREPEND.length(), idx);
                    uri = uri.substring(idx);
                }
            }
            while (uri.startsWith("/")) {
                uri = uri.substring(1);
            }

            if (uri.startsWith("static")) {
                return;
            }
            // Query params
            String[] queryStringSplit = uri.split("/");
            String[] queryParams = null;
            if (queryStringSplit.length > 0) {
                action = StringUtility.trimAndEmptyIsNull(queryStringSplit[0]);
                if (queryStringSplit.length > 1) {
                    queryParams = new String[queryStringSplit.length - 1];
                    System.arraycopy(queryStringSplit, 1, queryParams, 0, queryParams.length);
                } else {
                    queryParams = new String[0];
                }
            } else {
                queryParams = new String[0];
            }

            // Action Object
            actionObj = WebsiteActions.getUIAction(action);

            // Internationalisation : Load resource bundle from locale in
            // cookie.If locale not set in cookie, then from
            // default whitelabel config.
            // TODO
            // Country countryLocale = SessionStore.getCountryCookie(request);
            // String country = countryLocale == null ? "IN" :
            // countryLocale.getCode();
            // Language languageLocale =
            // SessionStore.getLanguageCookie(request);
            // String language = languageLocale == null ? "EN" :
            // languageLocale.getCode();
            // I18NBean.addOrUpdateLocaleSettings(request, response, country,
            // language, false);

            // Set UIAction
            UIBean.setCurrentUIAction(request, actionObj);

            // CSRF check
            if (actionObj.requiresCsrfCheck()) {
                if (isPost && !ServletFileUpload.isMultipartContent(request)) {
                    // Currently nor post csrf token check for multi part
                    // content requests
                    String postToken = request.getParameter("sec_token", WebsiteNavigation.CSRF_POST_PARAM,
                            ZilliousSecurityRequestType.DEFAULT_SAFE_STRING);
                    if (!SessionStore.compareCsrfPostToken(request, postToken)) {
                        throw new AccessException(AccessExceptionType.CSRF_POST_VERIFICATION_FAILED);
                    }
                }
                if (!SessionStore.compareCsrfUrlToken(request, csrfRecieved)) {
                    throw new AccessException(AccessExceptionType.CSRF_URL_VERIFICATION_FAILED);
                }
            }

            // HTTPS check
            if (!WebsiteApplication.isBypassHttpsCheck() && !request.isSecure()) {
                throw new AccessException(AccessExceptionType.HTTPS_REQUIRED);
            }

            // Execute action
            request.setAttribute(WEBSITE_ACTION_ATTR, actionObj);
            WebsitePages page = actionObj.executeAction(request, response, queryParams, isPost);
            dispatchRequest(request, response, page);

        } catch (Exception e) {
            if (action == null) {
                s_logger.error("Error in executing request.", e);
            } else {
                s_logger.error("Error in executing request with action: " + action, e);
            }

            if (e instanceof AccessException) {
                // TODO
            }

            if (!response.isCommitted()) {
                try {
                    dispatchRequest(request, response, WebsitePages.ERROR_PAGE);
                } catch (Exception e2) {
                }
            } else {
                s_logger.error("Response committed could not do anything!");
            }
        }
    }

    private static String removeBaseFromURI(String uri, String base) {
        int idx = uri.indexOf(base);
        if (idx != -1) {
            uri = uri.substring(idx + base.length());
        }
        while (uri.startsWith("/")) {
            uri = uri.substring(1);
        }
        return uri;
    }

    public static String getContextPath() {
        return s_contextURL;
    }

    public static String getServletPath() {
        return s_servletURL;
    }

    public static String getSprinServletPath() {
        return s_servletSpringURL;
    }

    protected void dispatchRequest(ZilliousSecurityWrapperRequest request, ZilliousSecurityWrapperResponse response,
            WebsitePages page) throws Exception {
        if (page == WebsitePages.NO_PAGE) {
            // Do nothing; Maybe rename it to NO_ACTION? or DEV_NULL :)
            return;

        } else if (page == WebsitePages.REDIRECT_URL) {
            String url = StringUtility.trimAndEmptyIsNull(WebsitePages.getRedirectUrlInRequest(request));
            if (url == null) {
                throw new RuntimeException(
                        "Reditect URL UI Page returned without any redirect url being set using UIPages.setRedirectUrlInRequest");
            }
            setHtmlResponseContentEncoding(request, response);
            response.sendRedirect(response.encodeRedirectURL(url));
            return;
        } else if (page == WebsitePages.JSP_PAGE_PATH) {
            String jspPath = StringUtility.trimAndEmptyIsNull(WebsitePages.getJspPagePathInRequest(request));
            if (jspPath == null) {
                throw new RuntimeException(
                        "JSP_PAGE_PATH UI Page returned without any jsp path being set using UIPages.setJspPagePathInRequest");
            }
            setHtmlResponseContentEncoding(request, response);
            setResponseCacheHeader(request, response, false);
            getServletContext().getRequestDispatcher(jspPath).forward(request, response);
            return;
        }

        String path = page.getActualJspFilePath(request);
        if (path == null) {
            throw new AccessException(AccessExceptionType.PAGE_MISSING, page.name());
        }

        setHtmlResponseContentEncoding(request, response);
        setResponseCacheHeader(request, response, page.isCachePage());
        getServletContext().getRequestDispatcher(path).forward(request, response);
    }

    public static void setHtmlResponseContentEncoding(ZilliousSecurityWrapperRequest request,
            ZilliousSecurityWrapperResponse response) {
        response.setContentType("text/html;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
    }

    public static void setResponseCacheHeader(ZilliousSecurityWrapperRequest request,
            ZilliousSecurityWrapperResponse response, boolean isCachePage) {
        setResponseCacheHeader(request, response, (isCachePage) ? 3 : -1);
    }

    public static void setResponseCacheHeaderStaticContent(ZilliousSecurityWrapperRequest request,
            ZilliousSecurityWrapperResponse response) {
        response.setDateHeader("Expires", new Date().getTime() + (DateUtility.MILIS_IN_DAY * 3));
        response.setHeader("Cache-Control", "private");
        response.setHeader("Pragma", "cache");
    }

    public static void setResponseCacheHeader(ZilliousSecurityWrapperRequest request,
            ZilliousSecurityWrapperResponse response, int cacheAgeInDays) {
        response.setHeader("Expires", "Sat, 1-Jan-2000 12:00:00 GMT");
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        response.setHeader("Pragma", "no-cache");
    }

    public static String getRequestGetURL(ZilliousSecurityWrapperRequest request) {
        StringBuffer originalUrl = request.getRequestURL();
        if (request.getQueryString() != null) {
            originalUrl.append("?").append(request.getQueryString());
        }
        return originalUrl.toString();
    }

    public static WebsiteActions getUIActionBeingProcessed(ZilliousSecurityWrapperRequest request) {
        return (WebsiteActions) request.getAttribute(WEBSITE_ACTION_ATTR);
    }

    public static String getSpringServletName() {
        return s_springServletName;
    }
}
