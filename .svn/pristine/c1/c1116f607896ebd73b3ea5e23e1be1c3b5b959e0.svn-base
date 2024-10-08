package com.zillious.corporate_website.portal.ui.dto;

import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.zillious.corporate_website.data.AttendanceEnum;
import com.zillious.corporate_website.logger.Logger;
import com.zillious.corporate_website.portal.ui.LeaveRequestStatus;
import com.zillious.corporate_website.portal.ui.model.AttendanceRecord;
import com.zillious.corporate_website.portal.ui.model.LeaveRequest;
import com.zillious.corporate_website.portal.ui.model.User;
import com.zillious.corporate_website.portal.ui.model.YearlyCalendar;
import com.zillious.corporate_website.utils.DateUtility;
import com.zillious.corporate_website.utils.StringUtility;

/**
 * @author nishant.gupta
 *
 */
public class AttendanceDTO {
    private static final long                MIN_HOURS          = 8;
    private static final long                MIN_HOURS_HALF_DAY = 4;
    private static Logger                    s_logger           = Logger.getInstance(AttendanceDTO.class);
    /**
     * key: dateInDDMMMYY
     */
    private Map<String, PerDayAttendanceDTO> m_dateMap;
    private User                             m_user;
    private int                              m_numHalfDays      = 0;
    private int                              m_numAbsent        = 0;
    private int                              m_numPresent       = 0;
    private int                              m_numHours         = 0;
    private int                              m_approved         = 0;

    public void add(AttendanceRecord record) throws Exception {

        Date gen_ts = record.getKey().getGenTs();
        m_user = record.getKey().getEmployee();
        String dateInDDMMMYY = DateUtility.getDateInDDMMMYY(gen_ts);
        if (m_dateMap == null) {
            m_dateMap = new TreeMap<String, PerDayAttendanceDTO>(new Comparator<String>() {

                @Override
                public int compare(String o1, String o2) {
                    try {
                        Date date1 = DateUtility.parseDateInDDMMMYY(o1);
                        Date date2 = DateUtility.parseDateInDDMMMYY(o2);
                        return date1.compareTo(date2);
                    } catch (Exception e) {
                        s_logger.error("Error in parsing dates obtained for the user: " + m_user.getEmail());
                    }
                    return 0;
                }

            });
        }
        if (m_dateMap.get(dateInDDMMMYY) == null) {
            m_dateMap.put(dateInDDMMMYY, new PerDayAttendanceDTO());
        }

        m_dateMap.get(dateInDDMMMYY).add(record);

    }

    /**
     * Calculate the details from the attendance data
     * 
     * @param queryStartDate
     * @param queryEndDate
     * @param holidays
     * @param mapForLeaves
     */
    public void doPostProcessingOnData(Date queryStartDate, Date queryEndDate,
            Map<String, LeaveRequestStatus> mapForLeaves, Set<String> holidays) {

        Calendar startCal = Calendar.getInstance();
        startCal.setTime(queryStartDate);
        Calendar endCal = Calendar.getInstance();
        endCal.setTime(queryEndDate);
        long totalMins = 0;

        for (Calendar date = startCal; date.before(endCal) || date.equals(endCal); date.add(Calendar.DATE, 1)) {

            int dayOfWeek = date.get(Calendar.DAY_OF_WEEK);

            String day = DateUtility.getDateInDDMMMYY(date.getTime());
            PerDayAttendanceDTO perDayDTO = m_dateMap.get(day);

            List<AttendanceRecord> genTSList;
            if (perDayDTO == null || (genTSList = perDayDTO.getAttendanceEntries()).size() < 2) {

                // TODO: Add roster flow here to see if the person needed to be
                // present on this holiday
                // Don't mark it absent if there is no entry, if it is a
                // saturday or sunday.
                if (perDayDTO == null || perDayDTO.getAttendanceEntries().isEmpty()) {
                    if (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY) {
                        continue;
                    }

                }

                boolean isAbsent = false;
                if ((mapForLeaves != null && mapForLeaves.get(day) == LeaveRequestStatus.APPROVED)
                        || (holidays != null && holidays.contains(day))) {
                    if (holidays == null || !holidays.contains(day)) {
                        m_approved++;
                    }
                } else {
                    // if there is only 1 entry for the day, or no entry for the
                    // day,
                    // mark the person as absent
                    m_numAbsent++;
                    isAbsent = true;
                }

                if (perDayDTO == null) {
                    m_dateMap.put(day, new PerDayAttendanceDTO(isAbsent ? AttendanceEnum.ABSENT
                            : AttendanceEnum.APPROVED));
                } else {
                    perDayDTO.setState(isAbsent ? AttendanceEnum.ABSENT : AttendanceEnum.APPROVED);
                }
                continue;
            }

            Collections.sort(genTSList, new Comparator<AttendanceRecord>() {

                @Override
                public int compare(AttendanceRecord o1, AttendanceRecord o2) {
                    try {
                        Date date1 = o1.getKey().getGenTs();
                        Date date2 = o2.getKey().getGenTs();
                        return date1.compareTo(date2);
                    } catch (Exception e) {
                    }
                    return 0;
                }
            });

            Date startDate;
            Date endDate;
            try {
                startDate = genTSList.get(0).getKey().getGenTs();
                endDate = genTSList.get(genTSList.size() - 1).getKey().getGenTs();
                long diff = endDate.getTime() - startDate.getTime();
                long hours = TimeUnit.HOURS.convert(diff, TimeUnit.MILLISECONDS);
                long mins = (TimeUnit.MINUTES.convert(diff, TimeUnit.MILLISECONDS)) % 60;

                m_numHours += hours;
                totalMins += mins;
                perDayDTO.setHours((int) hours);
                perDayDTO.setMinutes((int) mins);
                if (hours >= MIN_HOURS) {
                    m_numPresent++;
                    perDayDTO.setState(AttendanceEnum.PRESENT);
                } else {
                    // giving 15 mins grace
                    if (((MIN_HOURS * 60) - ((hours * 60) + mins)) <= 15) {
                        m_numPresent++;
                        perDayDTO.setState(AttendanceEnum.PRESENT);
                    } else if (hours >= MIN_HOURS_HALF_DAY) {
                        m_numHalfDays++;
                        perDayDTO.setState(AttendanceEnum.HALFDAY);
                    } else {
                        // giving 15 mins grace
                        if (((MIN_HOURS_HALF_DAY * 60) - ((hours * 60) + mins)) <= 15) {
                            m_numHalfDays++;
                            perDayDTO.setState(AttendanceEnum.HALFDAY);
                        } else {
                            m_numAbsent++;
                            perDayDTO.setState(AttendanceEnum.ABSENT);
                        }
                    }
                }
            } catch (Exception e) {
                s_logger.error("Error while getting the number of hours for user: " + m_user.getEmail() + ", date: "
                        + day, e);
                m_numAbsent++;
            }
        }

        m_numHours += (totalMins / 60);
    }

    /**
     * @return the dateMap
     */
    public Map<String, PerDayAttendanceDTO> getDateMap() {
        return m_dateMap;
    }

    /**
     * @return the numAbsent
     */
    public int getNumAbsent() {
        return m_numAbsent;
    }

    /**
     * @return the numPresent
     */
    public int getNumPresent() {
        return m_numPresent;
    }

    /**
     * @return the numHalfDays
     */
    public int getNumHalfDays() {
        return m_numHalfDays;
    }

    public JsonObject convertToJsonObject() {
        try {
            JsonObject obj = new JsonObject();
            obj.addProperty("user_id", m_user.getUserId());
            obj.addProperty("name", StringUtility.trimAndNullIsEmpty(m_user.getUserProfile().getName()));
            obj.addProperty("email", StringUtility.trimAndNullIsEmpty(m_user.getEmail()));
            obj.addProperty("present", m_numPresent);
            obj.addProperty("absent", m_numAbsent);
            obj.addProperty("approved", m_approved);
            obj.addProperty("halfday", m_numHalfDays);
            obj.addProperty("hours", m_numHours);
            return obj;
        } catch (Exception e) {
            s_logger.error("Error in creating json object for the attendance DTO", e);
        }

        return null;
    }

    /**
     * @return json array per user data to be displayed on the calendar
     */
    private JsonArray convertToJsonCalendarArray() {
        JsonArray array = new JsonArray();
        try {
            for (String day : m_dateMap.keySet()) {
                PerDayAttendanceDTO perDayDTO = m_dateMap.get(day);

                String convertddMMMyyToYYMMDD = DateUtility.convertddMMMyyToYYMMDD(day);
                if (perDayDTO == null || perDayDTO.getAttendanceEntries() == null
                        || perDayDTO.getAttendanceEntries().isEmpty()) {
                    JsonObject obj = new JsonObject();
                    AttendanceEnum enumVal = AttendanceEnum.ABSENT;

                    if (perDayDTO != null && perDayDTO.getState() != null) {
                        enumVal = perDayDTO.getState();
                    }
                    obj.addProperty("title", enumVal.name());
                    obj.addProperty("start", convertddMMMyyToYYMMDD);
                    obj.addProperty("allDay", true);
                    obj.addProperty("backgroundColor", enumVal.getEventRenderingColor());
                    array.add(obj);
                } else {
                    String backgroundColor = perDayDTO.getState() != null ? perDayDTO.getState()
                            .getEventRenderingColor() : "";
                    JsonObject obj = new JsonObject();

                    String time = perDayDTO.getHours() + "H" + perDayDTO.getMinutes() + "M";

                    obj.addProperty("title", perDayDTO.getState().name() + " " + time);

                    obj.addProperty("start", convertddMMMyyToYYMMDD);
                    obj.addProperty("allDay", true);
                    obj.addProperty("backgroundColor", backgroundColor);
                    array.add(obj);

                    List<AttendanceRecord> attendanceRecords = perDayDTO.getAttendanceEntries();
                    obj = new JsonObject();
                    obj.addProperty("title", "In-Time");
                    obj.addProperty("start",
                            DateUtility.getDateInDashColonYYYYMMDDHHMMSS(attendanceRecords.get(0).getKey().getGenTs()));
                    array.add(obj);
                    if (attendanceRecords.size() > 1) {
                        obj = new JsonObject();
                        obj.addProperty("title", "Out-Time");
                        obj.addProperty(
                                "start",
                                DateUtility.getDateInDashColonYYYYMMDDHHMMSS(attendanceRecords
                                        .get(attendanceRecords.size() - 1).getKey().getGenTs()));
                        array.add(obj);
                    }

                    // for (int i = 1; i < attendanceRecords.size() - 1; i++) {
                    // AttendanceRecord attendanceRecord =
                    // attendanceRecords.get(i);
                    //
                    // obj = new JsonObject();
                    // obj.addProperty("title", "Device-id:" +
                    // attendanceRecord.getKey().getDeviceId());
                    // obj.addProperty("start",
                    // DateUtility.getDateInDashColonYYYYMMDDHHMMSS(attendanceRecord.getKey().getGenTs()));
                    // array.add(obj);
                    // }
                }
            }

            return array;
        } catch (Exception e) {
            s_logger.error("Error in creating json object for the attendance DTO", e);
        }

        return null;
    }

    public static JsonArray convertToUserSpecificJsonArray(List<AttendanceRecord> list, Date startDate, Date endDate,
            List<LeaveRequest> leaveRequests, List<YearlyCalendar> holidays) {
        JsonArray result = new JsonArray();
        if (list == null || list.isEmpty()) {
            return result;
        }
        try {

            Map<User, AttendanceDTO> records = getUserToDTOMap(list);

            if (records != null) {

                Map<User, Map<String, LeaveRequestStatus>> mapForLeaves = convertLeaveRequestsToMap(leaveRequests);
                Set<String> flattenHolidays = flattenHolidayCalendar(holidays);

                for (User user : records.keySet()) {
                    AttendanceDTO attendanceDTO = records.get(user);

                    attendanceDTO.doPostProcessingOnData(startDate, endDate, (mapForLeaves == null ? null
                            : mapForLeaves.get(user)), flattenHolidays);

                    JsonObject jsonObject = attendanceDTO.convertToJsonObject();
                    if (jsonObject != null) {
                        result.add(jsonObject);
                    }
                }
            }

        } catch (Exception e) {
            s_logger.error("Error while creating json string for attendance", e);
            result = new JsonArray();
        }

        return result;

    }

    private static Set<String> flattenHolidayCalendar(List<YearlyCalendar> holidays) {
        if (holidays == null || holidays.isEmpty()) {
            return null;
        }

        Set<String> set = new HashSet<String>();
        for (YearlyCalendar holiday : holidays) {
            Calendar startCal = Calendar.getInstance();
            startCal.setTime(holiday.getStartDate());

            Calendar endCal = Calendar.getInstance();
            endCal.setTime(holiday.getEndDate());

            for (Calendar date = startCal; date.before(endCal); date.add(Calendar.DATE, 1)) {
                String dateInDDMMMYY = DateUtility.getDateInDDMMMYY(date.getTime());
                set.add(dateInDDMMMYY);
            }
        }
        return set;
    }

    private static Map<User, Map<String, LeaveRequestStatus>> convertLeaveRequestsToMap(List<LeaveRequest> leaveRequests) {
        if (leaveRequests == null || leaveRequests.isEmpty()) {
            return null;
        }

        Map<User, Map<String, LeaveRequestStatus>> outerMap = new HashMap<User, Map<String, LeaveRequestStatus>>();

        for (LeaveRequest request : leaveRequests) {
            User user = request.getRequestor();
            if (!outerMap.containsKey(user)) {
                outerMap.put(user, new HashMap<String, LeaveRequestStatus>());
            }

            Map<String, LeaveRequestStatus> innerMap = outerMap.get(user);
            Calendar startCal = Calendar.getInstance();
            startCal.setTime(request.getStartDate());

            Calendar endCal = Calendar.getInstance();
            endCal.setTime(request.getEndDate());

            for (Calendar date = startCal; date.before(endCal); date.add(Calendar.DATE, 1)) {
                String dateInDDMMMYY = DateUtility.getDateInDDMMMYY(date.getTime());
                if (innerMap.get(dateInDDMMMYY) != LeaveRequestStatus.APPROVED) {
                    innerMap.put(dateInDDMMMYY, request.getRequestStatus());
                }
            }

        }

        return outerMap;
    }

    /**
     * @param list
     * @return
     * @throws Exception
     */
    private static Map<User, AttendanceDTO> getUserToDTOMap(List<AttendanceRecord> list) throws Exception {
        Map<User, AttendanceDTO> records = new HashMap<User, AttendanceDTO>();
        for (AttendanceRecord record : list) {
            User employee = record.getKey().getEmployee();
            if (!records.containsKey(employee)) {
                records.put(employee, new AttendanceDTO());
            }
            records.get(employee).add(record);
        }
        return records;
    }

    /**
     * @param list
     * @param startDate
     * @param endDate
     * @param leaveRequests
     * @param holidays
     * @return array of json objects. Json Object -> user data + attendance
     *         records as array
     */
    public static JsonArray convertToCalendarArray(List<AttendanceRecord> list, Date startDate, Date endDate,
            List<LeaveRequest> leaveRequests, List<YearlyCalendar> holidays) {
        JsonArray result = new JsonArray();
        if (list == null || list.isEmpty()) {
            return result;
        }
        try {

            Map<User, AttendanceDTO> employeeToAttendanceRecordsMap = getUserToDTOMap(list);

            if (employeeToAttendanceRecordsMap != null) {

                Map<User, Map<String, LeaveRequestStatus>> employeeToLeaveRequestMap = convertLeaveRequestsToMap(leaveRequests);
                Set<String> flattenHolidays = flattenHolidayCalendar(holidays);

                for (User user : employeeToAttendanceRecordsMap.keySet()) {
                    JsonObject obj = new JsonObject();

                    AttendanceDTO attendanceDTO = employeeToAttendanceRecordsMap.get(user);
                    attendanceDTO.doPostProcessingOnData(startDate, endDate, employeeToLeaveRequestMap == null ? null
                            : employeeToLeaveRequestMap.get(user), flattenHolidays);
                    JsonArray jsonArray = attendanceDTO.convertToJsonCalendarArray();
                    if (jsonArray != null) {
                        obj.add("user", user.convertToIdNameJsonObject());
                        obj.add("data", jsonArray);
                        result.add(obj);
                    }

                    // Should work for single user only
                    // break;
                }
            }

        } catch (Exception e) {
            s_logger.error("Error while creating json string for attendance", e);
            result = new JsonArray();
        }

        return result;
    }
}
