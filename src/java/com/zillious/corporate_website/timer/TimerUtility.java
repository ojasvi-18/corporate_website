package com.zillious.corporate_website.timer;

import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.ServletConfig;

import org.apache.log4j.Logger;

import com.zillious.corporate_website.ui.beans.AudienceBean;
import com.zillious.corporate_website.utils.DateUtility;

public class TimerUtility {
    private static Logger    s_logger           = Logger.getLogger(TimerUtility.class);
    private static Timer     m_timer            = null;
    public static final long ONE_HOUR_IN_MILLIS = 1000 * 60 * 60;
    public static final long ONE_DAY_IN_MILLIS  = 24 * ONE_HOUR_IN_MILLIS;

    public static void initialize(ServletConfig config) {
        String tempServer = null;
        if ((config != null && config.getServletContext() != null)) {
            tempServer = config.getServletContext().getInitParameter("serverName");
        } else {
            tempServer = "https://www.zillious.com";
        }

        s_logger.debug("Timer utility initializing");

        final String serverName = tempServer;
        s_logger.debug("serverName:" + serverName);

        if (m_timer == null) {
            m_timer = new Timer("Website Timer", true);
        }

        TimerTask task = new TimerTask() {
            boolean isFirstRun = true;

            @Override
            public void run() {
                AudienceBean.sendContactInquiryReport(serverName);
                AudienceBean.sendNewsletterSubscriptionReport(isFirstRun);
                AudienceBean.deactivatePopupRequests();
                isFirstRun = false;
            }
        };

        m_timer.scheduleAtFixedRate(task, DateUtility.getDateAfterNowWithHourMinuteSecond(16, 13, 0), ONE_DAY_IN_MILLIS);
        // m_timer.scheduleAtFixedRate(task,
        // DateUtility.getDateAfterNowWithHourMinuteSecond(15, 43, 0), 60 *
        // 1000);
    }

    public static void shutdown() {
        if (m_timer != null) {
            try {
                m_timer.cancel();
                m_timer.purge();
                s_logger.info("Timer to download files from FTP and reload cache stopped");
            } catch (Throwable t) {
                s_logger.error("Error in canceling timer task.", t);
            }
            m_timer = null;
        }
    }
}
