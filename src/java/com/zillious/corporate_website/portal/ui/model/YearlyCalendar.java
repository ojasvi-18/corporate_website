package com.zillious.corporate_website.portal.ui.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.zillious.corporate_website.portal.entity.utility.EventTypeSerializer;
import com.zillious.corporate_website.portal.ui.EventType;
import com.zillious.corporate_website.utils.DateUtility;

/**
 * @author satyam.mittal
 *
 */
@Entity
@Table(name = "year_calendar")
public class YearlyCalendar implements DBObject {

    /**
     * 
     */
    private static final long serialVersionUID = -281311181706825332L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int               m_id;

    @Column(name = "name", nullable = false)
    private String            m_name;

    @Column(name = "start_date", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date              m_startDate;

    @Column(name = "end_date", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date              m_endDate;

    @Column(name = "event_type", nullable = false)
    @Convert(converter = EventTypeSerializer.class)
    private EventType         m_eventType;

    public JsonElement convertHolidayCalendarListToCompleteJson() {
        JsonObject holidayCalendarHoliday = new JsonObject();
        holidayCalendarHoliday.addProperty("id", getId());
        holidayCalendarHoliday.addProperty("title", getName());
        holidayCalendarHoliday.addProperty("start", DateUtility.getDateInYYYYMMDDDash(getStartDate()));
        holidayCalendarHoliday.addProperty("end", DateUtility.getDateInYYYYMMDDDash(getEndDate()));
        holidayCalendarHoliday.addProperty("allDay", "allDay");
        return holidayCalendarHoliday;
    }

    /**
     * @return the id
     */
    public int getId() {
        return m_id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        m_id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return m_name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        m_name = name;
    }

    /**
     * @return the eventType
     */
    public EventType getEventType() {
        return m_eventType;
    }

    /**
     * @param eventType the eventType to set
     */
    public void setEventType(EventType eventType) {
        m_eventType = eventType;
    }

    /**
     * @return the startDate
     */
    public Date getStartDate() {
        return m_startDate;
    }

    /**
     * @param startDate the startDate to set
     */
    public void setStartDate(Date startDate) {
        m_startDate = startDate;
    }

    /**
     * @return the endDate
     */
    public Date getEndDate() {
        return m_endDate;
    }

    /**
     * @param endDate the endDate to set
     */
    public void setEndDate(Date endDate) {
        m_endDate = endDate;
    }

    /**
     * @param jobj
     * @param newHoliday
     * @return
     */
    public static YearlyCalendar parseEventFromJson(JsonObject jobj, EventType type) {
        YearlyCalendar eventObject = new YearlyCalendar();
        eventObject.setName(jobj.getAsJsonPrimitive("title").toString().replaceAll("^\"|\"$", ""));
        eventObject.setStartDate(DateUtility.parseDateInYYYYMMDDDashNullForException(jobj.getAsJsonPrimitive("start")
                .toString().replaceAll("^\"|\"$", "")));
        eventObject.setEndDate(DateUtility.parseDateInYYYYMMDDDashNullForException(jobj.getAsJsonPrimitive("end")
                .toString().replaceAll("^\"|\"$", "")));
        eventObject.setEventType(type);

        return eventObject;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return true;
        }

        if (!(obj instanceof YearlyCalendar)) {
            return false;
        }

        return getId() == ((YearlyCalendar) obj).getId();
    }

    @Override
    public int hashCode() {
        return 31 * getId();
    }

    public static String getHolidayCalendarNotificationContent(JsonArray jsonArray, int year) {
        String newLine = "\r\n";
        StringBuilder content = new StringBuilder("Hi,");
        content.append(newLine).append("Please find below the events/holidays for the year : ");
        content.append(year).append(newLine).append(newLine);

        Map<EventType, List<YearlyCalendar>> typeToList = new HashMap<EventType, List<YearlyCalendar>>();

        Iterator<JsonElement> iterator = jsonArray.iterator();
        while (iterator.hasNext()) {
            JsonObject object = (JsonObject) iterator.next();
            JsonElement typeElem = object.get("type");
            EventType eventType = null;
            if (typeElem != null) {
                eventType = EventType.deserialize(typeElem.getAsString());
            }
            if (eventType == null) {
                eventType = EventType.HOLIDAY;
            }

            YearlyCalendar event = YearlyCalendar.parseEventFromJson(object, eventType);

            if (!typeToList.containsKey(eventType)) {
                typeToList.put(eventType, new ArrayList<YearlyCalendar>());
            }

            typeToList.get(eventType).add(event);
        }

        for (EventType type : typeToList.keySet()) {
            content.append(type.name()).append(" : ").append(newLine);
            List<YearlyCalendar> eventList = typeToList.get(type);
            for (int i = 1; i <= eventList.size(); i++) {
                YearlyCalendar event = eventList.get(i - 1);
                content.append(i).append(", ").append(event.getName()).append(", ")
                        .append(DateUtility.getDateInDDMMMYYDashSeparator(event.getStartDate())).append(newLine);
            }
            content.append(newLine);

        }

        content.append("Thank You").append(newLine).append("Zillious Solutions Pvt Ltd");
        return content.toString();

    }
}
