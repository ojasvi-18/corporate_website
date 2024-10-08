package com.zillious.corporate_website.ui.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.zillious.corporate_website.i18n.Country;
import com.zillious.corporate_website.i18n.Language;
import com.zillious.corporate_website.ui.security.ZilliousSecurityWrapperRequest;
import com.zillious.corporate_website.ui.security.ZilliousSecurityWrapperResponse;
import com.zillious.corporate_website.ui.session.SessionStore;
import com.zillious.corporate_website.ui.util.ZilliousLocator;

/**
 * @author Nishant
 * 
 */
public class GeoLocateFilter implements Filter {
    private static Logger s_logger = Logger.getLogger(GeoLocateFilter.class);

    @Override
    public void destroy() {
        // TODO Auto-generated method stub

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException,
            ServletException {
        if (request instanceof HttpServletRequest) {
            ZilliousSecurityWrapperRequest zReq = (ZilliousSecurityWrapperRequest) request;
            ZilliousSecurityWrapperResponse zRes = (ZilliousSecurityWrapperResponse) response;
            Country country = Country.getCountryFromCode(SessionStore.getCountryFromSession(zReq));
            if (country == null) {
                country = SessionStore.getCountryCookie(zReq);
                if (country == null) {
                    String clientIP = zReq.getRemoteAddr();
                    country = ZilliousLocator.getCountry(clientIP);
                }
                s_logger.info("Country obtained from the locator: " + country);
                Language lang = Language.getLanguageFromCodeWithPropertyFileCheck(SessionStore
                        .getLanguageFromSession(zReq));
                if (lang == null) {
                    lang = SessionStore.getLanguageCookie(zReq);
                    if (lang == null && country != null) {
                        lang = country.getDefaultLanguage();
                    }
                    if (lang == null) {
                        lang = Language.ENGLISH;
                    }
                }

                SessionStore.setCountryInSession(zReq, zRes, country);
                SessionStore.setCountryCookie(zRes, country);
                SessionStore.setLanguageCookie(zRes, lang);
                SessionStore.setLanguageInSession(zReq, zRes, lang);
            }
            filterChain.doFilter(request, response);
        }
    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {

    }

}
