package com.zillious.corporate_website.app;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletConfig;

import org.owasp.esapi.ESAPI;

import com.zillious.corporate_website.app.util.IPAddressValidator;
import com.zillious.corporate_website.db.WebsiteDBConnector;
import com.zillious.corporate_website.logger.Logger;
import com.zillious.corporate_website.timer.TimerUtility;
import com.zillious.corporate_website.utils.ConfigStore;
import com.zillious.corporate_website.utils.NumberUtility;
import com.zillious.corporate_website.utils.StringUtility;

public class WebsiteApplication {
    private static final Logger  s_logger             = Logger.getInstance(WebsiteApplication.class);

    // 0=undone,1=processing,2=success,3=failed
    private static Object        s_initLock           = new Object();
    private static int           s_initStatus         = 0;

    private static ServletConfig s_servletConfig      = null;

    private static int           s_serverId           = -1;
    private static boolean       s_isBypassHttpsCheck = false;
    private static String        s_serverProxyHost    = null;
    private static int           s_serverProxyPort    = -1;

    private static List<String>  s_ipAddress          = new ArrayList<String>();

    private WebsiteApplication() {
    }

    public static void initialize(ServletConfig config) {
        synchronized (s_initLock) {
            if (s_initStatus == 0) {
                s_logger.info("Intializing WebsiteApp...");
                s_initStatus = 1;
            } else {
                s_logger.info("CWebsiteApp Already Initialized.");
                return;
            }
        }
        try {
            if (config != null && config.getServletContext() != null) {
                s_servletConfig = config;

                s_serverId = NumberUtility.parsetIntWithDefaultOnErr(
                        config.getServletContext().getInitParameter("AppServer.Id"), -1);

                s_serverProxyHost = StringUtility.trimAndEmptyIsNull(config.getServletContext().getInitParameter(
                        "AppServer.ProxyHost"));
                s_serverProxyPort = NumberUtility.parsetIntWithDefaultOnErr(config.getServletContext()
                        .getInitParameter("AppServer.ProxyPort"), -1);

                s_isBypassHttpsCheck = "true".equalsIgnoreCase(config.getServletContext().getInitParameter(
                        "AppServer.BypassHttpsCheck"));

                String ipAddress = config.getServletContext().getInitParameter("Attendance.ipaddress");
                
                ESAPI.initialize("org.owasp.esapi.reference.DefaultSecurityConfiguration");
                
                s_logger.debug("ipaddress: " + ipAddress);
                if (ipAddress == null || ipAddress.trim().isEmpty()) {
                    s_ipAddress.add("127.0.0.1");
                } else {
                    IPAddressValidator validator = new IPAddressValidator();
                    String[] tokens = ipAddress.split(",", -1);
                    for (String token : tokens) {
                        token = token.trim();
                        if (validator.validate(token)) {
                            s_ipAddress.add(token);
                        } else {
                            s_logger.info("incorrect ip address: " + token);
                        }
                    }
                }
            }

            // Config Store
            ConfigStore.initialise();

            // Database
            WebsiteDBConnector.initialize();

            // Timer
            TimerUtility.initialize(config);

            synchronized (s_initLock) {
                s_initStatus = 2;
            }

            s_logger.info("Intialized WebsiteApp.");

        } catch (Exception e) {
            s_logger.error("Error in initializing WebsiteApp.", e);
            synchronized (s_initLock) {
                s_initStatus = 3;
            }
        }
    }

    public static void shutdown() {
        s_logger.info("Shutdown WebsiteApp.");
        TimerUtility.shutdown();
    }

    public static int getServerId() {
        return s_serverId;
    }

    public static ServletConfig getServletConfig() {
        return s_servletConfig;
    }

    public static boolean hasProxy() {
        return s_serverProxyPort > 0 && s_serverProxyHost != null;
    }

    public static String getProxyHost() {
        return s_serverProxyHost;
    }

    public static int getProxyPort() {
        return s_serverProxyPort;
    }

    public static boolean isBypassHttpsCheck() {
        return s_isBypassHttpsCheck;
    }

    public static boolean isValidIPAddress(String ipAddress) {
        if (s_ipAddress == null || s_ipAddress.isEmpty() || ipAddress == null) {
            return false;
        }
        return s_ipAddress.contains(ipAddress);
    }
}
