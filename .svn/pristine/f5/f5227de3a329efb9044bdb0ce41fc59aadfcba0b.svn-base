package com.zillious.corporate_website.ui.beans;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.zillious.commons.db.PostLoadDB;
import com.zillious.corporate_website.data.PostLoadDTO;
import com.zillious.corporate_website.ui.navigation.WebsitePages;
import com.zillious.corporate_website.ui.security.ZilliousSecurityRequestType;
import com.zillious.corporate_website.ui.security.ZilliousSecurityWrapperRequest;
import com.zillious.corporate_website.ui.security.ZilliousSecurityWrapperResponse;

public class AppBootstrapBean {
    private static Logger s_logger = Logger.getLogger(AppBootstrapBean.class);

    public static WebsitePages processPostLoad(ZilliousSecurityWrapperRequest request,
            ZilliousSecurityWrapperResponse response, String[] queryParams, boolean isPost) {

        if (!isPost) {
            return WebsitePages.NO_PAGE;
        }

        try {
            // Hard coded security check
            String sString = request.getParameter("s", "security",
                    ZilliousSecurityRequestType.DEFAULT_SAFE_STRING_WITH_ANY_TEXT);
            if (queryParams == null || queryParams.length < 1
                    || !"dflSS39330030LKFfdfLFLDAPS23SPksd32489LKJFUYGdfsk".equals(queryParams[0])) {
                return addMessageToResponse(response, HttpServletResponse.SC_BAD_REQUEST, "Security Failed!");
            }

            // App Logs
            StringBuilder appLogsB = new StringBuilder();
            InputStream requestInpStrm = request.getInputStream();
            byte[] buffer = new byte[1024 * 4];
            int len = -1;
            while ((len = requestInpStrm.read(buffer)) != -1) {
                appLogsB.append(new String(buffer, 0, len));
            }

            // Trace Logs
            StringBuilder traceLogsB = new StringBuilder();

            // Remote Client IP
            String clientIP = request.getRemoteAddr();
            traceLogsB.append("Remote IP Address: " + clientIP);

            // Local Tracert
            BufferedReader in = null;
            try {
                traceLogsB.append("\r\n\r\nRemote Tracert:");
                Runtime r = Runtime.getRuntime();
                Process p = r.exec("tracert " + clientIP);
                in = new BufferedReader(new InputStreamReader(p.getInputStream()));

                String line;
                while ((line = in.readLine()) != null) {
                    traceLogsB.append("\r\n").append(line);
                }

            } catch (Throwable t) {
                traceLogsB.append("\r\nTracert: Exception in performing").append(t);
                if (in != null) {
                    try {
                        in.close();
                    } catch (Exception e) {
                    }
                    in = null;
                }
            }

            // Local Traceroute
            in = null;
            try {
                traceLogsB.append("\r\n\r\n\r\nRemote Traceroute:");
                Runtime r = Runtime.getRuntime();
                Process p = r.exec("traceroute " + clientIP);
                in = new BufferedReader(new InputStreamReader(p.getInputStream()));

                String line;
                while ((line = in.readLine()) != null) {
                    traceLogsB.append("\r\n").append(line);
                }

            } catch (Throwable t) {
                traceLogsB.append("\r\nTraceroute: Exception in performing").append(t);
                if (in != null) {
                    try {
                        in.close();
                    } catch (Exception e) {
                    }
                    in = null;
                }
            }

            // Store in Database
            String appLogs = appLogsB.toString();
            String traceLogs = traceLogsB.toString();

            try {
                PostLoadDTO pl = new PostLoadDTO(clientIP, appLogs, traceLogs);
                PostLoadDB.storePostLoadLogs(pl);
            } catch (Throwable t) {
                s_logger.error("Error in storing PostLoad logs", t);
            }

            return addMessageToResponse(response, HttpServletResponse.SC_OK, traceLogs);

        } catch (Exception e) {
            s_logger.error("Error while process PostLoad request", e);
        }

        return WebsitePages.NO_PAGE;
    }

    private static WebsitePages addMessageToResponse(ZilliousSecurityWrapperResponse response, int code,
            String errorMessage) throws IOException {
        response.setStatus(code);
        response.getOutputStream().write(errorMessage.getBytes());
        return WebsitePages.NO_PAGE;
    }
}
