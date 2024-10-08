package com.zillious.corporate_website.ui.filter;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zillious.corporate_website.logger.Logger;
import com.zillious.corporate_website.ui.security.ZilliousSecurityWrapperRequest;
import com.zillious.corporate_website.ui.security.ZilliousSecurityWrapperResponse;

public class SecurityFilter implements Filter {
    private static final Logger s_logger = Logger.getInstance(SecurityFilter.class);

    private Set<String>         m_iframeAllowedURI;

    public void destroy() {
    }

    public void init(FilterConfig arg0) throws ServletException {
        m_iframeAllowedURI = new HashSet<String>();
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
        if (request instanceof HttpServletRequest) {
            HttpServletRequest req = (HttpServletRequest) request;
            HttpServletResponse res = (HttpServletResponse) response;

            // Wrap Request and Response with Secure Wrappers
            ZilliousSecurityWrapperRequest secReq = new ZilliousSecurityWrapperRequest(req);
            ZilliousSecurityWrapperResponse secRes = new ZilliousSecurityWrapperResponse(res);

            // XFrame Header
            try {
                String reqUri = secReq.getRequestURI();
                if (!m_iframeAllowedURI.contains(reqUri)) {
                    secRes.addHeader("X-FRAME-OPTIONS", "DENY");
                }
            } catch (Exception e) {
                s_logger.error("Error in applying XFrame", e);
            }

            filterChain.doFilter(secReq, secRes);

        } else {
            filterChain.doFilter(request, response);
        }
    }
}
