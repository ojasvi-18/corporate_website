package com.zillious.corporate_website.portal.dao;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;

import com.zillious.corporate_website.portal.ui.EventType;
import com.zillious.corporate_website.portal.ui.model.YearlyCalendar;
import com.zillious.corporate_website.portal.ui.model.LeavePolicy;

/**
 * @author satyam.mittal
 *
 */
@Component
public interface LeavesPolicyDao extends Dao {

    /**
     * this function will get all holidays for the holidaycalendar from
     * holidaycalendaerholiday table from db
     * 
     * @param eventType
     * @param year 
     * 
     * @return
     */
    public List<YearlyCalendar> getEventsFromCalendar(EventType eventType, String year);

    /**
     * this function will add new holiday to holidaycalendarholiday table in db
     * 
     * @param newHoliday
     * @return
     */
    public boolean addHolidayToCalendar(YearlyCalendar newHoliday);

    /**
     * this function will update holidaycalendarholiday table already present
     * data i.e editing the holidays
     * 
     * @param updatedHoliday
     * @return
     */
    public boolean updateHolidayCalendar(YearlyCalendar updatedHoliday);

    /**
     * this method will return list of holiday types from database
     * 
     * @return
     */
    public List<LeavePolicy> getLeavesPolicy();

    /**
     * this function will add new holiday to holiday type and will return either
     * success or error
     * 
     * @param newHoliday
     * @return
     */
    public boolean addHolidayToHolidayType(LeavePolicy newHoliday);

    /**
     * this function will update the already present holiday type in data base
     * 
     * @param updatedHoliday
     * @return
     */
    public boolean updateHolidayType(LeavePolicy updatedHoliday);

    /**
     * this method is used to delete leave_type
     * 
     * @param holidayTypeData
     * @return
     */
    public boolean deleteHolidayType(LeavePolicy holidayTypeData);

    /**
     * this method is used to delete event from holiday calendar
     * 
     * @param calendarEvent
     * @return
     */
    public boolean deleteHolidayCalendarEvent(YearlyCalendar calendarEvent);

    public List<YearlyCalendar> getEventsFromCalendar(EventType eventType, Date startDate, Date endDate);
}
