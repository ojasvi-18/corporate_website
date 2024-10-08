package com.zillious.corporate_website.portal.service;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.google.gson.JsonObject;
import com.zillious.corporate_website.portal.ui.model.LeaveRequest;
import com.zillious.corporate_website.portal.ui.model.User;

@Component
public interface LeavesService {

    /**
     * This API gets all the leaves request related data for the user
     * 
     * @param userId
     * @param year
     * @return
     */
    JsonObject getLeaveRequestDataForUser(int userId, String year);

    JsonObject addLeaveRequestDataForUser(String requestJson);

    JsonObject deleteLeaveRequest(String requestJson);

    JsonObject updateLeaveRequestDataForUser(String requestJson);

    JsonObject updateLeaveRequestStatus(String requestJson);

    JsonObject sendReminderForTheLeaveRequest(String requestJson);

    List<LeaveRequest> getLeaveRequestDataForUser(User user, Date startDate, Date endDate);
    List<LeaveRequest> getLeaveRequestDataForUser(Set<User> user, Date startDate, Date endDate);

    JsonObject getLeaveReportData(String requestJson, User loggedInUser);

    JsonObject getLeaveReportCalendarData(String requestJson, User loggedInUser);

}
