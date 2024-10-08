package com.zillious.corporate_website.portal.ui.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.JsonObject;
import com.zillious.corporate_website.portal.ui.UserRoles;
import com.zillious.corporate_website.portal.ui.model.User;
import com.zillious.corporate_website.portal.ui.navigation.PortalActions;
import com.zillious.corporate_website.portal.utility.JsonUtility;
import com.zillious.corporate_website.ui.beans.SecurityBean;
import com.zillious.corporate_website.ui.navigation.WebsiteException;
import com.zillious.corporate_website.ui.navigation.WebsiteExceptionType;
import com.zillious.corporate_website.ui.navigation.WebsiteNavigation;
import com.zillious.corporate_website.ui.security.ZilliousSecurityWrapperRequest;
import com.zillious.corporate_website.ui.session.SessionStore;

public class RestfulInterceptor implements HandlerInterceptor {

    private static final Logger s_logger = Logger.getLogger(RestfulInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest origRequest, HttpServletResponse origResponse, Object handler)
            throws Exception {
        s_logger.debug("in preHandle Restful interceptor" + origRequest.getRequestURI());

        ZilliousSecurityWrapperRequest zilliousRequest = SecurityBean.getRequest(origRequest);

        JsonObject responseObj = new JsonObject();

        try {

            String requestURI = zilliousRequest.getRequestURI();

            // String[] requestTokens =
            // StringUtility.splitNullForEmpty(actionUri, "/");

            // String servletName = requestTokens[0];

            String servletPath = WebsiteNavigation.getSprinServletPath();
            if (!requestURI.startsWith(servletPath)) {
                throw new WebsiteException(WebsiteExceptionType.INVALID_REQUEST);
            }

            String actionURI = requestURI.substring(servletPath.length());

            s_logger.debug("action uri: " + actionURI);

            PortalActions portalAction = PortalActions.getPortalActionByDisplayName(actionURI);
            s_logger.info("Portal action : " + portalAction);

            if (portalAction == null) {
                throw new WebsiteException(WebsiteExceptionType.INVALID_REQUEST);
            }

            User loggedInUser = SessionStore.getLoggedInUser(zilliousRequest);

            if (loggedInUser != null) {

                UserRoles user_role = loggedInUser.getUserRole();
                if (portalAction.isAllowedForRole(user_role)) {
                    return true;
                } else {
                    JsonUtility.addLogout(responseObj);
                    origResponse.setContentType("application/json");
                    origResponse.getWriter().write(responseObj.toString());
                    return false;
                }
            } else {
                if (portalAction.isAllowedForRole(UserRoles.NOT_LOGGED_IN)) {
                    return true;
                } else {
                    JsonUtility.addLogout(responseObj);
                    origResponse.setContentType("application/json");
                    origResponse.getWriter().write(responseObj.toString());
                    return false;
                }
            }
        } catch (Exception e) {
            s_logger.debug("in preHandle PortalInterceptor", e);
            JsonUtility.addLogout(responseObj);
            origResponse.setContentType("application/json");
            origResponse.getWriter().write(responseObj.toString());
            return false;
        }
    }

    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelView) throws Exception {
        s_logger.debug("in postHandle PortalInterceptor");
    }

    public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
            throws Exception {
        s_logger.debug("in afterCompletion PortalInterceptor");
    }

}
