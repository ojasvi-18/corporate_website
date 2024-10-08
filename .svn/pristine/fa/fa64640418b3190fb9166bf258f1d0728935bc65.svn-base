package com.zillious.corporate_website.ui.navigation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.zillious.corporate_website.data.ContactDTO;
import com.zillious.corporate_website.db.WebsiteDBConnector;
import com.zillious.corporate_website.ui.resources.QueryTypes;
import com.zillious.corporate_website.utils.DateUtility;

/**
 * @author Nishant
 * 
 */
public class DBUtil {
    private static final String       SQL_INSERT_NEWSLETTERSUBSCRIPTION = "insert into newsletter_subscription(email, client_ip, gen_ts) values (?,?, NOW())";
    private static final String       SQL_INSERT_CONTACT_REQUEST        = "insert into contact_request(email, name, phone, message, querytype, client_ip, gen_ts) values (?,?,?,?,?,?, NOW())";
    private static Logger             s_logger                          = Logger.getLogger(DBUtil.class);
    private static WebsiteDBConnector s_staticInstance                  = WebsiteDBConnector.getStaticInstance();

    public static boolean addEmailToSubscriptionList(String email, String ipAddress) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        boolean isAlreadyExists = checkIfAlreadyExists(email);
        if (!isAlreadyExists) {
            try {
                conn = s_staticInstance.getConnection(false);
                stmt = conn.prepareStatement(SQL_INSERT_NEWSLETTERSUBSCRIPTION);
                s_staticInstance.setString(stmt, 1, email, 50);
                s_staticInstance.setString(stmt, 2, ipAddress, 15);
                s_logger.debug("Query to add the user to the newsletter email list:" + stmt);
                stmt.execute();
                conn.commit();
                return true;

            } catch (Exception e) {
                s_logger.error("Error while adding people to subscription list of the newsletter", e);
                throw new WebsiteException(WebsiteExceptionType.OTHER);
            } finally {
                s_staticInstance.closeAll(conn, stmt, null, true);
            }

        } else {
            throw new WebsiteException(WebsiteExceptionType.NEWSLETTER_SUBSCRIBED);
        }
    }

    private static boolean checkIfAlreadyExists(String email) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = s_staticInstance.getReadOnlyConnection();
            stmt = conn.prepareStatement("select * from newsletter_subscription where email=? limit 1");
            s_staticInstance.setString(stmt, 1, email);
            rs = stmt.executeQuery();
            if (rs.isBeforeFirst()) {
                return true;
            }
            return false;

        } catch (Exception e) {
            s_logger.error("Error while checking if email already exists", e);

        } finally {
            s_staticInstance.closeAll(conn, stmt, rs, true);
        }
        return true;

    }

    public static void addContactRequest(String email, String name, String phone, QueryTypes queryType,
            String contactMessage, String ipAddress) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        Date generatedDate = checkIfRequestAlreadyExists(email, queryType);

        if (generatedDate == null) {
            try {
                conn = s_staticInstance.getConnection(false);
                stmt = conn.prepareStatement(SQL_INSERT_CONTACT_REQUEST);
                int i = 1;
                s_staticInstance.setString(stmt, i++, email, 50);
                s_staticInstance.setString(stmt, i++, name, 50);
                s_staticInstance.setString(stmt, i++, phone, 20);
                s_staticInstance.setString(stmt, i++, contactMessage, 1024 * 1024);
                s_staticInstance.setIntDefaultNull(stmt, i++, queryType.getQueryId(), -1);
                s_staticInstance.setString(stmt, i++, ipAddress, 15);
                s_logger.debug("Query to add the contact request:" + stmt);
                stmt.execute();
                conn.commit();
            } catch (Exception e) {
                s_logger.error("Error while adding contact request", e);
                throw e;
            } finally {
                s_staticInstance.closeAll(conn, stmt, null, true);
            }
        } else {
            throw new WebsiteException(WebsiteExceptionType.DUPLICATE_CONTACT_REQUEST, generatedDate);
        }
    }

    /**
     * This api checks whether the same query contact requests have been
     * requested for more than 5 times in the last 48 hours
     * 
     * @param email
     * @return
     * @throws Exception
     */
    private static Date checkIfRequestAlreadyExists(String email, QueryTypes type) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = s_staticInstance.getReadOnlyConnection();
            stmt = conn
                    .prepareStatement("select gen_ts from contact_request where email=? and querytype = ? and ack=0");
            s_staticInstance.setString(stmt, 1, email);
            s_staticInstance.setIntDefaultNull(stmt, 2, type.getQueryId(), -1);
            rs = stmt.executeQuery();
            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    return s_staticInstance.getTimeStampAsDate(rs, "gen_ts");
                }
            }
            return null;
        } catch (Exception e) {
            s_logger.error(
                    "Error while checking if this query has been requested more than 5 times in the last 48 hours", e);
            throw e;
        } finally {
            s_staticInstance.closeAll(conn, stmt, rs, true);
        }
    }

    public static void ackContactReq(QueryTypes type, String email) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = s_staticInstance.getConnection(false);
            stmt = conn.prepareStatement("update contact_request set ack=1 where email=? and querytype=?");
            s_staticInstance.setString(stmt, 1, email);
            s_staticInstance.setIntDefaultNull(stmt, 2, type.getQueryId(), -1);
            stmt.execute();
            conn.commit();
        } catch (Exception e) {
            s_logger.error(
                    "Error while updating the status of the contact query", e);
            throw e;
        } finally {
            s_staticInstance.closeAll(conn, stmt, null, true);
        }
    }

    public static Calendar getLastTimeForDay(Date ticketingDate, boolean isToday) {
        Calendar cal = Calendar.getInstance();
        if (!isToday) {
            cal.setTime(ticketingDate);
        }
        // Subtracting 1 second from the start time of the next day
        cal.add(Calendar.DATE, 1);
        cal = removeTime(false, cal);
        cal.add(Calendar.MILLISECOND, -1);
        return cal;
    }

    public static Date removeTime(Date date, boolean isToday) {
        Calendar cal = Calendar.getInstance();
        if (!isToday) {
            cal.setTime(date);
        }
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public static Calendar removeTime(boolean isToday, Calendar date) {
        Calendar cal = Calendar.getInstance();
        if (!isToday) {
            cal.setTime(date.getTime());
        }
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal;
    }

    public static List<ContactDTO> fetchUnacknowledgedInquries() throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<ContactDTO> dtos = null;
        try {
            conn = s_staticInstance.getReadOnlyConnection();
            stmt = conn.prepareStatement("select * from contact_request where gen_ts <= NOW() and ack=0");
            rs = stmt.executeQuery();
            if (rs.isBeforeFirst()) {
                dtos = new ArrayList<ContactDTO>();
            }
            while (rs.next()) {
                String emailID = rs.getString("email");
                String name = rs.getString("name");
                String phone = rs.getString("phone");
                String message = rs.getString("message");
                QueryTypes type = QueryTypes.getQueryType(rs.getString("querytype"));
                String clientIP = rs.getString("client_ip");
                ContactDTO dto = new ContactDTO(emailID, name, phone, message, type, clientIP);
                dtos.add(dto);
            }
            return dtos;
        } catch (Exception e) {
            s_logger.error("Error while fetching the contact enquiry list from the database");
            throw e;
        } finally {
            s_staticInstance.closeAll(conn, stmt, rs, true);
        }
    }

    public static List<ContactDTO> fetchNewsletterSubscritionAlerts(boolean isFirstRun) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<ContactDTO> dtos = null;
        try {
            conn = s_staticInstance.getReadOnlyConnection();
            stmt = conn.prepareStatement("select * from newsletter_subscription where gen_ts <= NOW()"
                    + (!isFirstRun ? " and gen_ts >= ?" : ""));
            if (!isFirstRun) {
                s_staticInstance.setTimestamp(stmt, 1, DateUtility.getPreviousDaysDate(10, 0, 0));
            }
            rs = stmt.executeQuery();
            if (rs.isBeforeFirst()) {
                dtos = new ArrayList<ContactDTO>();
            }
            while (rs.next()) {
                String emailID = rs.getString("email");
                String clientIP = rs.getString("client_ip");
                ContactDTO dto = new ContactDTO(emailID, null, null, null, null, clientIP);
                dtos.add(dto);
            }
            return dtos;
        } catch (Exception e) {
            s_logger.error("Error while fetching the Newsletter subscription requests list from the database");
            throw e;
        } finally {
            s_staticInstance.closeAll(conn, stmt, rs, true);
        }
    }

    public static boolean addPopupContentRequest(String content, String ipAddress, Calendar startDate, Calendar endDate)
            throws WebsiteException {
        Connection conn = null;
        PreparedStatement stmt = null;
        boolean isAlreadyExists = checkIfAnyRequestExists();
        if (!isAlreadyExists) {
            try {
                conn = s_staticInstance.getConnection(false);
                stmt = conn
                        .prepareStatement("insert into popup_request(content, client_ip, start_time, end_time, is_active, gen_ts) values (?,?,?,?,?, NOW())");
                s_staticInstance.setString(stmt, 1, content, 102400);
                s_staticInstance.setString(stmt, 2, ipAddress, 15);
                s_staticInstance.setTimestamp(stmt, 3, startDate);
                s_staticInstance.setTimestamp(stmt, 4, endDate);
                s_staticInstance.setString(stmt, 5, "Y");
                s_logger.debug("Query to add the popup content request:" + stmt);
                stmt.execute();
                conn.commit();
                return true;

            } catch (Exception e) {
                s_logger.error("Error while adding people to subscription list of the newsletter", e);
                throw new WebsiteException(WebsiteExceptionType.OTHER);
            } finally {
                s_staticInstance.closeAll(conn, stmt, null, true);
            }

        } else {
            throw new WebsiteException(WebsiteExceptionType.ACTIVE_REQUEST_PRESENT);
        }
    }

    private static boolean checkIfAnyRequestExists() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = s_staticInstance.getReadOnlyConnection();
            stmt = conn.prepareStatement("select * from popup_request where is_active=? limit 1");
            s_staticInstance.setString(stmt, 1, "Y");
            rs = stmt.executeQuery();
            if (rs.isBeforeFirst()) {
                return true;
            }
            return false;

        } catch (Exception e) {
            s_logger.error("Error while checking if any active content already exists", e);
        } finally {
            s_staticInstance.closeAll(conn, stmt, rs, true);
        }
        return true;
    }

    public static void approveCurrentlyActivePopupRequest(boolean isApprove) {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = s_staticInstance.getConnection(false);
            if (!isApprove) {
                stmt = conn.prepareStatement("update popup_request set is_approved = 'N', is_active='N'");
            } else {
                stmt = conn.prepareStatement("update popup_request set is_approved = 'Y' where is_active='Y'");
            }
            stmt.executeUpdate();
            conn.commit();
        } catch (Exception e) {
            s_logger.error("Error while checking if any active content already exists", e);
        } finally {
            s_staticInstance.closeAll(conn, stmt, null, true);
        }

    }

    public static String getPopupContent() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = s_staticInstance.getReadOnlyConnection();
            stmt = conn
                    .prepareStatement("select content from popup_request where is_active='Y' and is_approved='Y' and start_time <= NOW() and end_time >= NOW()");
            rs = stmt.executeQuery();
            while (rs.next()) {
                return rs.getString("content");
            }
        } catch (Exception e) {
            s_logger.error("Error while checking if any active content already exists", e);
        } finally {
            s_staticInstance.closeAll(conn, stmt, rs, true);
        }
        return null;
    }

    /**
     * 
     * This API runs every day, and sets is_active value of the currently active
     * popup_requests to false, so that they do not appear on the home page
     */
    public static void deactivatePopupRequests() {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = s_staticInstance.getConnection(false);
            stmt = conn
                    .prepareStatement("update popup_request set is_approved = 'N', is_active='N' where start_time > NOW() or end_time < NOW()");
            stmt.executeUpdate();
            conn.commit();
        } catch (Exception e) {
            s_logger.error("Error while deactivating the active requests", e);
        } finally {
            s_staticInstance.closeAll(conn, stmt, null, true);
        }
    }

}
