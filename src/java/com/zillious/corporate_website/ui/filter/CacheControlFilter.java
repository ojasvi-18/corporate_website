package com.zillious.corporate_website.ui.filter;

import java.io.IOException;
import java.util.Date;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CacheControlFilter implements Filter {

    public void destroy() {
    }

    public void init(FilterConfig arg) throws ServletException {
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {

        if (request instanceof HttpServletRequest) {
            HttpServletResponse res = (HttpServletResponse) response;

            // Cache Header
            res.setDateHeader("Expires", new Date().getTime() + (1000 * 60 * 60 * 24 * 7));
            res.setHeader("Cache-Control", "public");
            res.setHeader("Pragma", "cache");
        }

        filterChain.doFilter(request, response);
    }
}
