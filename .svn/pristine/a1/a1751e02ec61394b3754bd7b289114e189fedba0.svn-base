package com.zillious.corporate_website.portal.service;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.zillious.corporate_website.portal.ui.EventType;
import com.zillious.corporate_website.portal.ui.model.LeavePolicy;
import com.zillious.corporate_website.portal.ui.model.User;
import com.zillious.corporate_website.portal.ui.model.YearlyCalendar;

/**
 * @author satyam.mittal
 *
 */
@Component
public interface LeavesPolicyService {

    /**
     * THIS METHOD WILL RETURN LIST OF HOLIDAYS FOR HOLIDAY CALENDAR PRESENT ON
     * LEAVEPOLICY.HTML
     * 
     * @param year for which list is to be obtained
     * 
     * @return
     */
    public JsonObject getholidayCalendarHolidaysAsJson(User loggedInUser, String year);

    public JsonObject addHolidayToHolidayCalendar(String newHolidayString);

    public JsonObject updateHolidayCalendarHoliday(String updateHoliday);

    /**
     * this method will return holiday type list
     * 
     * @return
     */
    public JsonObject getLeavePolicyTypesAsJson(User loggedInUser);

    /**
     * this function will add new holiday type to data base
     * 
     * @param newHolidayToHolidayType
     * @return
     */
    public JsonObject addHolidayToHolidayType(String newHolidayToHolidayType);

    public JsonObject updateHolidayType(String updateHoliday);

    /**
     * this method will delete leave type from leave_type table form data base
     * 
     * @param updateHoliday
     * @return
     */
    public JsonObject deleteHolidayType(String updateHoliday);

    /**
     * this method will delete holiday calendar event from data base
     * 
     * @param updateHoliday
     * @return
     */
    public JsonObject deleteHolidayCalendarEvent(String updateHoliday);

    // public List<YearlyCalendar> getHolidayCalendarHolidays(String year);

    /**
     * This api fetches the list of holidays for the current year from the
     * database and converts it into a string format
     * 
     * @return
     */
    public Set<String> getHolidayDates();

    public boolean sendNotificationsOfYearCalendar(User loggedInUser, String requestJson) throws Exception;

    public JsonArray getCalendarEventsAsJson(List<YearlyCalendar> list);

    public List<LeavePolicy> getLeavesPolicy();

    public List<YearlyCalendar> getEventsForDateRange(EventType eventType, Date startDate, Date endDate);
}
