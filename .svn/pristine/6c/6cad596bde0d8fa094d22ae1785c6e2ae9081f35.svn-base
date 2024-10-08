package com.zillious.corporate_website.portal.ui.controller;

/**
 * @author satyam.mittal
 *
 */
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
import com.zillious.corporate_website.portal.ui.model.User;
import com.zillious.corporate_website.portal.ui.navigation.RequestMappings;
import com.zillious.corporate_website.portal.utility.JsonUtility;
import com.zillious.corporate_website.ui.beans.SecurityBean;
import com.zillious.corporate_website.ui.navigation.WebsiteExceptionType;
import com.zillious.corporate_website.ui.security.ZilliousSecurityWrapperRequest;
import com.zillious.corporate_website.ui.session.SessionStore;
import com.zillious.corporate_website.utils.DateUtility;

@RestController
@RequestMapping(RequestMappings.LEAVESPOLICY_BASE)
public class LeavesPolicyController {
    private static Logger       s_logger = Logger.getLogger(LeavesPolicyController.class);

    @Autowired
    private LeavesPolicyService leavesPolicyService;

    // METHODS FOR HOLIDAY CALENDAR BEGINS
    /**
     * this function will return list of all holidays present in holiday
     * calendar list from the data base for the LeavesPolicy Page
     * (leavePolicy.html)
     */
    @RequestMapping(value = RequestMappings.HOLIDAYCALENDAR, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public String getHolidayCalendarList(HttpServletRequest request, @RequestBody String requestJson) {
        String year = null;
        try {
            JsonParser parser = new JsonParser();
            JsonObject parsedObject = (JsonObject) parser.parse(requestJson);
            year = parsedObject.get("year").getAsString();
        } catch (Exception e) {

        }
        
        if (year == null) {
            return getHolidayListForYear(request, DateUtility.getCurrentYear());
        }

        return getHolidayListForYear(request, year);
    }

    private String getHolidayListForYear(HttpServletRequest request, String year) {
        ZilliousSecurityWrapperRequest zilliousRequest = SecurityBean.getRequest(request);
        User loggedInUser = SessionStore.getLoggedInUser(zilliousRequest);

        s_logger.debug("Came in getHolidayCalendarList of LeavesPolicyController");

        JsonObject holidayCalendarHolidaysList = leavesPolicyService.getholidayCalendarHolidaysAsJson(loggedInUser,
                year);
        return holidayCalendarHolidaysList.toString();
    }

    /**
     * this function will add new holiday to table holidaycalendarholidays in
     * data base
     * 
     * @param newHolidayToCalendar
     * @return
     */
    @RequestMapping(value = RequestMappings.HOLIDAYCALENDAR_ADD, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody String addHolidayToHolidayCalendar(@RequestBody String newHolidayToCalendar,
            HttpServletRequest request) {
        ZilliousSecurityWrapperRequest zilliousRequest = SecurityBean.getRequest(request);
        User loggedInUser = SessionStore.getLoggedInUser(zilliousRequest);
        try {
            if ((loggedInUser == null) || (!loggedInUser.getUserRole().isAdminRole())) {
                JsonObject json = new JsonObject();
                JsonUtility.createResponseJson(json, false, WebsiteExceptionType.INVALID_ACCESS.getDesc());
                return json.toString();
            } else {
                JsonObject j_obj = leavesPolicyService.addHolidayToHolidayCalendar(newHolidayToCalendar);
                s_logger.debug("In HolidayCalendar_Add " + j_obj);
                return j_obj.toString();
            }

        } catch (Exception e) {
            JsonObject json = new JsonObject();
            JsonUtility.createResponseJson(json, false, WebsiteExceptionType.EXCEPTION_ERROR.getDesc());
            s_logger.debug("error while adding holiday in holiday calendar: ", e);

            return json.toString();
        }

    }

    /**
     * this function will update the holiday present in holidaycalendarholiday
     * table in data base
     * 
     * @param updateHliday
     * @return
     */
    @RequestMapping(value = RequestMappings.HOLIDAYCALENDAR_UPDATE, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody String updateHolidayCalendar(@RequestBody String updateHoliday, HttpServletRequest request) {
        ZilliousSecurityWrapperRequest zilliousRequest = SecurityBean.getRequest(request);
        User loggedInUser = SessionStore.getLoggedInUser(zilliousRequest);
        try {
            s_logger.debug("in Update holiday calendar: ");

            if ((loggedInUser == null) || (!loggedInUser.getUserRole().isAdminRole())) {
                JsonObject json = new JsonObject();
                JsonUtility.createResponseJson(json, false, WebsiteExceptionType.INVALID_ACCESS.getDesc());
                return json.toString();
            } else {

                JsonObject j_obj = leavesPolicyService.updateHolidayCalendarHoliday(updateHoliday);
                return j_obj.toString();
            }
        } catch (Exception e) {
            JsonObject json = new JsonObject();
            JsonUtility.createResponseJson(json, false, WebsiteExceptionType.EXCEPTION_ERROR.getDesc());
            s_logger.debug("error in update holiday calendar: ", e);
            return json.toString();
        }
    }

    @RequestMapping(value = RequestMappings.HOLIDAYCALENDAR_DELETE, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody String deleteHolidayCalendarEvent(@RequestBody String updateHoliday, HttpServletRequest request) {
        ZilliousSecurityWrapperRequest zilliousRequest = SecurityBean.getRequest(request);
        User loggedInUser = SessionStore.getLoggedInUser(zilliousRequest);
        try {
            if ((loggedInUser == null) || (!loggedInUser.getUserRole().isAdminRole())) {
                JsonObject json = new JsonObject();
                JsonUtility.createResponseJson(json, false, WebsiteExceptionType.INVALID_REQUEST.getDesc());
                return json.toString();
            } else {

                JsonObject j_obj = leavesPolicyService.deleteHolidayCalendarEvent(updateHoliday);
                return j_obj.toString();
            }

        } catch (Exception e) {
            JsonObject json = new JsonObject();
            JsonUtility.createResponseJson(json, false, WebsiteExceptionType.EXCEPTION_ERROR.getDesc());
            s_logger.debug("ececption occur in holiday calendar deleted: ", e);
            return json.toString();
        }

    }

    // METHODS FOR HOLIDAY TYPE BEGINS
    /**
     * this method will return list of holidayTypes
     * 
     * @return
     */
    @RequestMapping(value = RequestMappings.LEAVETYPE_BASE, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String fetchHolidayTypeList(HttpServletRequest request) {
        ZilliousSecurityWrapperRequest zilliousRequest = SecurityBean.getRequest(request);
        User loggedInUser = SessionStore.getLoggedInUser(zilliousRequest);

        s_logger.debug("Came in getHolidayTypeList of LeavesPolicyController");
        JsonObject holidayTypeHolidaysList = leavesPolicyService.getLeavePolicyTypesAsJson(loggedInUser);
        return holidayTypeHolidaysList.toString();
    }

    @RequestMapping(value = RequestMappings.LEAVETYPE_ADD, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody String addHolidayToHolidayType(@RequestBody String newHolidayToHolidayType,
            HttpServletRequest request) {

        ZilliousSecurityWrapperRequest zilliousRequest = SecurityBean.getRequest(request);
        User loggedInUser = SessionStore.getLoggedInUser(zilliousRequest);
        try {
            s_logger.debug("in leavetype add :" + loggedInUser);
            if ((loggedInUser == null) || (!loggedInUser.getUserRole().isAdminRole())) {
                JsonObject json = new JsonObject();
                JsonUtility.createResponseJson(json, false, WebsiteExceptionType.INVALID_REQUEST.getDesc());
                return json.toString();
            } else {
                JsonObject j_obj = leavesPolicyService.addHolidayToHolidayType(newHolidayToHolidayType);
                return j_obj.toString();
            }
        } catch (Exception e) {
            JsonObject json = new JsonObject();
            JsonUtility.createResponseJson(json, false, WebsiteExceptionType.EXCEPTION_ERROR.getDesc());
            s_logger.debug("exception in leave type add: ", e);
            return json.toString();
        }
    }

    @RequestMapping(value = RequestMappings.LEAVETYPE_UPDATE, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody String updateHolidayType(@RequestBody String updateHoliday, HttpServletRequest request) {
        ZilliousSecurityWrapperRequest zilliousRequest = SecurityBean.getRequest(request);
        User loggedInUser = SessionStore.getLoggedInUser(zilliousRequest);
        try {
            s_logger.debug("in leave type update:" + loggedInUser);
            if ((loggedInUser == null) || (!loggedInUser.getUserRole().isAdminRole())) {
                JsonObject json = new JsonObject();
                JsonUtility.createResponseJson(json, false, WebsiteExceptionType.INVALID_REQUEST.getDesc());
                return json.toString();
            } else {
                JsonObject j_obj = leavesPolicyService.updateHolidayType(updateHoliday);
                return j_obj.toString();
            }
        } catch (Exception e) {
            JsonObject json = new JsonObject();
            JsonUtility.createResponseJson(json, false, WebsiteExceptionType.EXCEPTION_ERROR.getDesc());
            s_logger.debug("Exception in leave type update: ", e);
            return json.toString();
        }

    }

    @RequestMapping(value = RequestMappings.LEAVETYPE_DELETE, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody String deleteHolidayType(@RequestBody String updateHoliday, HttpServletRequest request) {
        ZilliousSecurityWrapperRequest zilliousRequest = SecurityBean.getRequest(request);
        User loggedInUser = SessionStore.getLoggedInUser(zilliousRequest);
        try {
            if ((loggedInUser == null) || (!loggedInUser.getUserRole().isAdminRole())) {
                JsonObject json = new JsonObject();
                JsonUtility.createResponseJson(json, false, WebsiteExceptionType.INVALID_ACCESS.getDesc());
                return json.toString();
            } else {
                JsonObject j_obj = leavesPolicyService.deleteHolidayType(updateHoliday);
                return j_obj.toString();
            }
        } catch (Exception e) {
            JsonObject json = new JsonObject();
            JsonUtility.createResponseJson(json, false, WebsiteExceptionType.EXCEPTION_ERROR.getDesc());
            s_logger.debug("exception in leave type update ", e);
            return json.toString();
        }

    }
}
