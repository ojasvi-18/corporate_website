package com.zillious.corporate_website.portal.ui.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cascade;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.zillious.corporate_website.portal.entity.utility.LeaveRequestStatusSerializer;
import com.zillious.corporate_website.portal.ui.LeaveRequestStatus;
import com.zillious.corporate_website.utils.DateUtility;
import com.zillious.corporate_website.utils.StringUtility;

/**
 * @author nishant.gupta
 *
 */
@Entity
@Table(name = "leave_requests")
public class LeaveRequest implements DBObject {

    private static final long  serialVersionUID = -7596511782883986217L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int                m_id;

    @OneToOne(fetch = FetchType.LAZY)
    @Cascade(org.hibernate.annotations.CascadeType.PERSIST)
    @JoinColumn(name = "leave_type")
    private LeavePolicy        m_leaveType;

    @ManyToOne(fetch = FetchType.LAZY)
    @Cascade(org.hibernate.annotations.CascadeType.PERSIST)
    @JoinColumn(name = "employee_id", nullable = false)
    private User               m_requestor;

    @Column(name = "start_date", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date               m_startDate;

    @Column(name = "end_date", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date               m_endDate;

    @Column(name = "reason", nullable = false)
    private String             m_reason;

    @Column(name = "status", nullable = false)
    @Convert(converter = LeaveRequestStatusSerializer.class)
    private LeaveRequestStatus m_requestStatus;

    @OneToOne(fetch = FetchType.LAZY)
    @Cascade(org.hibernate.annotations.CascadeType.PERSIST)
    @JoinColumn(name = "changed_by", nullable = true)
    private User               m_statusChangedBy;

    @Column(name = "days", nullable = false)
    private int                m_days;

    @Column(name = "comments", nullable = true)
    private String             m_additionalComments;

    @Column(name = "gen_date", nullable = true)
    @Temporal(TemporalType.DATE)
    private Date               m_raisedOn;

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
        this.m_id = id;
    }

    /**
     * @return the leaveId
     */
    public LeavePolicy getLeaveType() {
        return m_leaveType;
    }

    /**
     * @param leaveId the leaveId to set
     */
    public void setLeaveType(LeavePolicy leaveId) {
        m_leaveType = leaveId;
    }

    /**
     * @return the requestor
     */
    public User getRequestor() {
        return m_requestor;
    }

    /**
     * @param requestor the requestor to set
     */
    public void setRequestor(User requestor) {
        m_requestor = requestor;
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
     * @return the reason
     */
    public String getReason() {
        return m_reason;
    }

    /**
     * @param reason the reason to set
     */
    public void setReason(String reason) {
        m_reason = reason;
    }

    /**
     * @return the requestStatus
     */
    public LeaveRequestStatus getRequestStatus() {
        return m_requestStatus;
    }

    /**
     * @param requestStatus the requestStatus to set
     */
    public void setRequestStatus(LeaveRequestStatus requestStatus) {
        m_requestStatus = requestStatus;
    }

    /**
     * @return the statusChangedBy
     */
    public User getStatusChangedBy() {
        return m_statusChangedBy;
    }

    /**
     * @param statusChangedBy the statusChangedBy to set
     */
    public void setStatusChangedBy(User statusChangedBy) {
        m_statusChangedBy = statusChangedBy;
    }

    public String getRequestEmailContent() {
        StringBuilder content = new StringBuilder("Hi");
        content.append("\r\n");
        content.append(
                StringUtility.trimAndNullIsDefault(m_requestor.getUserProfile().getName(), m_requestor.getEmail()))
                .append(" has a raised a request for: ").append(m_leaveType.getName()).append("\r\n");
        content.append("Start Date: ").append(DateUtility.getDateInDDMMMYYDashSeparator(m_startDate)).append("\r\n");

        // long numDays = TimeUnit.DAYS.convert(m_endDate.getTime() -
        // m_startDate.getTime(), TimeUnit.MILLISECONDS);
        // populateDays(holidayList);

        int numDays = getDays();

        if (numDays > 1) {
            content.append("End Date: ").append(DateUtility.getDateInDDMMMYYDashSeparator(m_endDate))
                    .append("(Not including)").append("\r\n");
        }

        content.append("Number of Days: ").append(numDays).append("\r\n");

        content.append("Reason:").append(m_reason).append("\r\n");
        content.append("Please login to the portal to approve/decline the request").append("\r\n").append("\r\n")
                .append("\r\n");

        content.append("Thanks").append("\r\n").append("Zillious Solutions Pvt Ltd");
        return content.toString();
    }

    /**
     * Checks whether the requested date(s) falls on any of the mentioned
     * holidays or not
     * 
     * @param holidayList
     * @param isSandwich
     */
    public void populateDays(Set<String> holidayList, boolean isSandwich) {
        m_days = 0;

        Calendar start = Calendar.getInstance();
        start.setTime(m_startDate);

        Calendar end = Calendar.getInstance();
        end.setTime(m_endDate);

        int startWeek = DateUtility.getWeekOfMonth(start.getTime());
        int endWeek = DateUtility.getWeekOfMonth(end.getTime());

        boolean isWeekend = false;
        boolean isFriday = false;
        boolean isMonday = false;
        int leavesOnSatOrSun = 0;

        while (!start.equals(end)) {
            Date targetDay = start.getTime();

            switch (start.get(Calendar.DAY_OF_WEEK)) {
            case Calendar.FRIDAY:
                isFriday = true;
                break;
            case Calendar.MONDAY:
                isMonday = true;
                break;
            case Calendar.SATURDAY:
            case Calendar.SUNDAY:
                isWeekend = true;
                break;
            default:
                isWeekend = false;
                isFriday = false;
                isMonday = false;
            }

            String currentDate = DateUtility.getDateInDDMMMYY(targetDay);
            if (!isWeekend) {
                if (!holidayList.contains(currentDate)) {
                    m_days++;
                }

            } else if (isWeekend && isSandwich) {
                if ((startWeek != endWeek)) {

                    // adding 2 because this check is for sat-sun
                    if ((isFriday && isMonday)) {
                        m_days = m_days + 2;
                        m_days = m_days - leavesOnSatOrSun;
                    }
                    // checks for leaves on sat sun
                    else if (holidayList.contains(currentDate)) {
                        leavesOnSatOrSun++;
                    }
                }
            }

            // adding for monday explicitly as for monday isWeekend!=false
            if (isMonday) {
                if (!holidayList.contains(currentDate)) {
                    m_days++;
                }
            }

            start.add(Calendar.DATE, 1);
        }
    }

    /**
     * @return the days
     */
    public int getDays() {
        return m_days;
    }

    /**
     * @param days the days to set
     */
    public void setDays(int days) {
        m_days = days;
    }

    /**
     * @param requestElement
     * @param user
     * @param request
     * @param holidays
     * @return
     */
    public void parseLeaveRequestDetailsFromJson(JsonObject requestElement, Set<String> holidays) {
        LeaveRequest request = this;

        JsonObject leaveTypeJson = requestElement.get("leave_type").getAsJsonObject();
        LeavePolicy leaveType = LeavePolicy.parseFromJson(leaveTypeJson);

        request.setLeaveType(leaveType);
        request.setStartDate(DateUtility.parseDateInYYYYMMDDDashNullForException(requestElement
                .getAsJsonPrimitive("start").getAsString().replaceAll("^\"|\"$", "")));
        request.setEndDate(DateUtility.parseDateInYYYYMMDDDashNullForException(requestElement.getAsJsonPrimitive("end")
                .getAsString().replaceAll("^\"|\"$", "")));
        request.setReason(requestElement.get("reason").getAsString().replaceAll("^\"|\"$", ""));
        boolean isSandwich = leaveType.getIsSandwich();
        // leaveTypeJson.getAsJsonPrimitive("isSandwich").getAsBoolean();

        request.populateDays(holidays, isSandwich);
    }

    /**
     * Returns the json object to be returned on Manipulation of Leave request
     * (Add/update)
     * 
     * @return
     */
    public JsonElement convertToJsonObject() {
        JsonObject json = new JsonObject();
        json.addProperty("title", m_leaveType.getName() + " : " + m_days + " days");
        json.addProperty("leave_id", getId());
        json.add("leave_type", m_leaveType.convertToJson());
        json.add("requestor", m_requestor.convertToIdNameJsonObject());

        json.addProperty("start", DateUtility.getDateInYYYYMMDDDash(getStartDate()));
        json.addProperty("end", DateUtility.getDateInYYYYMMDDDash(getEndDate()));
        json.addProperty("raised", getRaisedOn() == null ? "UNSET" : DateUtility.getDateInYYYYMMDDDash(getRaisedOn()));
        json.addProperty("reason", m_reason);
        m_requestStatus.fillStatus(json);
        json.addProperty("days", m_days);

        if (m_additionalComments != null) {
            json.addProperty("additionalComments", m_additionalComments);
        }

        int days = DateUtility.getNumberOfDays(getStartDate());

        json.addProperty("canRemind", (days <= 2 && m_requestStatus == LeaveRequestStatus.PENDING));

        if (m_statusChangedBy != null) {
            json.add("changed_by", m_statusChangedBy.convertToIdNameJsonObject());
        }
        return json;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return true;
        }

        if (!(obj instanceof LeaveRequest)) {
            return false;
        }

        return getId() == ((LeaveRequest) obj).getId();
    }

    @Override
    public int hashCode() {
        return 31 * getId();
    }

    public String getRequestUpdateEmailContent() {
        StringBuilder content = new StringBuilder("Hi");
        content.append("\r\n");
        content.append(
                StringUtility.trimAndNullIsDefault(m_requestor.getUserProfile().getName(), m_requestor.getEmail()))
                .append(" has a made changes to his request. New Info: \r\n").append(m_leaveType.getName())
                .append("\r\n");
        content.append("Start Date: ").append(DateUtility.getDateInDDMMMYYDashSeparator(m_startDate)).append("\r\n");

        int numDays = getDays();

        if (numDays > 1) {
            content.append("End Date: ").append(DateUtility.getDateInDDMMMYYDashSeparator(m_endDate))
                    .append("(Not including)").append("\r\n");
        }

        content.append("Number of Days: ").append(numDays).append("\r\n");

        content.append("Reason:").append(m_reason).append("\r\n");
        content.append("Please login to the portal to approve/decline the request").append("\r\n").append("\r\n");

        content.append("Thanks").append("\r\n").append("Zillious Solutions Pvt Ltd");
        return content.toString();
    }

    public String getRequestDeleteEmailContent() {
        StringBuilder content = new StringBuilder("Hi,");
        content.append("\r\n");
        content.append(
                StringUtility.trimAndNullIsDefault(m_requestor.getUserProfile().getName(), m_requestor.getEmail()))
                .append(" has deleted his request. This is just an update e-mail\r\n\r\n")
                .append(m_leaveType.getName()).append("\r\n");
        content.append("Start Date: ").append(DateUtility.getDateInDDMMMYYDashSeparator(m_startDate)).append("\r\n");

        int numDays = getDays();

        if (numDays > 1) {
            content.append("End Date: ").append(DateUtility.getDateInDDMMMYYDashSeparator(m_endDate))
                    .append("(Not including)").append("\r\n");
        }

        content.append("Number of Days: ").append(numDays).append("\r\n\r\n");

        content.append("Thanks").append("\r\n").append("Zillious Solutions Pvt Ltd");
        return content.toString();
    }

    public String getRequestStatusUpdateEmailContent(LeaveRequestStatus prevStatus) {
        StringBuilder content = new StringBuilder("Hi,");
        content.append("\r\n");

        content.append("Your request has been serviced by: ")
                .append(StringUtility.trimAndNullIsDefault(m_statusChangedBy.getUserProfile().getName(),
                        m_statusChangedBy.getEmail())).append("\r\n");

        if (prevStatus == getRequestStatus()) {
            content.append("The status of your Leave Request has not been changed yet.").append("\r\n");
        } else {
            content.append("The status of your Leave Request has been changed from: ").append(prevStatus.name())
                    .append(" to: ").append(getRequestStatus().name()).append("\r\n");
        }

        if (m_additionalComments != null) {
            content.append("You have received comments from your supervisor on your request: \r\n")
                    .append(m_additionalComments).append("\r\n");
        }

        content.append("\r\nYour Leave Request Details:\r\n");

        content.append("Leave Type: ").append(m_leaveType.getName()).append("\r\n");
        content.append("Start Date: ").append(DateUtility.getDateInDDMMMYYDashSeparator(m_startDate)).append("\r\n");

        int numDays = getDays();

        if (numDays > 1) {
            content.append("End Date: ").append(DateUtility.getDateInDDMMMYYDashSeparator(m_endDate))
                    .append("(Not including)").append("\r\n");
        }

        content.append("Number of Days: ").append(numDays).append("\r\n");
        content.append("Reason:").append(m_reason).append("\r\n\r\n");

        content.append("Thanks").append("\r\n").append("Zillious Solutions Pvt Ltd");
        return content.toString();
    }

    public String getReminderEmailContent() {
        StringBuilder content = new StringBuilder("Hi,");
        content.append("\r\n");
        String requestorDetails = StringUtility.trimAndNullIsDefault(m_requestor.getUserProfile().getName(),
                m_requestor.getEmail());

        content.append("This is a reminder for a request raised by ").append(requestorDetails).append("\r\n");

        content.append("The Details for the Leave Request are:\r\n");

        content.append("Leave Type: ").append(m_leaveType.getName()).append("\r\n");
        content.append("Start Date: ").append(DateUtility.getDateInDDMMMYYDashSeparator(m_startDate)).append("\r\n");

        int numDays = getDays();

        if (numDays > 1) {
            content.append("End Date: ").append(DateUtility.getDateInDDMMMYYDashSeparator(m_endDate))
                    .append("(Not including)").append("\r\n");
        }

        content.append("Number of Days: ").append(numDays).append("\r\n");
        content.append("Reason:").append(m_reason).append("\r\n\r\n");

        content.append("The request has not been addressed yet.\r\n")
                .append("Please login to the portal to approve/decline the request").append("\r\n\r\n");

        content.append("Thanks").append("\r\n").append("Zillious Solutions Pvt Ltd");
        return content.toString();
    }

    /**
     * @return the additionalComments
     */
    public String getAdditionalComments() {
        return m_additionalComments;
    }

    /**
     * @param additionalComments the additionalComments to set
     */
    public void setAdditionalComments(String additionalComments) {
        m_additionalComments = additionalComments;
    }

    /**
     * @param set
     * @param leaveRequests
     * @param array
     */
    public static JsonArray getLeaveRequestsJson(Collection<LeaveRequest> leaveRequests) {

        if (leaveRequests == null || leaveRequests.isEmpty()) {
            return new JsonArray();
        }

        JsonArray array = new JsonArray();
        for (LeaveRequest request : leaveRequests) {
            array.add(request.convertToJsonObject());
        }
        return array;
    }

    public static JsonArray convertToLeavesReportJson(List<LeaveRequest> list) {
        JsonArray data = new JsonArray();

        if (list == null || list.isEmpty()) {
            return data;
        }

        Map<User, Map<LeaveRequestStatus, List<LeaveRequest>>> userToRequestMap = new HashMap<User, Map<LeaveRequestStatus, List<LeaveRequest>>>();

        for (LeaveRequest request : list) {
            User requestor = request.getRequestor();
            if (userToRequestMap.get(requestor) == null) {
                userToRequestMap.put(requestor, new HashMap<LeaveRequestStatus, List<LeaveRequest>>());
            }

            LeaveRequestStatus requestStatus = request.getRequestStatus();

            if (userToRequestMap.get(requestor).get(requestStatus) == null) {
                userToRequestMap.get(requestor).put(requestStatus, new ArrayList<LeaveRequest>());
            }

            userToRequestMap.get(requestor).get(requestStatus).add(request);
        }

        for (User user : userToRequestMap.keySet()) {
            Map<LeaveRequestStatus, List<LeaveRequest>> innerMap = userToRequestMap.get(user);
            JsonObject userObject = new JsonObject();
            userObject.addProperty("user_id", user.getUserId());
            userObject.addProperty("name", user.getUserProfile().getName());
            userObject.addProperty("email", user.getEmail());
            userObject.addProperty("approved",
                    innerMap.get(LeaveRequestStatus.APPROVED) != null ? innerMap.get(LeaveRequestStatus.APPROVED)
                            .size() : 0);
            userObject.addProperty("pending",
                    innerMap.get(LeaveRequestStatus.PENDING) != null ? innerMap.get(LeaveRequestStatus.PENDING).size()
                            : 0);
            userObject.addProperty("declined",
                    innerMap.get(LeaveRequestStatus.DECLINED) != null ? innerMap.get(LeaveRequestStatus.DECLINED)
                            .size() : 0);
            data.add(userObject);
        }

        return data;
    }

    /**
     * @return the raisedOn
     */
    public Date getRaisedOn() {
        return m_raisedOn;
    }

    /**
     * @param raisedOn the raisedOn to set
     */
    public void setRaisedOn(Date raisedOn) {
        m_raisedOn = raisedOn;
    }
}
