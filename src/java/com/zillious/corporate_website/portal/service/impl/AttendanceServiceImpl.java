package com.zillious.corporate_website.portal.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.zillious.corporate_website.portal.dao.AttendanceDao;
import com.zillious.corporate_website.portal.dao.UserDao;
import com.zillious.corporate_website.portal.service.AttendanceService;
import com.zillious.corporate_website.portal.service.LeavesPolicyService;
import com.zillious.corporate_website.portal.service.LeavesService;
import com.zillious.corporate_website.portal.service.UserService;
import com.zillious.corporate_website.portal.ui.EventType;
import com.zillious.corporate_website.portal.ui.dto.AttendanceDTO;
import com.zillious.corporate_website.portal.ui.model.AttendanceRecord;
import com.zillious.corporate_website.portal.ui.model.AttendanceRecord.AttendancePK;
import com.zillious.corporate_website.portal.ui.model.LeaveRequest;
import com.zillious.corporate_website.portal.ui.model.Team;
import com.zillious.corporate_website.portal.ui.model.User;
import com.zillious.corporate_website.portal.ui.model.YearlyCalendar;
import com.zillious.corporate_website.portal.utility.CollectionsUtility;
import com.zillious.corporate_website.portal.utility.JsonUtility;
import com.zillious.corporate_website.ui.navigation.WebsiteException;
import com.zillious.corporate_website.ui.navigation.WebsiteExceptionType;
import com.zillious.corporate_website.utils.DateUtility;

/**
 * @author nishant.gupta
 *
 */
@Service
public class AttendanceServiceImpl implements AttendanceService {

    private static Logger       s_logger = Logger.getLogger(AttendanceServiceImpl.class);

    @Autowired
    private AttendanceDao       m_attendanceDao;

    @Autowired
    private UserService         m_userService;

    @Autowired
    private LeavesService       m_leavesService;

    @Autowired
    private LeavesPolicyService m_leavesPolicyService;

    @Autowired
    private UserDao m_userDao;


    /*
     * (non-Javadoc)
     * 
     * @see com.zillious.corporate_website.portal.service.AttendanceService#
     * getAttendanceData(java.lang.String)
     */
    @Override
    @Transactional
    public JsonObject getAttendanceData(String requestJson, User requestor) {
        JsonObject finalJson = new JsonObject();
        boolean isSuccess = false;
        String message = null;
        try {
            JsonParser parser = new JsonParser();
            JsonObject requestObject = (JsonObject) parser.parse(requestJson);

            JsonElement jsonElement = requestObject.get("user_id");
            User specificUser = null;
            if (jsonElement != null) {
                int forUserId = jsonElement.getAsInt();
                specificUser = new User(forUserId);
            }

            Set<User> forUser = null;

            // only admin role user can view the attendance of all users
            if (!requestor.getUserRole().isAdminRole()) {
                requestor = m_userService.getUser(requestor.getUserId());

                if (specificUser == null || (specificUser != null && specificUser.getUserId() == requestor.getUserId())) {
                    forUser = new HashSet<User>();
                    forUser.add(requestor);
                }

                if (specificUser == null || (forUser == null)) {
                    Set<Team> managedTeams = requestor.getManagedTeams();
                    if (managedTeams != null) {
                        for (Team managedTeam : managedTeams) {
                            Set<User> members = managedTeam.getMembers();
                            for (User member : members) {
                                if (specificUser == null
                                        || (specificUser != null && specificUser.getUserId() == member.getUserId())) {
                                    if (forUser == null) {
                                        forUser = new HashSet<User>();
                                    }
                                    forUser.add(member);
                                }
                            }
                        }
                    }
                }
            } else if (specificUser != null) {
                if (forUser == null) {
                    forUser = new HashSet<User>();
                }
                specificUser = m_userService.getUser(specificUser.getUserId());
                forUser.add(specificUser);
            }

            String startDateFromJson = requestObject.get("startDate").getAsString();
            String endDateFromJson = requestObject.get("endDate").getAsString();

            Date startDate = DateUtility.parseDateInMMDDYYYYDash(startDateFromJson);
            Date endDate = DateUtility.parseDateInMMDDYYYYDash(endDateFromJson);

            List<LeaveRequest> leaveRequests = m_leavesService.getLeaveRequestDataForUser(forUser, startDate, endDate);

            List<YearlyCalendar> holidays = m_leavesPolicyService.getEventsForDateRange(EventType.HOLIDAY, startDate,
                    endDate);

            JsonArray data = getAttendanceGist(forUser, startDate, endDate, leaveRequests, holidays);

            finalJson.add("attendance_data", data);
            isSuccess = true;
        } catch (Exception e) {
            if (e instanceof WebsiteException) {
                message = ((WebsiteException) e).getType().getDesc();
            } else {
                message = e.getMessage();
            }
        }

        JsonUtility.createResponseJson(finalJson, isSuccess, message);
        return finalJson;
    }

    @Override
    @Transactional
    public JsonObject getCalendarData(String requestJson, User requestor) {
        JsonObject finalJson = new JsonObject();
        boolean isSuccess = false;
        String message = null;
        try {
            JsonParser parser = new JsonParser();
            JsonObject requestObject = (JsonObject) parser.parse(requestJson);

            Set<User> forUser = null;

            // user_id is mandatory here
            // converting user id to list of users for which calendar data is
            // requested
            JsonElement requestUsers = requestObject.get("users");
            JsonArray userArray = null;
            if (requestUsers == null) {
                throw new WebsiteException(WebsiteExceptionType.MISSINGDATA);
            }

            userArray = requestUsers.getAsJsonArray();
            // int userId = requestObject.get("user_id").getAsInt();

            boolean isSelfAttendanceRequest = true;
            boolean isRequestorAdminRoleUser = requestor.getUserRole().isAdminRole();

            if (!isRequestorAdminRoleUser) {
                if (userArray != null && userArray.size() == 1) {
                    JsonObject userElement = (JsonObject) userArray.get(0);
                    int userId = userElement.get("user_id").getAsInt();
                    if (requestor.getUserId() != userId) {
                        isSelfAttendanceRequest = false;
                    }
                }
            }

            // manager can see the leaves of the team members
            if (!isSelfAttendanceRequest && !isRequestorAdminRoleUser) {
                // requestor = m_userService.getUser(requestor.getUserId());
                List<User> userList = new ArrayList<User>();
                for (int i = 0; i < userArray.size(); i++) {
                    JsonElement jsonElement = userArray.get(i);
                    userList.add(new User(jsonElement.getAsJsonObject().get("user_id").getAsInt()));
                }
                forUser = new HashSet<User>();
                Map<Integer, User> usersToFetch = m_userService.getUsers(userList);
                for (User user : usersToFetch.values()) {
                    for (Team team : user.getTeamlist()) {
                        if (team.getSupervisor().equals(requestor)) {
                            forUser.add(user);
                            isSelfAttendanceRequest = true;
                            break;
                        }
                    }
                }
            }

            if (!isSelfAttendanceRequest) {
                throw new WebsiteException(WebsiteExceptionType.INVALID_ACCESS);
            }

            // saving 1 hit
            if (CollectionsUtility.isCollectionEmpty(forUser)) {
                forUser = new HashSet<User>();
                List<User> userList = new ArrayList<User>();
                for (int i = 0; i < userArray.size(); i++) {
                    JsonElement jsonElement = userArray.get(i);
                    userList.add(new User(jsonElement.getAsJsonObject().get("user_id").getAsInt()));
                }
                forUser = new HashSet<User>();
                Map<Integer, User> usersToFetch = m_userService.getUsers(userList);
                if (!CollectionsUtility.isCollectionEmpty(usersToFetch)) {
                    forUser.addAll(usersToFetch.values());
                } else {
                    forUser = null;
                }
            }

            if (forUser == null) {
                throw new WebsiteException(WebsiteExceptionType.MISSING_USER);
            }

            String startDateFromJson = requestObject.get("startDate").getAsString();
            String endDateFromJson = requestObject.get("endDate").getAsString();

            Date startDate = DateUtility.parseDateInMMDDYYYYDash(startDateFromJson);
            Date endDate = DateUtility.parseDateInMMDDYYYYDash(endDateFromJson);
            JsonArray data = null;
            if (startDate != null && endDate != null && (startDate.before(endDate) || startDate.equals(endDate))) {
                List<AttendanceRecord> attendanceRecordsList = m_attendanceDao.getAttendanceRecords(startDate, endDate,
                        forUser);

                List<LeaveRequest> leaveRequests = m_leavesService.getLeaveRequestDataForUser(forUser, startDate,
                        endDate);

                List<YearlyCalendar> holidayList = m_leavesPolicyService.getEventsForDateRange(EventType.HOLIDAY,
                        startDate, endDate);

                if (attendanceRecordsList != null) {
                    data = AttendanceDTO.convertToCalendarArray(attendanceRecordsList, startDate, endDate,
                            leaveRequests, holidayList);
                }

                JsonArray leaveRequestsJson = LeaveRequest.getLeaveRequestsJson(leaveRequests);
                data.addAll(leaveRequestsJson);
                finalJson.add("leavesData", leaveRequestsJson);
                JsonArray holidays = m_leavesPolicyService.getCalendarEventsAsJson(holidayList);
                if (holidays != null) {
                    data.addAll(holidays);
                    finalJson.add("holidays", holidays);
                }
            }

            finalJson.add("calendar_data", data);
            isSuccess = true;
        } catch (Exception e) {
            if (e instanceof WebsiteException) {
                message = ((WebsiteException) e).getType().getDesc();
            } else {
                message = e.getMessage();
            }
        }

        JsonUtility.createResponseJson(finalJson, isSuccess, message);
        return finalJson;
    }

    /**
     * @param forUser
     * @param startDate
     * @param endDate
     * @param holidays
     * @param leaveRequests
     * @param data
     * @return
     */
    private JsonArray getAttendanceGist(Set<User> forUser, Date startDate, Date endDate,
            List<LeaveRequest> leaveRequests, List<YearlyCalendar> holidays) {
        JsonArray data = null;
        if (startDate != null && endDate != null && (startDate.before(endDate) || startDate.equals(endDate))) {
            List<AttendanceRecord> list = m_attendanceDao.getAttendanceRecords(startDate, endDate, forUser);
            s_logger.debug(list == null ? "null" : list.size());
            if (list != null) {
                data = AttendanceDTO.convertToUserSpecificJsonArray(list, startDate, endDate, leaveRequests, holidays);
            }
        }
        return data;
    }
    @Override
    @Transactional
    public JsonObject addManualAttendanceEntry(String requestJson, String ipAddress) {
        JsonObject response = new JsonObject();
        try {
            JsonParser parser = new JsonParser();
            JsonObject requestObject = (JsonObject) parser.parse(requestJson);
            JsonObject employeeObject = requestObject.get("employee").getAsJsonObject();
            int userId = employeeObject.get("id").getAsInt();

            JsonObject attendanceObject = requestObject.get("datetime").getAsJsonObject();
            String date = attendanceObject.get("date").getAsString();
            JsonArray time = attendanceObject.get("time").getAsJsonArray();

            User user = m_userService.getUser(userId);
            if (user == null) {
                throw new WebsiteException(WebsiteExceptionType.INVALID_ACCESS);
            }

            boolean isSaved = false;
            for(JsonElement t : time){
                JsonObject entryObject = t.getAsJsonObject();
                JsonElement entryTime = entryObject.get("entryTime");
                JsonElement timeType = entryObject.get("name");
                String dateTimeString = date + " " + entryTime.getAsString();
                Date genTs = DateUtility.parseDateInDDMMYYYYHHMMNullIfError(dateTimeString);
                if (genTs == null) {
                    throw new WebsiteException(WebsiteExceptionType.INVALID_REQUEST);
                }
                AttendanceRecord record = new AttendanceRecord();
                record.setIpAddress(ipAddress);
                AttendancePK key = new AttendancePK();
                key.setGenTs(genTs);
                key.setEmployee(user);
                key.setDeviceId("1");
                record.setKey(key);
                List<AttendanceRecord> recordsToBeDeleted = getAttendanceRecordsToBeDeleted(userId ,genTs , timeType.getAsString());
                
                if(recordsToBeDeleted != null && !recordsToBeDeleted.isEmpty()){
                    for(AttendanceRecord recordToBeDeleted : recordsToBeDeleted){
                        m_attendanceDao.deleteAttendanceEntry(recordToBeDeleted);
                    } 
                }
                isSaved = m_attendanceDao.saveAttendanceEntry(record);     
            }          

            response = new JsonObject();
            JsonUtility.createResponseJson(response, isSaved, isSaved ? null : "Error in saving attendance entry");            

        } catch (Exception e) {
            s_logger.error("Error while adding manual attendance entry into the database", e);
            response = new JsonObject();
            JsonUtility.createResponseJson(response, false, WebsiteExceptionType.INVALID_REQUEST.getDesc()); 
        }
        return response;
    }

    private List<AttendanceRecord> getAttendanceRecordsToBeDeleted(int userId, Date genTs, String asString) {
        List<AttendanceRecord> recordsToBeDeleted = null;
        User user = m_userDao.getUser(userId);
        if(user!=null){
            // fetch records where gen_ts in db < maxDate and gen_ts in db > minDate
            if(asString.equals("in_time")){   
                Date minDate = DateUtility.getAndParseDateInDDMMYYYYDash(genTs);
                Date maxDate = genTs;
                recordsToBeDeleted = m_attendanceDao.getAttendanceRecordsForUser(minDate, maxDate, user);            
            }
            if(asString.equals("out_time")){
                Date minDate = genTs;
                Date nextDayDate = DateUtility.getDateBeforeOrAfterNumberOfDays(minDate, 1);
                Date maxDate = DateUtility.getDateWithZeroTimefields(nextDayDate);
                recordsToBeDeleted = m_attendanceDao.getAttendanceRecordsForUser(minDate, maxDate, user);
            }
        }
        return recordsToBeDeleted;
    }
}
