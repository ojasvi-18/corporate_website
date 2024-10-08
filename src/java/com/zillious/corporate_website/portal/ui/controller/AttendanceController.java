package com.zillious.corporate_website.portal.ui.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.JsonObject;
import com.zillious.corporate_website.portal.service.AttendanceService;
import com.zillious.corporate_website.portal.service.LeavesService;
import com.zillious.corporate_website.portal.ui.model.User;
import com.zillious.corporate_website.portal.ui.navigation.RequestMappings;
import com.zillious.corporate_website.portal.utility.JsonUtility;
import com.zillious.corporate_website.ui.beans.SecurityBean;
import com.zillious.corporate_website.ui.navigation.WebsiteException;
import com.zillious.corporate_website.ui.security.ZilliousSecurityWrapperRequest;
import com.zillious.corporate_website.ui.session.SessionStore;

/**
 * @author nishant.gupta
 *
 */
@RestController
public class AttendanceController {

    @Autowired
    private AttendanceService m_attendanceService;

    @Autowired
    private LeavesService     m_leaveReportService;

    private static Logger     s_logger = Logger.getLogger(LeavesRequestController.class);

    @RequestMapping(value = RequestMappings.ATTENDANCE_BASE, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public String getUserLeavesInfo(@RequestBody String requestJson, HttpServletRequest request) {
        s_logger.debug("Request json obtained: " + requestJson);

        String errorMessage = "";
        JsonObject responseJson;
        try {
            ZilliousSecurityWrapperRequest zilliousRequest = SecurityBean.getRequest(request);
            User loggedInUser = SessionStore.getLoggedInUser(zilliousRequest);
            responseJson = m_attendanceService.getAttendanceData(requestJson, loggedInUser);
        } catch (Exception e) {
            responseJson = new JsonObject();
            if (e instanceof WebsiteException) {
                errorMessage = ((WebsiteException) e).getType().getDesc();
            } else {
                errorMessage = e.getMessage();
            }
            JsonUtility.createResponseJson(responseJson, false, errorMessage);
        }
        return responseJson.toString();
    }

    @RequestMapping(value = RequestMappings.ATTENDANCECALENDAR, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public String getUserLeavesCalendarInfo(@RequestBody String requestJson, HttpServletRequest request) {
        s_logger.debug("Request json obtained: " + requestJson);

        String errorMessage = "";
        JsonObject responseJson;
        try {
            ZilliousSecurityWrapperRequest zilliousRequest = SecurityBean.getRequest(request);
            User loggedInUser = SessionStore.getLoggedInUser(zilliousRequest);
            responseJson = m_attendanceService.getCalendarData(requestJson, loggedInUser);
        } catch (Exception e) {
            responseJson = new JsonObject();
            if (e instanceof WebsiteException) {
                errorMessage = ((WebsiteException) e).getType().getDesc();
            } else {
                errorMessage = e.getMessage();
            }            
            JsonUtility.createResponseJson(responseJson, false, errorMessage);
        }
        return responseJson.toString();
    }

    @RequestMapping(value = RequestMappings.LEAVEREPORT_BASE, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public String getUserLeavesReportInfo(@RequestBody String requestJson, HttpServletRequest request) {
        s_logger.debug("Request json obtained: " + requestJson);

        String errorMessage = "";
        JsonObject responseJson;
        try {
            ZilliousSecurityWrapperRequest zilliousRequest = SecurityBean.getRequest(request);
            User loggedInUser = SessionStore.getLoggedInUser(zilliousRequest);
            responseJson = m_leaveReportService.getLeaveReportData(requestJson, loggedInUser);
        } catch (Exception e) {
            responseJson = new JsonObject();
            if (e instanceof WebsiteException) {
                errorMessage = ((WebsiteException) e).getType().getDesc();
            } else {
                errorMessage = e.getMessage();
            }
            JsonUtility.createResponseJson(responseJson, false, errorMessage);
        }
        return responseJson.toString();
    }
    
    @RequestMapping(value = RequestMappings.LEAVEREPORTCALENDAR, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public String getUserLeavesReportCalendarInfo(@RequestBody String requestJson, HttpServletRequest request) {
        s_logger.debug("Request json obtained: " + requestJson);

        String errorMessage = "";
        JsonObject responseJson;
        try {
            ZilliousSecurityWrapperRequest zilliousRequest = SecurityBean.getRequest(request);
            User loggedInUser = SessionStore.getLoggedInUser(zilliousRequest);
            responseJson = m_leaveReportService.getLeaveReportCalendarData(requestJson, loggedInUser);
        } catch (Exception e) {
            responseJson = new JsonObject();
            if (e instanceof WebsiteException) {
                errorMessage = ((WebsiteException) e).getType().getDesc();
            } else {
                errorMessage = e.getMessage();
            }
            JsonUtility.createResponseJson(responseJson, false, errorMessage);
        }
        return responseJson.toString();
    }
}
