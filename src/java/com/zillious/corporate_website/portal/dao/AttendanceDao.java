package com.zillious.corporate_website.portal.dao;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.zillious.corporate_website.portal.ui.model.AttendanceRecord;
import com.zillious.corporate_website.portal.ui.model.User;

/**
 * @author nishant.gupta
 *
 */
@Component
public interface AttendanceDao extends Dao {

    /**
     * returns the list of attendance records within start date and end date for a set of users
     * @param startDate
     * @param endDate
     * @param forUser
     * @return
     */
    List<AttendanceRecord> getAttendanceRecords(Date startDate, Date endDate, Set<User> forUser);

    boolean saveAttendanceEntry(AttendanceRecord record);

    void deleteAttendanceEntry(AttendanceRecord record);

    /**
     * returns the list of attendance records within the start date and end date for a particular user
     * @param startDate
     * @param endDate
     * @param user
     * @return
     */
    List<AttendanceRecord> getAttendanceRecordsForUser(Date startDate, Date endDate, User user);
}
