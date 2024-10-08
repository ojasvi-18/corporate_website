package com.zillious.corporate_website.portal.dao;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.zillious.corporate_website.portal.ui.model.LeaveRequest;
import com.zillious.corporate_website.portal.ui.model.User;

/**
 * @author satyam.mittal
 *
 */
@Component
public interface LeavesDao extends Dao {

    LeaveRequest getLeaveRequest(int asInt);

    boolean update(LeaveRequest leaveRequest);

    List<LeaveRequest> getLeaveRequestDataForUser(User user, Date startDate, Date endDate);

    List<LeaveRequest> getLeaveRequestDataForUser(Set<User> users, Date startDate, Date endDate);

    // List<LeaveRequest> getLeaveRequests(String year, User forUser);

    List<LeaveRequest> getLeaveRequests(String year, Set<User> forUser);
    


    /**
     * This API gets leave requests of the user for the current week .
     * 
     * @param teamMember
     * @param currentDate
     * @param lastDateOfWeek
     * @return List<LeaveRequest>
     */
    List<LeaveRequest> getWeekLeaveRequestsForUser(User teamMember, Date currentDate, Date lastDateOfTheWee);

}
