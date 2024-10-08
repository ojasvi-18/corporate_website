package com.zillious.corporate_website.portal.service;

import org.springframework.stereotype.Component;

import com.google.gson.JsonObject;
import com.zillious.corporate_website.portal.ui.model.User;

/**
 * @author nishant.gupta
 *
 */
@Component
public interface AttendanceService {

    JsonObject getAttendanceData(String requestJson, User requestor);

    JsonObject getCalendarData(String requestJson, User requestor);

    JsonObject addManualAttendanceEntry(String requestJson, String ipAddress);
}
