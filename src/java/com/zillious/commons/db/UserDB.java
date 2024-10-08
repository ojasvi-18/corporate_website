package com.zillious.commons.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.zillious.corporate_website.data.AttendanceFromDeviceDTO;
import com.zillious.corporate_website.db.WebsiteDBConnector;
import com.zillious.corporate_website.portal.ui.model.User;
import com.zillious.corporate_website.ui.navigation.WebsiteException;
import com.zillious.corporate_website.ui.navigation.WebsiteExceptionType;
import com.zillious.corporate_website.utils.DateUtility;
import com.zillious.corporate_website.utils.NumberUtility;
import com.zillious.corporate_website.utils.StringUtility;

/**
 * @author nishant.gupta
 *
 */
public class UserDB {
    private static WebsiteDBConnector s_connector = WebsiteDBConnector.getStaticInstance();
    private static Logger             s_logger    = Logger.getLogger(UserDB.class);

    public static User authenticateUser(Connection conn, String email, String password) throws Exception {
        User user = UserDB.getUserByEmail(conn, email);
        if (user == null || !User.authenthicateUser(user.getUserId(), user.getAuthToken(), password)) {
            throw new WebsiteException(WebsiteExceptionType.USER_AUTH_FAILED);
        }
        if (!user.isEnabled()) {
            throw new WebsiteException(WebsiteExceptionType.USER_DISABLED);
        }
        return user;
    }

    private static final String SQL_SELECT_BY_EMAIL = "select * from website_users where LOWER(email)=LOWER(?) limit 1";

    public static User getUserByEmail(Connection conn, String email) throws Exception {
        WebsiteDBConnector aCon = WebsiteDBConnector.getStaticInstance();
        boolean isCloseConnection = false;
        PreparedStatement stmt = null;
        ResultSet result = null;
        try {
            if (conn == null) {
                isCloseConnection = true;
                conn = aCon.getReadOnlyConnection();
            }
            stmt = conn.prepareStatement(SQL_SELECT_BY_EMAIL);
            aCon.setString(stmt, 1, email);
            result = stmt.executeQuery();
            if (result.next()) {
                User user = new User(result);
                return user;
            } else {
                return null;
            }

        } finally {
            aCon.closeAll(conn, stmt, result, isCloseConnection);
        }
    }

    private static final String SQL_ADD_ATTENDANCE = "insert ignore into attendance_records(dev_user_id, gen_ts, device_id, ipaddress) values (?,?,?,?)";

    /**
     * @param recordsToUpload
     * @param ipAddress
     * @return
     */
    public static int addAttendance(List<AttendanceFromDeviceDTO> recordsToUpload, String ipAddress) {
        Connection conn = null;
        PreparedStatement stmt = null;
        int totalCount = 0;
        int batchCount = 0;
        try {
            conn = s_connector.getConnectionForBatchInsert();
            stmt = conn.prepareStatement(SQL_ADD_ATTENDANCE);
            for (AttendanceFromDeviceDTO dto : recordsToUpload) {
                int i = 1;
                int deviceUserId = NumberUtility.parsetIntWithDefaultOnErr(dto.getUserId(), -1);
                Integer websiteUserID = null;
                stmt.setInt(i++, websiteUserID == null ? deviceUserId : websiteUserID);
                Date genTS = DateUtility.parseDateInDDMMYYHHMMSSNullIfError(dto.getDate() + " " + dto.getTime());
                s_connector.setTimestamp(stmt, i++, genTS);
                s_connector
                        .setString(stmt, i++, StringUtility.pad(
                                String.valueOf(NumberUtility.parsetIntWithDefaultOnErr(dto.getDeviceId(), 0)), "0", 2,
                                true), 2);
                s_connector.setString(stmt, i++, ipAddress);
                stmt.addBatch();
                batchCount++;

                if (batchCount == 100) {
                    stmt.executeBatch();
                    totalCount += batchCount;
                    batchCount = 0;
                }
            }

            if (batchCount > 0) {
                stmt.executeBatch();
                totalCount += batchCount;
                batchCount = 0;
            }
            s_connector.commit(conn, true);
        } catch (Exception e) {
            s_logger.error("Error while saving user info into the database, lineNumber:" + (totalCount + batchCount), e);
            return -1;
        } finally {
            s_connector.closeAll(conn, stmt, null, true);
        }

        return totalCount;

    }

    public static Set<String> getDeviceUserIds() {
        Set<String> deviceUserIds = null;
        WebsiteDBConnector aCon = WebsiteDBConnector.getStaticInstance();
        boolean isCloseConnection = false;
        PreparedStatement stmt = null;
        ResultSet result = null;
        Connection conn = null;
        try {
            conn = aCon.getReadOnlyConnection();
            isCloseConnection = true;
            stmt = conn.prepareStatement("select distinct device_id from website_users");
            result = stmt.executeQuery();
            if (result.isBeforeFirst()) {
                deviceUserIds = new HashSet<String>();
            }
            while (result.next()) {
                deviceUserIds.add(result.getString("device_id"));
            }

            return deviceUserIds;

        } catch (Exception e) {
            s_logger.error("Error in getting device user ids in the website_users table", e);
        } finally {
            aCon.closeAll(conn, stmt, result, isCloseConnection);
        }
        return null;
    }
}
