package com.zillious.corporate_website.ui.beans;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.owasp.esapi.filters.SecurityWrapperRequest;
import org.owasp.esapi.filters.SecurityWrapperResponse;

import com.zillious.corporate_website.ui.security.ZilliousSecurityWrapperRequest;
import com.zillious.corporate_website.ui.security.ZilliousSecurityWrapperResponse;

public class SecurityBean {
    public static ZilliousSecurityWrapperRequest getRequest(HttpServletRequest request) {
        if (request instanceof SecurityWrapperRequest) {
            return (ZilliousSecurityWrapperRequest) request;
        } else {
            return new ZilliousSecurityWrapperRequest(request);
        }
    }

    public static ZilliousSecurityWrapperResponse getResponse(HttpServletResponse response) {
        if (response instanceof SecurityWrapperResponse) {
            return (ZilliousSecurityWrapperResponse) response;
        } else {
            return new ZilliousSecurityWrapperResponse(response);
        }
    }
}
