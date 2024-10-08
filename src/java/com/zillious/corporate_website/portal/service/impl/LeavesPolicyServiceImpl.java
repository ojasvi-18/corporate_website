package com.zillious.corporate_website.portal.service.impl;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.zillious.corporate_website.portal.dao.LeavesPolicyDao;
import com.zillious.corporate_website.portal.entity.EmailMessage;
import com.zillious.corporate_website.portal.service.LeavesPolicyService;
import com.zillious.corporate_website.portal.service.UserService;
import com.zillious.corporate_website.portal.ui.EventType;
import com.zillious.corporate_website.portal.ui.model.LeavePolicy;
import com.zillious.corporate_website.portal.ui.model.User;
import com.zillious.corporate_website.portal.ui.model.YearlyCalendar;
import com.zillious.corporate_website.portal.utility.JsonUtility;
import com.zillious.corporate_website.ui.navigation.WebsiteException;
import com.zillious.corporate_website.ui.navigation.WebsiteExceptionType;
import com.zillious.corporate_website.utils.DateUtility;
import com.zillious.corporate_website.utils.EmailSender;

/**
 * @author satyam.mittal
 *
 */

@Service
public class LeavesPolicyServiceImpl implements LeavesPolicyService {
    private static Logger   s_logger = Logger.getLogger(LeavesPolicyServiceImpl.class);
    @Autowired
    private LeavesPolicyDao m_leavesPolicyDao;

    @Autowired
    private UserService     m_userService;

    @Override
    public JsonObject getholidayCalendarHolidaysAsJson(User loggedInUser, String year) {
        JsonObject finalJson = new JsonObject();
        List<YearlyCalendar> holidayCalendarHolidayslist = getHolidayCalendarHolidays(year);
        JsonArray holidayCalendarHolidayListJson = new JsonArray();
        try {
            if (holidayCalendarHolidayslist != null) {
                for (YearlyCalendar holidayCalemdarHolidays : holidayCalendarHolidayslist) {
                    holidayCalendarHolidayListJson.add(holidayCalemdarHolidays
                            .convertHolidayCalendarListToCompleteJson());
                }
            }
            finalJson.add("HolidayCalendar", holidayCalendarHolidayListJson);
            JsonUtility.addIsAdminProperty(finalJson, loggedInUser);

            s_logger.debug("holidayCalendarHolidayListJson: " + holidayCalendarHolidayListJson);
            return finalJson;
        } catch (Exception e) {
            JsonUtility.createResponseJson(finalJson, false, WebsiteExceptionType.EXCEPTION_ERROR.getDesc());
            s_logger.debug("Exception in holidayCalendarHolidayListJson: ", e);
            return finalJson;
        }
    }

    /**
     * @param year
     * @return
     */
    // @Override
    protected List<YearlyCalendar> getHolidayCalendarHolidays(String year) {
        return getCalendarEvents(EventType.HOLIDAY, year);
    }

    /**
     * @param year
     * @param holiday
     * @return
     */
    protected List<YearlyCalendar> getCalendarEvents(EventType eventType, String year) {
        List<YearlyCalendar> holidayCalendarHolidayslist = this.m_leavesPolicyDao
                .getEventsFromCalendar(eventType, year);
        return holidayCalendarHolidayslist;
    }

    @Override
    public JsonObject addHolidayToHolidayCalendar(String newHolidayString) {
        JsonParser parser = new JsonParser();
        JsonObject jobj = (JsonObject) parser.parse(newHolidayString);
        YearlyCalendar newHoliday = YearlyCalendar.parseEventFromJson(jobj, EventType.HOLIDAY);

        JsonObject finalJSON = new JsonObject();
        boolean isSuccess = false;
        String errorMsg = "";
        try {
            isSuccess = m_leavesPolicyDao.addHolidayToCalendar(newHoliday);
            finalJSON.addProperty("id", newHoliday.getId());
        } catch (Exception e) {
            errorMsg = e.getMessage();
        }

        JsonUtility.createResponseJson(finalJSON, isSuccess, errorMsg);
        return finalJSON;
    }

    @Override
    public JsonObject updateHolidayCalendarHoliday(String updateHolidayJson) {
        JsonParser parser = new JsonParser();
        JsonObject jobj = (JsonObject) parser.parse(updateHolidayJson);
        YearlyCalendar updatedHoliday = YearlyCalendar.parseEventFromJson(jobj, EventType.HOLIDAY);
        updatedHoliday.setId(jobj.get("id").getAsInt());

        JsonObject finalJSON = new JsonObject();
        boolean isSuccess = false;
        String errorMsg = "";
        try {
            isSuccess = m_leavesPolicyDao.updateHolidayCalendar(updatedHoliday);
        } catch (Exception e) {
            errorMsg = e.getMessage();
        }

        JsonUtility.createResponseJson(finalJSON, isSuccess, errorMsg);

        return finalJSON;

    }

    @Override
    public JsonObject getLeavePolicyTypesAsJson(User loggedInUser) {
        JsonArray holidayTypeHolidayListJson = new JsonArray();
        JsonObject finalJson = new JsonObject();
        try {
            List<LeavePolicy> holidayTypeHolidayslist = getLeavesPolicy();
            if (holidayTypeHolidayslist != null) {
                for (LeavePolicy holidayTypeHolidays : holidayTypeHolidayslist) {
                    holidayTypeHolidayListJson.add(holidayTypeHolidays.convertToJson());
                }
            }
            finalJson.add("LeaveType", holidayTypeHolidayListJson);
            JsonUtility.addIsAdminProperty(finalJson, loggedInUser);

            s_logger.debug("Leave policy types as json: " + holidayTypeHolidayListJson);
            return finalJson;

        } catch (Exception e) {
            JsonUtility.createResponseJson(finalJson, false, WebsiteExceptionType.EXCEPTION_ERROR.getDesc());
            s_logger.debug("exceptions in Leave policy types : ", e);
            return finalJson;
        }

    }

    /**
     * @return
     */
    @Override
    @Transactional
    public List<LeavePolicy> getLeavesPolicy() {
        return this.m_leavesPolicyDao.getLeavesPolicy();
    }

    @Override
    public JsonObject addHolidayToHolidayType(String newHolidayToHolidayType) {
        JsonParser parser = new JsonParser();
        JsonObject jobj = null;
        if (newHolidayToHolidayType != null) {
            jobj = (JsonObject) parser.parse(newHolidayToHolidayType);
        }
        LeavePolicy newHoliday = new LeavePolicy();

        // if (jobj.getAsJsonPrimitive("id") != null) {
        // newHoliday.setHolidayId(jobj.getAsJsonPrimitive("id").getAsInt());
        // }
        if (jobj.getAsJsonPrimitive("name").toString() != null) {
            newHoliday.setName(jobj.getAsJsonPrimitive("name").toString().replaceAll("^\"|\"$", ""));
        }
        if (jobj.getAsJsonPrimitive("days") != null) {
            newHoliday.setDays(jobj.getAsJsonPrimitive("days").getAsInt());
        }
        if (jobj.getAsJsonPrimitive("additionalComments").toString() != null){
            newHoliday.setAdditionalComments(jobj.getAsJsonPrimitive("additionalComments").toString().replaceAll("^\"|\"$", ""));
        }
        if (jobj.getAsJsonPrimitive("isSandwich") != null){
            newHoliday.setIsSandwich(jobj.getAsJsonPrimitive("isSandwich").getAsBoolean());
        }
        JsonObject finalJSON = new JsonObject();
        boolean isSuccess = false;
        String errorMsg = "";
        try {
            isSuccess = m_leavesPolicyDao.addHolidayToHolidayType(newHoliday);
            finalJSON.addProperty("id", newHoliday.getId());
        } catch (Exception e) {
            errorMsg = e.getMessage();
            s_logger.debug("exceptions in Leave policy types add : ", e);
        }

        JsonUtility.createResponseJson(finalJSON, isSuccess, errorMsg);
        return finalJSON;
    }

    @Override
    public JsonObject updateHolidayType(String updateHoliday) {
        JsonParser parser = new JsonParser();
        JsonObject jobj = (JsonObject) parser.parse(updateHoliday);
        LeavePolicy newHoliday = LeavePolicy.parseFromJson(jobj);

        JsonObject finalJSON = new JsonObject();
        // finalJSON.addProperty("id", team.getTeamId());
        boolean isSuccess = false;
        String errorMsg = "";
        try {
            isSuccess = m_leavesPolicyDao.updateHolidayType(newHoliday);
        } catch (Exception e) {
            errorMsg = e.getMessage();
            s_logger.debug("exceptions in Leave policy types update : ", e);
        }

        JsonUtility.createResponseJson(finalJSON, isSuccess, errorMsg);
        // String returnedString = this.dao.updateTeam(team);

        return finalJSON;
    }

    @Override
    public JsonObject deleteHolidayType(String holidayTypeData) {
        JsonParser parser = new JsonParser();
        JsonObject jobj = (JsonObject) parser.parse(holidayTypeData);
        LeavePolicy leaveType = new LeavePolicy();
        leaveType.setId(jobj.get("id").getAsInt());
        JsonObject finalJSON = new JsonObject();
        finalJSON.addProperty("id", leaveType.getId());
        boolean isSuccess = false;
        String errorMsg = "";
        try {
            isSuccess = m_leavesPolicyDao.deleteHolidayType(leaveType);
        } catch (Exception e) {
            errorMsg = e.getMessage();
            s_logger.debug("exceptions in Leave policy types delete : ", e);
        }

        JsonUtility.createResponseJson(finalJSON, isSuccess, errorMsg);
        return finalJSON;
    }

    @Override
    public JsonObject deleteHolidayCalendarEvent(String deleteHoliday) {
        JsonParser parser = new JsonParser();
        JsonObject jobj = (JsonObject) parser.parse(deleteHoliday);
        YearlyCalendar calendarEvent = new YearlyCalendar();
        calendarEvent.setId(jobj.get("id").getAsInt());
        calendarEvent.setEventType(EventType.HOLIDAY);
        calendarEvent.setStartDate(new Date());
        calendarEvent.setEndDate(new Date());
        calendarEvent.setName("");
        JsonObject finalJSON = new JsonObject();
        boolean isSuccess = false;
        String errorMsg = "";
        try {
            isSuccess = m_leavesPolicyDao.deleteHolidayCalendarEvent(calendarEvent);
            finalJSON.addProperty("id", calendarEvent.getId());
        } catch (Exception e) {
            errorMsg = e.getMessage();
            s_logger.debug("exceptions in deleteHolidayCalendarEvent : ", e);
        }
        JsonUtility.createResponseJson(finalJSON, isSuccess, errorMsg);
        return finalJSON;
    }

    @Override
    public Set<String> getHolidayDates() {
        List<YearlyCalendar> holidays = getHolidayCalendarHolidays(null);
        Set<String> dates = null;
        if (holidays != null) {
            dates = new HashSet<String>();
            for (YearlyCalendar holiday : holidays) {
                dates.add(DateUtility.getDateInDDMMMYY(holiday.getStartDate()));
            }
        }

        return dates;
    }

    @Override
    public boolean sendNotificationsOfYearCalendar(User loggedInUser, String requestJson) throws Exception {
        try {
            JsonParser parser = new JsonParser();
            JsonObject parsedObject = (JsonObject) parser.parse(requestJson);
            int year = parsedObject.get("year").getAsInt();
            JsonArray jsonArray = parsedObject.get("list").getAsJsonArray();

            if (!loggedInUser.getUserRole().isAdminRole()) {
                throw new WebsiteException(WebsiteExceptionType.INVALID_ACCESS);
            }

            List<String> emailIDs = m_userService.getAllEmailIDs();

            if (emailIDs == null || emailIDs.isEmpty()) {
                throw new WebsiteException(WebsiteExceptionType.INSUFFICIENT_RECIPIENTS);
            }

            EmailMessage emailMessage = new EmailMessage();

            for (String toEmail : emailIDs) {
                emailMessage.addTo(toEmail);
            }

            // subject
            String subject = "Holiday List for Year - " + year;

            emailMessage.setSubject(subject);

            String content = YearlyCalendar.getHolidayCalendarNotificationContent(jsonArray, year);

            emailMessage.setFrom(loggedInUser.getEmail());

            emailMessage.setContent(content);

            EmailSender.sendEmail(emailMessage);
            return true;
        } catch (Exception e) {
            s_logger.error("Error in sending yearly calendar notifications", e);
            throw e;
        }
    }

    @Override
    public JsonArray getCalendarEventsAsJson(List<YearlyCalendar> holidayCalendarHolidayslist) {
        JsonArray holidayCalendarHolidayListJson = null;
        if (holidayCalendarHolidayslist != null) {
            holidayCalendarHolidayListJson = new JsonArray();
            for (YearlyCalendar holidayCalemdarHolidays : holidayCalendarHolidayslist) {
                holidayCalendarHolidayListJson.add(holidayCalemdarHolidays.convertHolidayCalendarListToCompleteJson());
            }
        }
        return holidayCalendarHolidayListJson;
    }

    /**
     * @param eventType
     * @param startDate
     * @param endDate
     * @return
     */
    @Override
    @Transactional
    public List<YearlyCalendar> getEventsForDateRange(EventType eventType, Date startDate, Date endDate) {
        return m_leavesPolicyDao.getEventsFromCalendar(eventType, startDate, endDate);
    }
}
