package com.zillious.corporate_website.portal.service.impl;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.zillious.corporate_website.portal.dao.LeavesDao;
import com.zillious.corporate_website.portal.entity.EmailMessage;
import com.zillious.corporate_website.portal.service.LeavesPolicyService;
import com.zillious.corporate_website.portal.service.LeavesService;
import com.zillious.corporate_website.portal.service.UserService;
import com.zillious.corporate_website.portal.ui.LeaveRequestStatus;
import com.zillious.corporate_website.portal.ui.model.LeavePolicy;
import com.zillious.corporate_website.portal.ui.model.LeaveRequest;
import com.zillious.corporate_website.portal.ui.model.Team;
import com.zillious.corporate_website.portal.ui.model.User;
import com.zillious.corporate_website.portal.utility.JsonUtility;
import com.zillious.corporate_website.ui.navigation.WebsiteException;
import com.zillious.corporate_website.ui.navigation.WebsiteExceptionType;
import com.zillious.corporate_website.utils.DateUtility;
import com.zillious.corporate_website.utils.EmailSender;
import com.zillious.corporate_website.utils.StringUtility;

/**
 * @author nishant.gupta
 *
 */
@Service
public class LeavesServiceImpl implements LeavesService {

    @Autowired
    private UserService         m_userService;

    @Autowired
    private LeavesDao           m_leavesDao;

    @Autowired
    private LeavesPolicyService m_leavesPolicyService;

    private static Logger       s_logger = Logger.getLogger(LeavesServiceImpl.class);

    @Override
    @Transactional
    public JsonObject getLeaveRequestDataForUser(int userId, String year) {
       User user = m_userService.getUser(userId);
        JsonObject finalJson = new JsonObject();
        if (user == null) {
            JsonUtility.createResponseJson(finalJson, false, WebsiteExceptionType.MISSING_USER.getDesc());
            return finalJson;
        }
        JsonUtility.createResponseJson(finalJson, true, null);

        // Leaves requested by the current user
        JsonArray leaveRequestsJson = user.getLeaveRequestsJson(year);
        finalJson.addProperty("id", user.getUserId());
        finalJson.add("user_leaves", leaveRequestsJson);

        // Leaves requested by users that are part of the team that this user is
        // supervisor of
        JsonArray leaveRequestsByTeamMembers = null;

        if (user.getUserRole().isDirector()) {
            List<User> allUsers = m_userService.getAllUsers();
            if (allUsers != null) {
                leaveRequestsByTeamMembers = populateMemberLeaveRequestArray(leaveRequestsByTeamMembers, allUsers,
                        year, user);
            }
        } else {
            Set<Team> managedTeams = user.getManagedTeams();
            if (managedTeams != null) {
                for (Team managedTeam : managedTeams) {
                    Set<User> teamMembers = managedTeam.getMembers();
                    if (teamMembers != null) {
                        leaveRequestsByTeamMembers = populateMemberLeaveRequestArray(leaveRequestsByTeamMembers,
                                teamMembers, year, user);
                    }
                }
            }
        }

        if (leaveRequestsByTeamMembers != null) {
            finalJson.add("member_leaves", leaveRequestsByTeamMembers);
        }
        return finalJson;
    }

    /**
     * @param leaveRequestsByTeamMembers
     * @param users
     * @param year
     * @param user
     * @return
     */
    protected JsonArray populateMemberLeaveRequestArray(JsonArray leaveRequestsByTeamMembers, Collection<User> users,
            String year, User user) {
        boolean isDirector = user.getUserRole().isDirector();

        for (User teamMember : users) {

            // requestor cannot change the status of his own leaves.
            if (!isDirector && teamMember.getUserId() == user.getUserId()) {
                continue;
            }
            JsonArray leaveRequestsArrayByMember = teamMember.getLeaveRequestsJson(year);
            if (leaveRequestsArrayByMember != null && leaveRequestsArrayByMember.size() != 0) {
                if (leaveRequestsByTeamMembers == null) {
                    leaveRequestsByTeamMembers = new JsonArray();
                }
                leaveRequestsByTeamMembers.addAll(leaveRequestsArrayByMember);
            }
        }
        return leaveRequestsByTeamMembers;
    }

    @Override
    @Transactional
    public JsonObject addLeaveRequestDataForUser(String requestJson) {
        JsonParser parser = new JsonParser();
        String message = "";
        JsonObject finalJson = new JsonObject();
        boolean status = false;
        try {
            JsonObject requestElement = (JsonObject) parser.parse(requestJson);
            int userId = requestElement.get("user_id").getAsInt();
            User user = m_userService.getUser(userId);

            if (user == null) {
                throw new WebsiteException(WebsiteExceptionType.MISSING_USER);
            }

            LeaveRequest leaveRequest = new LeaveRequest();
            Set<String> holidayList = m_leavesPolicyService.getHolidayDates();
            leaveRequest.parseLeaveRequestDetailsFromJson(requestElement, holidayList);

            leaveRequest.setRaisedOn(DateUtility.getCurrentDate());

            if (leaveRequest.getDays() == 0) {
                throw new WebsiteException(WebsiteExceptionType.WRONG_DATES);
            }

            leaveRequest.setRequestor(user);
            leaveRequest.setRequestStatus(LeaveRequestStatus.PENDING);
            user.addLeaveRequest(leaveRequest);
            status = m_userService.updateUser(user);
            finalJson.addProperty("user_id", user.getUserId());

            // JsonObject leaveInfo = (JsonObject)
            // leaveRequest.convertToJsonObject();
            finalJson.add("leave_info", leaveRequest.convertToJsonObject());

            try {
                EmailMessage emailMessage = new EmailMessage();

                // To: user's team's supervisors
                Set<Team> teamlist = user.getTeamlist();
                if (teamlist != null) {
                    for (Team team : teamlist) {
                        User supervisor = team.getSupervisor();
                        emailMessage.addTo(supervisor.getEmail());
                    }
                }

                // CC: admin role users
                List<User> directors = m_userService.getAdminRoleUsers();
                if (directors != null) {
                    for (User director : directors) {
                        emailMessage.addCC(director.getEmail());
                    }
                }

                // subject
                emailMessage.setSubject("Leave Request - "
                        + StringUtility.trimAndNullIsDefault(user.getUserProfile().getName(), user.getEmail()));

                emailMessage.setContent(leaveRequest.getRequestEmailContent());
                emailMessage.addCC(user.getEmail());
                EmailSender.sendEmail(emailMessage);
            } catch (Exception e) {
                s_logger.error("Error in sending email message for leave request: " + leaveRequest.getId());
            }

        } catch (Exception e) {
            if (e instanceof WebsiteException) {
                message = ((WebsiteException) e).getType().getDesc();
            } else {
                message = e.getMessage();
            }
        }

        JsonUtility.createResponseJson(finalJson, status, message);
        return finalJson;
    }

    @Override
    @Transactional
    public JsonObject updateLeaveRequestDataForUser(String requestJson) {
        JsonParser parser = new JsonParser();
        String message = "";
        JsonObject finalJson = new JsonObject();
        boolean isSuccess = false;
        try {
            JsonObject requestElement = (JsonObject) parser.parse(requestJson);
            LeaveRequest leaveRequest = m_leavesDao.getLeaveRequest(requestElement.get("leave_id").getAsInt());

            if (leaveRequest == null) {
                throw new WebsiteException(WebsiteExceptionType.LEAVE_REQUEST_NOT_FOUND);
            }

            User user = leaveRequest.getRequestor();
            Set<String> holidayList = m_leavesPolicyService.getHolidayDates();
            leaveRequest.parseLeaveRequestDetailsFromJson(requestElement, holidayList);

            if (leaveRequest.getDays() == 0) {
                throw new WebsiteException(WebsiteExceptionType.WRONG_DATES);
            }

            user.addLeaveRequest(leaveRequest);
            leaveRequest.setRequestor(user);
            isSuccess = m_userService.updateUser(user);
            finalJson.addProperty("user_id", user.getUserId());
            // JsonObject leaveInfo = leaveRequest.getLeaveInfoJson();
            finalJson.add("leave_info", leaveRequest.convertToJsonObject());

            try {
                EmailMessage emailMessage = new EmailMessage();

                // To: user's team's supervisors
                Set<Team> teamlist = user.getTeamlist();
                if (teamlist != null) {
                    for (Team team : teamlist) {
                        User supervisor = team.getSupervisor();
                        emailMessage.addTo(supervisor.getEmail());
                    }
                }

                // CC: admin role users
                List<User> directors = m_userService.getAdminRoleUsers();
                if (directors != null) {
                    for (User director : directors) {
                        emailMessage.addCC(director.getEmail());
                    }
                }

                // subject
                emailMessage.setSubject("Leave Request Modification- "
                        + StringUtility.trimAndNullIsDefault(user.getUserProfile().getName(), user.getEmail()));

                emailMessage.setContent(leaveRequest.getRequestUpdateEmailContent());

                EmailSender.sendEmail(emailMessage);
            } catch (Exception e) {
                s_logger.error("Error in sending email message for leave request update: " + leaveRequest.getId());
            }

        } catch (Exception e) {
            s_logger.debug("Error while updating leave request: ", e);
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
    public JsonObject deleteLeaveRequest(String requestJson) {
        JsonParser parser = new JsonParser();
        JsonObject jobj = (JsonObject) parser.parse(requestJson);
        String errorMsg = "";
        JsonObject finalJSON = new JsonObject();
        boolean isSuccess = false;
        try {
            LeaveRequest leaveRequest = m_leavesDao.getLeaveRequest(jobj.get("leave_id").getAsInt());
            if (leaveRequest == null) {
                throw new WebsiteException(WebsiteExceptionType.LEAVE_REQUEST_NOT_FOUND);
            }
            User requestor = leaveRequest.getRequestor();
            requestor.getLeaveRequests().remove(leaveRequest);
            isSuccess = m_userService.updateUser(requestor);

            // JsonObject leaveInfo = leaveRequest.getLeaveInfoJson();
            finalJSON.add("leave_info", leaveRequest.convertToJsonObject());

            try {
                EmailMessage emailMessage = new EmailMessage();

                emailMessage.setFrom(requestor.getEmail());

                // To: user's team's supervisors
                Set<Team> teamlist = requestor.getTeamlist();
                if (teamlist != null) {
                    for (Team team : teamlist) {
                        User supervisor = team.getSupervisor();
                        emailMessage.addTo(supervisor.getEmail());
                    }
                }

                // CC: admin role users
                List<User> directors = m_userService.getAdminRoleUsers();
                if (directors != null) {
                    for (User director : directors) {
                        emailMessage.addCC(director.getEmail());
                    }
                }

                // subject
                emailMessage
                        .setSubject("Leave Request Cancelled - "
                                + StringUtility.trimAndNullIsDefault(requestor.getUserProfile().getName(),
                                        requestor.getEmail()));

                emailMessage.setContent(leaveRequest.getRequestDeleteEmailContent());

                EmailSender.sendEmail(emailMessage);
            } catch (Exception e) {
                s_logger.error("Error in sending email message for leave request cancelled: " + leaveRequest.getId());
            }

        } catch (Exception e) {
            s_logger.debug("error in deleting leaves request : ", e);
            if (e instanceof WebsiteException) {
                errorMsg = ((WebsiteException) e).getType().getDesc();
            } else {
                errorMsg = e.getMessage();
            }
        }

        JsonUtility.createResponseJson(finalJSON, isSuccess, errorMsg);
        return finalJSON;
    }

    @Override
    @Transactional
    public JsonObject updateLeaveRequestStatus(String requestJson) {
        JsonParser parser = new JsonParser();
        String message = "";
        JsonObject finalJson = new JsonObject();
        boolean isSuccess = false;
        try {
            JsonObject requestElement = (JsonObject) parser.parse(requestJson);
            LeaveRequest leaveRequest = m_leavesDao.getLeaveRequest(requestElement.get("leave_id").getAsInt());

            if (leaveRequest == null) {
                throw new WebsiteException(WebsiteExceptionType.LEAVE_REQUEST_NOT_FOUND);
            }

            int userId = requestElement.get("user_id").getAsInt();
            User servicingUser = m_userService.getUser(userId);
            if (servicingUser == null) {
                throw new WebsiteException(WebsiteExceptionType.USER_AUTH_FAILED);
            }

            String newStatusString = requestElement.get("status").getAsString();
            LeaveRequestStatus updatedStatus = LeaveRequestStatus.valueOf(newStatusString.trim());
            String additionalComments = requestElement.get("additionalComments") == null ? null : StringUtility
                    .trimAndEmptyIsNull(requestElement.get("additionalComments").getAsString());
            if (updatedStatus == leaveRequest.getRequestStatus() && additionalComments == null) {
                throw new RuntimeException("Unchanged Data");
            }

            LeaveRequestStatus prevStatus = leaveRequest.getRequestStatus();
            leaveRequest.setRequestStatus(updatedStatus);
            leaveRequest.setStatusChangedBy(servicingUser);
            leaveRequest.setAdditionalComments(additionalComments);

            isSuccess = m_leavesDao.update(leaveRequest);
            finalJson.addProperty("user_id", servicingUser.getUserId());
            finalJson.add("updated_leave", leaveRequest.convertToJsonObject());

            try {
                EmailMessage emailMessage = new EmailMessage();

                // subject
                emailMessage.setSubject("Leave Request Status Update");

                emailMessage.addTo(leaveRequest.getRequestor().getEmail());

                User user = m_userService.getUser(leaveRequest.getRequestor().getUserId());

                // To: user's team's supervisors
                Set<Team> teamlist = user.getTeamlist();
                if (teamlist != null) {
                    for (Team team : teamlist) {
                        User supervisor = team.getSupervisor();
                        emailMessage.addCC(supervisor.getEmail());
                    }
                }

                // CC: admin role users
                List<User> directors = m_userService.getAdminRoleUsers();
                if (directors != null) {
                    for (User director : directors) {
                        emailMessage.addCC(director.getEmail());
                    }
                }

                emailMessage.addCC(servicingUser.getEmail());
                emailMessage.setContent(leaveRequest.getRequestStatusUpdateEmailContent(prevStatus));

                EmailSender.sendEmail(emailMessage);
            } catch (Exception e) {
                s_logger.error("Error in sending email message for leave request status update: "
                        + leaveRequest.getId());
            }

        } catch (Exception e) {
            s_logger.debug("Error while updating leave request: ", e);
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
    public JsonObject sendReminderForTheLeaveRequest(String requestJson) {
        JsonParser parser = new JsonParser();
        String message = "";
        JsonObject finalJson = new JsonObject();
        boolean isSuccess = false;
        try {
            JsonObject requestElement = (JsonObject) parser.parse(requestJson);
            LeaveRequest leaveRequest = m_leavesDao.getLeaveRequest(requestElement.get("leave_id").getAsInt());

            if (leaveRequest == null) {
                throw new WebsiteException(WebsiteExceptionType.LEAVE_REQUEST_NOT_FOUND);
            }

            try {
                EmailMessage emailMessage = new EmailMessage();

                // subject
                emailMessage.setSubject("Leave Request Reminder");

                User user = leaveRequest.getRequestor();

                // To: user's team's supervisors
                Set<Team> teamlist = user.getTeamlist();
                if (teamlist != null) {
                    for (Team team : teamlist) {
                        User supervisor = team.getSupervisor();
                        emailMessage.addTo(supervisor.getEmail());
                    }
                }

                // CC: admin role users
                List<User> directors = m_userService.getAdminRoleUsers();
                if (directors != null) {
                    for (User director : directors) {
                        emailMessage.addCC(director.getEmail());
                    }
                }

                emailMessage.setContent(leaveRequest.getReminderEmailContent());

                EmailSender.sendEmail(emailMessage);
                isSuccess = true;
            } catch (Exception e) {
                s_logger.error("Error in sending email message for leave request status update: "
                        + leaveRequest.getId());
            }

        } catch (Exception e) {
            s_logger.debug("Error while updating leave request: ", e);
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
    public List<LeaveRequest> getLeaveRequestDataForUser(User user, Date startDate, Date endDate) {
        return m_leavesDao.getLeaveRequestDataForUser(user, startDate, endDate);
    }

    @Override
    @Transactional
    public List<LeaveRequest> getLeaveRequestDataForUser(Set<User> users, Date startDate, Date endDate) {
        return m_leavesDao.getLeaveRequestDataForUser(users, startDate, endDate);
    }

    @Override
    @Transactional
    public JsonObject getLeaveReportData(String requestJson, User requestor) {
        JsonObject finalJson = new JsonObject();
        boolean isSuccess = false;
        String message = null;
        try {
            JsonParser parser = new JsonParser();
            JsonObject requestObject = (JsonObject) parser.parse(requestJson);

            Set<User> forUser = null;
            // only admin role user can view the leave status of all users
            // non-admin user can only view leave requests for team members.
            // non-admin, non-team leader will only be able to check his own
            // info
            if (!requestor.getUserRole().isAdminRole()) {
                requestor = m_userService.getUser(requestor.getUserId());
                forUser = new HashSet<User>();
                forUser.add(requestor);
                Set<Team> managedTeams = requestor.getManagedTeams();
                if (managedTeams != null) {
                    for (Team managedTeam : managedTeams) {
                        Set<User> members = managedTeam.getMembers();
                        for (User member : members) {
                            forUser.add(member);
                        }
                    }
                }
            }

            String year = requestObject.get("year").getAsString();

            JsonArray data = null;
            if (year != null && !year.isEmpty()) {
                List<LeaveRequest> list = m_leavesDao.getLeaveRequests(year, forUser);
                s_logger.debug(list == null ? "leaves data  : null" : list.size());
                data = LeaveRequest.convertToLeavesReportJson(list);
            }

            finalJson.add("leaveReport_data", data);
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
    public JsonObject getLeaveReportCalendarData(String requestJson, User requestor) {
        JsonObject finalJson = new JsonObject();
        boolean isSuccess = false;
        String message = null;
        try {
            JsonParser parser = new JsonParser();
            JsonObject requestObject = (JsonObject) parser.parse(requestJson);

            // user_id is mandatory here
            int userId = requestObject.get("user_id").getAsInt();
            Set<User> forUser = null;

            boolean isValidAccess = true;

            if (requestor.getUserId() != userId && !requestor.getUserRole().isAdminRole()) {
                isValidAccess = false;
            }

            // supervisor can see the leaves of the team members
            if (!isValidAccess && !requestor.getUserRole().isAdminRole()) {
                requestor = m_userService.getUser(requestor.getUserId());
                Set<Team> managedTeams = requestor.getManagedTeams();
                if (managedTeams != null) {
                    forUser = new HashSet<User>();
                    for (Team managedTeam : managedTeams) {
                        Set<User> members = managedTeam.getMembers();
                        for (User member : members) {
                            if (member.getUserId() == userId) {
                                forUser.add(member);
                                isValidAccess = true;
                                break;
                            }
                        }
                    }
                }
            }

            if (!isValidAccess) {
                throw new WebsiteException(WebsiteExceptionType.INVALID_ACCESS);
            }

            // saving 1 hit
            if (forUser == null) {
                forUser = new HashSet<User>();
                User user = m_userService.getUser(userId);
                if (user != null) {
                    forUser.add(user);
                } else {
                    forUser = null;
                }
            }

            if (forUser == null) {
                throw new WebsiteException(WebsiteExceptionType.MISSING_USER);
            }

            String year = requestObject.get("year").getAsString();

            if (year != null && !year.isEmpty()) {
                List<LeaveRequest> list = m_leavesDao.getLeaveRequests(year, forUser);
                if (list != null) {
                    JsonArray leaveRequestsJson = LeaveRequest.getLeaveRequestsJson(list);
                    finalJson.add("calendar_data", leaveRequestsJson);

                    // Grouping on the basis of leave types
                    Map<LeavePolicy, Integer> map = new HashMap<LeavePolicy, Integer>();
                    for (LeaveRequest request : list) {
                        LeavePolicy leaveType = request.getLeaveType();

                        if (request.getRequestStatus().equals(LeaveRequestStatus.APPROVED)) {
                            int leaveDays = request.getDays();
                            if (map.get(leaveType) == null) {
                                map.put(leaveType, leaveDays);
                            } else {
                                map.put(leaveType, map.get(leaveType) + leaveDays);
                            }
                        }
                    }

                    JsonObject chartData = new JsonObject();
                    JsonArray leaveTypes = new JsonArray();
                    for (LeavePolicy policy : map.keySet()) {
                        JsonObject obj = new JsonObject();
                        obj.addProperty("label", policy.getName());
                        obj.addProperty("value", map.get(policy));
                        leaveTypes.add(obj);
                    }

                    chartData.add("leaveTypes", leaveTypes);

                    List<LeavePolicy> leavesPolicy = m_leavesPolicyService.getLeavesPolicy();
                    JsonArray leavesRemaining = new JsonArray();
                    for (LeavePolicy policy : leavesPolicy) {
                        JsonObject obj = new JsonObject();
                        obj.addProperty("label", policy.getName());
                        obj.addProperty("value", policy.getDays() - (map.get(policy) != null ? map.get(policy) : 0));
                        leavesRemaining.add(obj);
                    }

                    chartData.add("leavesRemaining", leavesRemaining);

                    finalJson.add("chart_data", chartData);
                }

            }

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

}
