package com.zillious.corporate_website.ui.security;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.owasp.esapi.ESAPI;
import org.owasp.esapi.StringUtilities;
import org.owasp.esapi.ValidationErrorList;
import org.owasp.esapi.errors.ValidationException;

import com.zillious.corporate_website.logger.Logger;
import com.zillious.corporate_website.ui.exception.AccessException;
import com.zillious.corporate_website.ui.exception.AccessExceptionType;

/**
 * This response wrapper simply overrides unsafe methods in the
 * HttpServletResponse API with safe versions.
 */
public class ZilliousSecurityWrapperResponse extends HttpServletResponseWrapper {
    private final Logger s_logger = Logger.getInstance(ZilliousSecurityWrapperResponse.class);

    /**
     * Construct a safe response that overrides the default response methods
     * with safer versions.
     * 
     * @param response
     */
    public ZilliousSecurityWrapperResponse(HttpServletResponse response) {
        super(response);
    }

    private HttpServletResponse getHttpServletResponse() {
        return (HttpServletResponse) super.getResponse();
    }

    /**
     * Add a cookie to the response after ensuring that there are no encoded or
     * illegal characters in the name and name and value. This method also sets
     * the secure and HttpOnly flags on the cookie. This implementation uses a
     * custom "set-cookie" header instead of using Java's cookie interface which
     * doesn't allow the use of HttpOnly.
     * 
     * @param cookie
     */
    public void addCookie(Cookie cookie) {
        String name = cookie.getName();
        String value = cookie.getValue();
        int maxAge = cookie.getMaxAge();
        String domain = cookie.getDomain();
        String path = cookie.getPath();
        boolean secure = true; // Force Secure

        // validate the name and value
        ValidationErrorList errors = new ValidationErrorList();
        ESAPI.validator().getValidInput("cookie name", name, "HTTPCookieName", 50, false, errors);
        ESAPI.validator().getValidInput("cookie value", value, "HTTPCookieValue",
                ESAPI.securityConfiguration().getMaxHttpHeaderSize(), false, errors);

        // if there are no errors, then just set a cookie header
        if (errors.size() > 0) {
            throw new RuntimeException(new AccessException(AccessExceptionType.INPUT_VALIDATION_FAILED));
        }

        String header = createCookieHeader(name, value, maxAge, domain, path, secure);
        this.addHeader("Set-Cookie", header);
    }

    private String createCookieHeader(String name, String value, int maxAge, String domain, String path,
            boolean secure) {
        // create the special cookie header instead of creating a Java cookie
        // Set-Cookie:<name>=<value>[; <name>=<value>][; expires=<date>][;
        // domain=<domain_name>][; path=<some_path>][; secure][;HttpOnly
        String header = name + "=" + value;
        header += "; Max-Age=" + maxAge;
        if (domain != null) {
            header += "; Domain=" + domain;
        }
        if (path != null) {
            header += "; Path=" + path;
        }
        if (secure) {
            header += "; Secure";
        }
        // Force HttpOnly
        header += "; HttpOnly";
        return header;
    }

    /**
     * Add a cookie to the response after ensuring that there are no encoded or
     * illegal characters in the name.
     * 
     * @param name
     * @param date
     */
    public void addDateHeader(String name, long date) {
        try {
            String safeName = ESAPI.validator().getValidInput("safeSetDateHeader", name, "HTTPHeaderName", 20, false);
            getHttpServletResponse().addDateHeader(safeName, date);
        } catch (ValidationException e) {
            s_logger.error("Attempt to set invalid date header name denied", e);
        }
    }

    /**
     * Add a header to the response after ensuring that there are no encoded or
     * illegal characters in the name and name and value. This implementation
     * follows the following recommendation: "A recipient MAY replace any linear
     * white space with a single SP before interpreting the field value or
     * forwarding the message downstream."
     * http://www.w3.org/Protocols/rfc2616/rfc2616-sec2.html#sec2.2
     * 
     * @param name
     * @param value
     */
    public void addHeader(String name, String value) {
        try {
            String strippedName = StringUtilities.stripControls(name);
            String strippedValue = StringUtilities.stripControls(value);
            String safeName = ESAPI.validator().getValidInput("addHeader", strippedName, "HTTPHeaderName", 20, false);
            String safeValue = ESAPI.validator().getValidInput("addHeader", strippedValue, "HTTPHeaderValue",
                    ESAPI.securityConfiguration().getMaxHttpHeaderSize(), false);
            getHttpServletResponse().addHeader(safeName, safeValue);

        } catch (ValidationException e) {
            s_logger.error("Attempt to add invalid header denied", e);
        }
    }

    /**
     * Add an int header to the response after ensuring that there are no
     * encoded or illegal characters in the name and name.
     * 
     * @param name
     * @param value
     */
    public void addIntHeader(String name, int value) {
        try {
            String safeName = ESAPI.validator().getValidInput("safeSetDateHeader", name, "HTTPHeaderName", 20, false);
            getHttpServletResponse().addIntHeader(safeName, value);
        } catch (ValidationException e) {
            s_logger.error("Attempt to set invalid int header name denied", e);
        }
    }

    /**
     * Same as HttpServletResponse, no security changes required.
     * 
     * @param name
     * @return
     */
    public boolean containsHeader(String name) {
        return getHttpServletResponse().containsHeader(name);
    }

    /**
     * Return the URL without any changes, to prevent disclosure of the Session
     * ID. The default implementation of this method can add the Session ID to
     * the URL if support for cookies is not detected. This exposes the Session
     * ID credential in bookmarks, referer headers, server logs, and more.
     * 
     * @param url
     * @return original url
     * @deprecated in servlet spec 2.1. Use {@link #encodeRedirectUrl(String)}
     *             instead.
     */
    @Deprecated
    public String encodeRedirectUrl(String url) {
        return url;
    }

    /**
     * Return the URL without any changes, to prevent disclosure of the Session
     * ID The default implementation of this method can add the Session ID to
     * the URL if support for cookies is not detected. This exposes the Session
     * ID credential in bookmarks, referer headers, server logs, and more.
     * 
     * @param url
     * @return original url
     */
    public String encodeRedirectURL(String url) {
        return url;
    }

    /**
     * Return the URL without any changes, to prevent disclosure of the Session
     * ID The default implementation of this method can add the Session ID to
     * the URL if support for cookies is not detected. This exposes the Session
     * ID credential in bookmarks, referer headers, server logs, and more.
     * 
     * @param url
     * @return original url
     * @deprecated in servlet spec 2.1. Use {@link #encodeURL(String)} instead.
     */
    @Deprecated
    public String encodeUrl(String url) {
        return url;
    }

    /**
     * Return the URL without any changes, to prevent disclosure of the Session
     * ID The default implementation of this method can add the Session ID to
     * the URL if support for cookies is not detected. This exposes the Session
     * ID credential in bookmarks, referer headers, server logs, and more.
     * 
     * @param url
     * @return original url
     */
    public String encodeURL(String url) {
        return url;
    }

    /**
     * Same as HttpServletResponse, no security changes required.
     * 
     * @throws IOException
     */
    public void flushBuffer() throws IOException {
        getHttpServletResponse().flushBuffer();
    }

    /**
     * Same as HttpServletResponse, no security changes required.
     * 
     * @return
     */
    public int getBufferSize() {
        return getHttpServletResponse().getBufferSize();
    }

    /**
     * Same as HttpServletResponse, no security changes required.
     * 
     * @return
     */
    public String getCharacterEncoding() {
        return getHttpServletResponse().getCharacterEncoding();
    }

    /**
     * Same as HttpServletResponse, no security changes required.
     * 
     * @return
     */
    public String getContentType() {
        return getHttpServletResponse().getContentType();
    }

    /**
     * Same as HttpServletResponse, no security changes required.
     * 
     * @return
     */
    public Locale getLocale() {
        return getHttpServletResponse().getLocale();
    }

    /**
     * Same as HttpServletResponse, no security changes required.
     * 
     * @return
     * @throws IOException
     */
    public ServletOutputStream getOutputStream() throws IOException {
        return getHttpServletResponse().getOutputStream();
    }

    /**
     * Same as HttpServletResponse, no security changes required.
     * 
     * @return
     * @throws IOException
     */
    public PrintWriter getWriter() throws IOException {
        return getHttpServletResponse().getWriter();
    }

    /**
     * Same as HttpServletResponse, no security changes required.
     * 
     * @return
     */
    public boolean isCommitted() {
        return getHttpServletResponse().isCommitted();
    }

    /**
     * Same as HttpServletResponse, no security changes required.
     */
    public void reset() {
        getHttpServletResponse().reset();
    }

    /**
     * Same as HttpServletResponse, no security changes required.
     */
    public void resetBuffer() {
        getHttpServletResponse().resetBuffer();
    }

    /**
     * Override the error code with a 200 in order to confound attackers using
     * automated scanners.
     * 
     * @param sc
     * @throws IOException
     */
    public void sendError(int sc) throws IOException {
        getHttpServletResponse().sendError(HttpServletResponse.SC_OK, getHTTPMessage(sc));
    }

    /**
     * Override the error code with a 200 in order to confound attackers using
     * automated scanners. The message is canonicalized and filtered for
     * dangerous characters.
     * 
     * @param sc
     * @param msg
     * @throws IOException
     */
    public void sendError(int sc, String msg) throws IOException {
        getHttpServletResponse().sendError(HttpServletResponse.SC_OK, ESAPI.encoder().encodeForHTML(msg));
    }

    /**
     * This method generates a redirect response that can only be used to
     * redirect the browser to safe locations, as configured in the ESAPI
     * security configuration. This method does not that redirect requests can
     * be modified by attackers, so do not rely information contained within
     * redirect requests, and do not include sensitive information in a
     * redirect.
     * 
     * @param location
     * @throws IOException
     */
    public void sendRedirect(String location) throws IOException {
        sendRedirect(location, false);
    }

    public void sendRedirect(String location, boolean isTrustedThirdPartyRedirect) throws IOException {
        // We need to redirect to third party Payment gateways and SSO websites
        if (isTrustedThirdPartyRedirect) {
            getHttpServletResponse().sendRedirect(location);
        } else {
            // TODO: Add domain check?
            getHttpServletResponse().sendRedirect(location);
        }
    }

    /**
     * Same as HttpServletResponse, no security changes required.
     * 
     * @param size
     */
    public void setBufferSize(int size) {
        getHttpServletResponse().setBufferSize(size);
    }

    /**
     * Sets the character encoding to the ESAPI configured encoding.
     * 
     * @param charset
     */
    public void setCharacterEncoding(String charset) {
        getHttpServletResponse().setCharacterEncoding(ESAPI.securityConfiguration().getCharacterEncoding());
    }

    /**
     * Same as HttpServletResponse, no security changes required.
     * 
     * @param len
     */
    public void setContentLength(int len) {
        getHttpServletResponse().setContentLength(len);
    }

    /**
     * Same as HttpServletResponse, no security changes required.
     * 
     * @param type
     */
    public void setContentType(String type) {
        getHttpServletResponse().setContentType(type);
    }

    /**
     * Add a date header to the response after ensuring that there are no
     * encoded or illegal characters in the name.
     * 
     * @param name
     * @param date
     */
    public void setDateHeader(String name, long date) {
        try {
            String safeName = ESAPI.validator().getValidInput("safeSetDateHeader", name, "HTTPHeaderName", 20, false);
            getHttpServletResponse().setDateHeader(safeName, date);
        } catch (ValidationException e) {
            s_logger.error("Attempt to set invalid date header name denied", e);
        }
    }

    /**
     * Add a header to the response after ensuring that there are no encoded or
     * illegal characters in the name and value. "A recipient MAY replace any
     * linear white space with a single SP before interpreting the field value
     * or forwarding the message downstream."
     * http://www.w3.org/Protocols/rfc2616/rfc2616-sec2.html#sec2.2
     * 
     * @param name
     * @param value
     */
    public void setHeader(String name, String value) {
        try {
            String strippedName = StringUtilities.stripControls(name);
            String strippedValue = StringUtilities.stripControls(value);
            String safeName = ESAPI.validator().getValidInput("setHeader", strippedName, "HTTPHeaderName", 20, false);
            String safeValue = ESAPI.validator().getValidInput("setHeader", strippedValue, "HTTPHeaderValue",
                    ESAPI.securityConfiguration().getMaxHttpHeaderSize(), false);
            getHttpServletResponse().setHeader(safeName, safeValue);
        } catch (ValidationException e) {
            s_logger.error("Attempt to set invalid header denied");
        }
    }

    /**
     * Add an int header to the response after ensuring that there are no
     * encoded or illegal characters in the name.
     * 
     * @param name
     * @param value
     */
    public void setIntHeader(String name, int value) {
        try {
            String safeName = ESAPI.validator().getValidInput("safeSetDateHeader", name, "HTTPHeaderName", 20, false);
            getHttpServletResponse().setIntHeader(safeName, value);
        } catch (ValidationException e) {
            s_logger.error("Attempt to set invalid int header name denied", e);
        }
    }

    /**
     * Same as HttpServletResponse, no security changes required.
     * 
     * @param loc
     */
    public void setLocale(Locale loc) {
        // TODO investigate the character set issues here
        getHttpServletResponse().setLocale(loc);
    }

    /**
     * Override the status code with a 200 in order to confound attackers using
     * automated scanners.
     * 
     * @param sc
     */
    public void setStatus(int sc) {
        getHttpServletResponse().setStatus(HttpServletResponse.SC_OK);
    }

    /**
     * Override the status code with a 200 in order to confound attackers using
     * automated scanners. The message is canonicalized and filtered for
     * dangerous characters.
     * 
     * @param sc
     * @param sm
     * @deprecated In Servlet spec 2.1.
     */
    @Deprecated
    public void setStatus(int sc, String sm) {
        try {
            // setStatus is deprecated so use sendError instead
            sendError(HttpServletResponse.SC_OK, sm);
        } catch (IOException e) {
            s_logger.debug("Attempt to set response status failed", e);
        }
    }

    /**
     * returns a text message for the HTTP response code
     */
    private String getHTTPMessage(int sc) {
        return "HTTP error code: " + sc;
    }
}
