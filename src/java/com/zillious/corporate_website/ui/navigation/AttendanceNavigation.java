/**
 * 
 */
package com.zillious.corporate_website.ui.navigation;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zillious.corporate_website.app.WebsiteApplication;
import com.zillious.corporate_website.logger.Logger;
import com.zillious.corporate_website.ui.beans.SecurityBean;
import com.zillious.corporate_website.ui.exception.AccessException;
import com.zillious.corporate_website.ui.exception.AccessExceptionType;
import com.zillious.corporate_website.ui.security.ZilliousSecurityWrapperRequest;
import com.zillious.corporate_website.ui.security.ZilliousSecurityWrapperResponse;
import com.zillious.corporate_website.utils.StringUtility;

/**
 * @author nishant.gupta
 *
 */
public class AttendanceNavigation extends HttpServlet {
    private static final long   serialVersionUID = 4682864795590991754L;

    private static final Logger s_logger         = Logger.getInstance(AttendanceNavigation.class);

    private static String       s_contextURL     = "";
    private static String       s_servletURL     = "/attn";

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
            String uri = removeBaseFromURI(request.getRequestURI(), getServletPath());

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

            // HTTPS check
            if (!WebsiteApplication.isBypassHttpsCheck() && !request.isSecure()) {
                throw new AccessException(AccessExceptionType.HTTPS_REQUIRED);
            }

            // Execute action
            WebsitePages page = actionObj.executeAction(request, response, queryParams, isPost);
            dispatchRequest(request, response, page);

        } catch (Exception e) {
            if (action == null) {
                s_logger.error("Error in executing request.", e);
            } else {
                s_logger.error("Error in executing request with action: " + action, e);
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

    protected void dispatchRequest(ZilliousSecurityWrapperRequest request, ZilliousSecurityWrapperResponse response,
            WebsitePages page) throws Exception {
        if (page == WebsitePages.NO_PAGE) {
            // Do nothing; Maybe rename it to NO_ACTION? or DEV_NULL :)
            return;
        } else {
            throw new WebsiteException(WebsiteExceptionType.CANNOTRETURNPAGE);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPostGet(req, resp, false);
    }

}
