/**
 * 
 */
package com.zillious.corporate_website.data;

import java.sql.ResultSet;
import java.util.Date;

import com.zillious.corporate_website.db.WebsiteDBConnector;
import com.zillious.corporate_website.utils.DateUtility;

/**
 * @author nishant.gupta
 *
 */
public class AttendanceFromDeviceDTO {
    private String m_userId;
    private String m_deviceId;
    private String m_date;
    private String m_time;

    private String m_dateTime;

    public AttendanceFromDeviceDTO(String id, String device, String date, String time) {
        m_userId = id;
        m_deviceId = device;
        m_date = date;
        m_time = time;
    }

    public AttendanceFromDeviceDTO(ResultSet rs) throws Exception {
        WebsiteDBConnector s_connector = WebsiteDBConnector.getStaticInstance();
        m_userId = rs.getString(1);
        Date gen_ts = s_connector.getTimeStampAsDate(rs, 2);
        m_dateTime = DateUtility.getDateInMysqlTSFormat(gen_ts);
        m_deviceId = rs.getString(3);
    }

    /**
     * @return the userId
     */
    public String getUserId() {
        return m_userId;
    }

    /**
     * @return the deviceId
     */
    public String getDeviceId() {
        return m_deviceId;
    }

    /**
     * @return the date
     */
    public String getDate() {
        return m_date;
    }

    /**
     * @return the time
     */
    public String getTime() {
        return m_time;
    }

    public String getDateTime() {
        return m_dateTime;
    }

}
