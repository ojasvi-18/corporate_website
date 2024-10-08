package com.zillious.commons.db;

import java.sql.Connection;
import java.sql.PreparedStatement;

import org.apache.log4j.Logger;

import com.zillious.corporate_website.data.PostLoadDTO;
import com.zillious.corporate_website.db.WebsiteDBConnector;

public class PostLoadDB {
    private static WebsiteDBConnector s_connector = WebsiteDBConnector.getStaticInstance();
    private static Logger             s_logger    = Logger.getLogger(PostLoadDB.class);

    /**
     * Store logs into database.
     * 
     * @param pl
     */
    public static void storePostLoadLogs(PostLoadDTO pl) {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = s_connector.getConnection(true);
            stmt = conn.prepareStatement(
                    "insert into app_bootstrap_post_load (client_ip,app_logs,trace_logs) values (?,?,?)");

            int i = 1;
            s_connector.setString(stmt, i++, pl.getClientIP(), 59);
            s_connector.setString(stmt, i++, pl.getAppLogs(), 60000);
            s_connector.setString(stmt, i++, pl.getTraceLogs(), 60000);
            stmt.executeUpdate();

        } catch (Exception e) {
            s_logger.error("Error while storing post load logs:", e);

        } finally {
            s_connector.closeAll(conn, stmt, null, true);
        }
    }
}
