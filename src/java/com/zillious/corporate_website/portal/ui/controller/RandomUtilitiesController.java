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

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.zillious.corporate_website.portal.ui.model.User;
import com.zillious.corporate_website.portal.service.AttendanceService;
import com.zillious.corporate_website.portal.service.LeavesPolicyService;
import com.zillious.corporate_website.portal.service.UserService;
import com.zillious.corporate_website.portal.ui.navigation.RequestMappings;
import com.zillious.corporate_website.portal.utility.JsonUtility;
import com.zillious.corporate_website.ui.beans.SecurityBean;
import com.zillious.corporate_website.ui.navigation.WebsiteException;
import com.zillious.corporate_website.ui.navigation.WebsiteExceptionType;
import com.zillious.corporate_website.ui.security.ZilliousSecurityWrapperRequest;
import com.zillious.corporate_website.ui.session.SessionStore;

/**
 * @author nishant.gupta
 *
 */
@RestController
public class RandomUtilitiesController {

    @Autowired
    private UserService         userService;

    @Autowired
    private LeavesPolicyService leavePolicyService;

    @Autowired
    private AttendanceService   m_attendanceService;

    private static Logger       s_logger = Logger.getLogger(RandomUtilitiesController.class);

    @RequestMapping(value = RequestMappings.USERPROFILESYNC, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody String updateProfile(@RequestBody String requestJson) {

        JsonParser parser = new JsonParser();
        JsonObject requestJsonObj = (JsonObject) parser.parse(requestJson);

        JsonElement authenticationElement = requestJsonObj.get("authentication");

        JsonObject responseJson = new JsonObject();
        try {
            if (authenticationElement == null) {
                // return error message - not authenticated
                JsonUtility.createResponseJson(responseJson, false, WebsiteExceptionType.USER_AUTH_FAILED.getDesc());
                return responseJson.toString();
            }

            JsonObject authenticationObject = authenticationElement.getAsJsonObject();
            User user = User.parseForAuthentication(authenticationObject);

            if (user == null) {
                JsonUtility.createResponseJson(responseJson, false, WebsiteExceptionType.USER_AUTH_FAILED.getDesc());
                return responseJson.toString();
            }

            if (!userService.isValidUser(user)) {
                JsonUtility.createResponseJson(responseJson, false, WebsiteExceptionType.USER_AUTH_FAILED.getDesc());
                return responseJson.toString();
            }

            boolean isDone = userService.syncUserProfiles();
            JsonUtility.createResponseJson(responseJson, isDone,
                    !isDone ? "Could not handle the request. Please contact the Administrator" : "");
        } catch (Exception e) {
            responseJson = new JsonObject();
            if (e instanceof WebsiteException) {
                JsonUtility.createResponseJson(responseJson, false, ((WebsiteException) e).getType().getDesc());
            } else {
                JsonUtility.createResponseJson(responseJson, false, WebsiteExceptionType.SYSTEM_ERROR.getDesc());
            }
        }

        return responseJson.toString();
    }

    /**
     * @param userProfileJson, obtained after making changes in the ui
     * @return
     */
    @RequestMapping(value = RequestMappings.WISHBIRTHDAY, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody String sendBirthdayWishes(HttpServletRequest request, @RequestBody String wishesJson) {

        JsonObject finalJson = new JsonObject();
        try {
            ZilliousSecurityWrapperRequest zilliousRequest = SecurityBean.getRequest(request);
            User loggedInUser = SessionStore.getLoggedInUser(zilliousRequest);
            if (loggedInUser == null) {
                throw new WebsiteException(WebsiteExceptionType.INVALID_ACCESS);
            }

            boolean isSuccessful = userService.sendBirthdayWishes(loggedInUser, wishesJson);
            JsonUtility.createResponseJson(finalJson, isSuccessful, null);
        } catch (Exception e) {
            s_logger.error("Error in sending wishes");
            String errorMsg = null;
            if (e instanceof WebsiteException) {
                WebsiteExceptionType type = ((WebsiteException) e).getType();
                if (type == WebsiteExceptionType.SAME_USER) {
                    errorMsg = "Please find some friends to wish you";
                } else {
                    errorMsg = type.getDesc();
                }
            }

            JsonUtility.createResponseJson(finalJson, false, errorMsg);
        }

        return finalJson.toString();
    }

    /**
     * @param userProfileJson, obtained after making changes in the ui
     * @return
     */
    @RequestMapping(value = RequestMappings.BROADCASTYEARLYCALENDAR, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody String sendYearCalendarNotifications(HttpServletRequest request,
            @RequestBody String requestJson) {

        JsonObject finalJson = new JsonObject();
        try {
            ZilliousSecurityWrapperRequest zilliousRequest = SecurityBean.getRequest(request);
            User loggedInUser = SessionStore.getLoggedInUser(zilliousRequest);
            if (loggedInUser == null) {
                throw new WebsiteException(WebsiteExceptionType.INVALID_ACCESS);
            }

            boolean isSuccessful = leavePolicyService.sendNotificationsOfYearCalendar(loggedInUser, requestJson);
            JsonUtility.createResponseJson(finalJson, isSuccessful, null);
        } catch (Exception e) {
            s_logger.error("Error in sending wishes");
            String errorMsg = null;
            if (e instanceof WebsiteException) {
                WebsiteExceptionType type = ((WebsiteException) e).getType();
                errorMsg = type.getDesc();
            }

            JsonUtility.createResponseJson(finalJson, false, errorMsg);
        }

        return finalJson.toString();
    }

    @RequestMapping(value = RequestMappings.CHECKADMIN, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody String checkIfAdmin(@RequestBody String requestJson) {
        JsonObject responseJson = new JsonObject();
        try {

            responseJson = userService.checkIfAdmin(requestJson);

        } catch (Exception e) {
            responseJson = new JsonObject();
            if (e instanceof WebsiteException) {
                JsonUtility.createResponseJson(responseJson, false, ((WebsiteException) e).getType().getDesc());
            } else {
                JsonUtility.createResponseJson(responseJson, false, WebsiteExceptionType.SYSTEM_ERROR.getDesc());
            }
        }

        return responseJson.toString();
    }
    
    

    @RequestMapping(value = RequestMappings.MANUALATTENDANCEENTRY, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody String addManualEntry(HttpServletRequest request, @RequestBody String requestJson) {
        JsonObject responseJson = new JsonObject();
        try {
            ZilliousSecurityWrapperRequest zilliousRequest = SecurityBean.getRequest(request);
            String ipAddress = zilliousRequest.getRemoteAddr();
            responseJson = m_attendanceService.addManualAttendanceEntry(requestJson, ipAddress);
                       
        } catch (Exception e) {
            responseJson = new JsonObject();
            if (e instanceof WebsiteException) {
                JsonUtility.createResponseJson(responseJson, false, ((WebsiteException) e).getType().getDesc());
            } else {
                JsonUtility.createResponseJson(responseJson, false, WebsiteExceptionType.SYSTEM_ERROR.getDesc());
            }
        }

        return responseJson.toString();
    }
}
