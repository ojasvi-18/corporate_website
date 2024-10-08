package com.zillious.corporate_website.portal.ui.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.zillious.corporate_website.portal.service.LeavesPolicyService;
import com.zillious.corporate_website.portal.service.LeavesService;
import com.zillious.corporate_website.portal.ui.ResponseStatus;
import com.zillious.corporate_website.portal.ui.model.User;
import com.zillious.corporate_website.portal.ui.navigation.RequestMappings;
import com.zillious.corporate_website.portal.utility.JsonUtility;
import com.zillious.corporate_website.ui.beans.SecurityBean;
import com.zillious.corporate_website.ui.security.ZilliousSecurityWrapperRequest;
import com.zillious.corporate_website.ui.session.SessionStore;

/**
 * @author nishant.gupta
 *
 */
@RestController
@RequestMapping(RequestMappings.APPLYLEAVE_BASE)
public class LeavesRequestController {
    @Autowired
    private LeavesService       m_leavesService;

    @Autowired
    private LeavesPolicyService m_leavesPolicyService;

    private static Logger       s_logger = Logger.getLogger(LeavesRequestController.class);

    @RequestMapping(value = RequestMappings.DIRECT_MAPPING, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public String getUserLeavesInfo(@RequestBody String requestJson, HttpServletRequest request) {
        ZilliousSecurityWrapperRequest zilliousRequest = SecurityBean.getRequest(request);
        User loggedInUser = SessionStore.getLoggedInUser(zilliousRequest);
        s_logger.debug("Request json obtained: " + requestJson);

        JsonParser parser = new JsonParser();
        JsonObject requestObject = (JsonObject) parser.parse(requestJson);
        JsonObject requestData = requestObject.get("request_data").getAsJsonObject();
        String year = requestObject.get("year").getAsString();

        int userId = JsonUtility.getUserIdFromJson(requestData);
        JsonObject responseJson = new JsonObject();
        boolean isSuccess = true;
        String errorMessage = "";
        try {
            JsonObject leaveRequestInitialDataForUser = m_leavesService.getLeaveRequestDataForUser(userId, year);
            if (leaveRequestInitialDataForUser == null
                    || !ResponseStatus.SUCCESS.name()
                            .equals(leaveRequestInitialDataForUser.get("status").getAsString())) {
                isSuccess = false;
            } else {
                responseJson.add("user_data", leaveRequestInitialDataForUser);
            }

            if (isSuccess) {
                JsonObject leaveTypesAsJson = m_leavesPolicyService.getLeavePolicyTypesAsJson(loggedInUser);
                isSuccess = !leaveTypesAsJson.isJsonNull();
                errorMessage = "No Leave Type Defined";
                responseJson.add("leave_types", leaveTypesAsJson);
            }
        } catch (Exception e) {
            errorMessage = e.getMessage();
        }
        JsonUtility.createResponseJson(responseJson, isSuccess, errorMessage);
        return responseJson.toString();
    }

    /**
     * Raises a new leave request for the user, details of which are sent as
     * post parameters
     * 
     * @param requestJson
     * @return
     */
    @RequestMapping(value = RequestMappings.ADD, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody String addUserLeaveRequest(@RequestBody String requestJson) {
        JsonObject leaveRequestListForUser = m_leavesService.addLeaveRequestDataForUser(requestJson);
        return leaveRequestListForUser.toString();
    }

    /**
     * updates the leave request for the user, details of which are sent as post
     * parameters
     * 
     * @param requestJson
     * @return
     */
    @RequestMapping(value = RequestMappings.UPDATE, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody String updateUserLeaveRequest(@RequestBody String requestJson) {
        JsonObject leaveRequestListForUser = m_leavesService.updateLeaveRequestDataForUser(requestJson);
        return leaveRequestListForUser.toString();
    }

    @RequestMapping(value = RequestMappings.DELETE, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody String deleteTeam(@RequestBody String requestJson) {
        JsonObject responseJson = m_leavesService.deleteLeaveRequest(requestJson);
        return responseJson.toString();

    }

    /**
     * updates the leave request for the user, details of which are sent as post
     * parameters
     * 
     * @param requestJson
     * @return
     */
    @RequestMapping(value = RequestMappings.UPDATESTATUS, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody String updateUserLeaveRequestStatus(@RequestBody String requestJson) {
        JsonObject leaveRequestListForUser = m_leavesService.updateLeaveRequestStatus(requestJson);
        return leaveRequestListForUser.toString();
    }

    /**
     * Sends the reminder to the servicers of the request to take some action on
     * the request raised.
     * 
     * @param requestJson
     * @return
     */
    @RequestMapping(value = RequestMappings.SEND_REMINDER, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody String sendLeaveRequestReminder(@RequestBody String requestJson) {
        JsonObject leaveRequestListForUser = m_leavesService.sendReminderForTheLeaveRequest(requestJson);
        return leaveRequestListForUser.toString();
    }
}
