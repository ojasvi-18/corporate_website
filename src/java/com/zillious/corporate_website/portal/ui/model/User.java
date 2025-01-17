package com.zillious.corporate_website.portal.ui.model;

import java.security.MessageDigest;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.codec.binary.Base64;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.zillious.corporate_website.logger.Logger;
import com.zillious.corporate_website.portal.entity.WeeklySummaryMessage;
import com.zillious.corporate_website.portal.entity.utility.RoleSerializer;
import com.zillious.corporate_website.portal.entity.utility.StatusSerializer;
import com.zillious.corporate_website.portal.ui.UserRoles;
import com.zillious.corporate_website.portal.ui.UserStatus;
import com.zillious.corporate_website.utils.DateUtility;
import com.zillious.corporate_website.utils.NumberUtility;
import com.zillious.corporate_website.utils.StringUtility;

@Entity
@Table(name = "website_users")
public class User implements DBObject {

    private static final long     serialVersionUID    = -596855232462293449L;

    private static Logger         s_logger            = Logger.getInstance(User.class);

    @Id
    @Column(name = "user_id", nullable = false)
    protected int                 m_userId;

    @Column(name = "status", nullable = false)
    @Convert(converter = StatusSerializer.class)
    protected UserStatus          m_status;

    @Column(name = "auth_token", nullable = true)
    protected String              m_authToken;

    @Column(name = "role", nullable = false)
    @Convert(converter = RoleSerializer.class)
    protected UserRoles           m_UserRole;

    @Column(name = "email", nullable = false)
    protected String              m_email;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id")
    private UserProfile           m_userProfile;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "user_team", joinColumns = { @JoinColumn(name = "user_id") }, inverseJoinColumns = { @JoinColumn(name = "team_id") })
    private Set<Team>             m_teamlist          = new HashSet<Team>();

    @Column(name = "device_id", nullable = false)
    private int                   m_deviceUserID;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "m_supervisor", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Team>             m_managedTeams;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "m_requestor", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<LeaveRequest>     m_leaveRequests;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "m_key.m_employee", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private Set<AttendanceRecord> m_attendanceRecords;

    /*
     * In Memory Variables
     */
    @Transient
    protected String              m_password;
    @Transient
    private boolean               m_isRequirePwdCheck = true;
    @Transient
    private String                  m_inTime;
    @Transient
    private String                  m_outTime;

    public String getInTime() {
        return m_inTime;
    }

    public void setInTime(String inTimeStr) {
        this.m_inTime = inTimeStr;
    }

    public String getOutTime() {
        return m_outTime;
    }

    public void setOutTime(String m_outTime) {
        this.m_outTime = m_outTime;
    }

    public User(int userId) {
        m_userId = userId;
    }

    public User() {
        // TODO Auto-generated constructor stub
    }

    public User(ResultSet rs) throws Exception {
        m_userId = rs.getInt("user_id");
        m_status = UserStatus.deserialize(rs.getString("status"));
        m_authToken = rs.getString("auth_token");
        m_UserRole = UserRoles.deserialize(rs.getString("role"));
        m_email = rs.getString("email");
    }

    public int getUserId() {
        return m_userId;
    }

    public void setUserId(int userId) {
        m_userId = userId;
    }

    public static String generateRandomCSRFToken(int len) {
        StringBuilder buf = new StringBuilder(len + 1);
        for (int i = 0; i < len; i++) {
            buf.append(StringUtility.getRandomAplanumeric());
        }
        return buf.toString();
    }

    public UserStatus getStatus() {
        return m_status;
    }

    /**
     * @return status isEnabled
     */
    public boolean isEnabled() {
        UserStatus status = getStatus();
        return status != null && (status == UserStatus.Enabled);
    }

    public String getUserStatus() {
        UserStatus status = getStatus();
        if (status == null) {
            return null;
        }

        return status.getDisplayName();
    }

    /**
     * @return the authToken
     */
    public String getAuthToken() {
        return m_authToken;
    }

    /**
     * @param authToken the authToken to set
     */
    public void setAuthToken(String authToken) {
        m_authToken = authToken;
    }

    /**
     * @return the userRole
     */

    public UserRoles getUserRole() {
        return m_UserRole;
    }

    /**
     * @param userRole the userRole to set
     */

    public void setUserRole(UserRoles userRole) {
        m_UserRole = userRole;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return m_email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        m_email = email;
    }

    /**
     * @return the isRequirePwdCheck
     */
    public boolean isRequirePwdCheck() {
        return m_isRequirePwdCheck;
    }

    /**
     * @param isRequirePwdCheck the m_isRequirePwdCheck to set
     */
    public void setIsRequirePwdCheck(boolean isRequirePwdCheck) {
        this.m_isRequirePwdCheck = isRequirePwdCheck;
    }

    private static String getPasswordHash(String userId, String password) {
        try {
            String dataString = "corp" + password + "zil" + userId;
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(dataString.getBytes("UTF-8"));
            byte raw[] = md.digest();
            return new String(Base64.encodeBase64(raw));

        } catch (Exception e) {
            s_logger.error("Error occurred while hashing password", e);
            return null;
        }
    }

    public static boolean authenthicateUser(int userId, String dbAuthToken, String givenPassword) {
        if (givenPassword == null || dbAuthToken == null) {
            return false;
        }
        String giveAuthToken = User.getPasswordHash(String.valueOf(userId), givenPassword);
        return giveAuthToken.equals(dbAuthToken);
    }

    private static String getPasswordHash(int userId, String givenPassword) {
        return getPasswordHash("" + userId, givenPassword);
    }

    public static void main(String[] args) {
        System.out.println(getPasswordHash(0, "admin123"));
    }

    public void setPassword(String passwd) {
        if (passwd != null && passwd.length() > 20) {
            passwd = passwd.substring(0, 20);
        }
        m_password = passwd;
    }

    public void hashPassword() {
        m_authToken = getPasswordHash(m_userId, m_password);
    }

    // @Override
    // public boolean equals(Object obj) {
    // if (obj == null) {
    // return false;
    // }
    //
    // if (!(obj instanceof User)) {
    // return false;
    // }
    //
    // if (this == obj) {
    // return true;
    // }
    //
    // User secondDTO = (User) obj;
    // return m_userId == secondDTO.m_userId;
    // }

    // @Override
    // public int hashCode() {
    // int hashCode = 1;
    // hashCode = 31 * hashCode + m_userId;
    // hashCode = 31 * hashCode + m_deviceUserID;
    // return hashCode;
    // }

    /**
     * @return the deviceUserID
     */
    public int getDeviceUserID() {
        return m_deviceUserID;
    }

    /**
     * @return the m_userProfile
     */
    public UserProfile getUserProfile() {
        return m_userProfile;
    }

    /**
     * @param m_userProfile the m_userProfile to set
     */
    public void setUserProfile(UserProfile m_userProfile) {
        this.m_userProfile = m_userProfile;
    }

    public JsonElement convertUserListToCompleteJson() {
        JsonObject user = new JsonObject();
        user.addProperty("name", getUserProfile().getName());
        user.addProperty("id", getUserId());
        user.addProperty("email", getEmail());
        user.addProperty("in_time", getInTime().toString());
        user.addProperty("out_time", getOutTime().toString());
        UserRoles userRole = getUserRole();
        JsonObject roleJson = null;
        if (userRole != null) {
            roleJson = userRole.getJson();
        } else {
            roleJson = new JsonObject();
        }
        user.add("role", roleJson);
        UserStatus status = getStatus();
        JsonObject statusJson;
        if (status != null) {
            statusJson = status.getJson();
        } else {
            statusJson = new JsonObject();
        }

        user.add("status", statusJson);
        user.addProperty("d_id", getDeviceUserID());

        return user;
    }

    public static User parseForAuthentication(JsonObject authenticationObject) {
        if (authenticationObject == null) {
            return null;
        }

        JsonElement emailElement = authenticationObject.get("email");
        if (emailElement == null) {
            return null;
        }

        JsonElement passwordElement = authenticationObject.get("password");
        if (passwordElement == null) {
            return null;
        }

        User user = new User();
        user.setEmail(emailElement.getAsString());
        user.setPassword(passwordElement.getAsString());

        return user;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return m_password;
    }

    public void setStatus(UserStatus status) {
        m_status = status;
    }

    public void setDeviceUserID(int i) {
        m_deviceUserID = i;
    }

    /**
     * This api returns an array of id and name of the users from the userlist
     * 
     * @param userList
     * @return
     */
    public static String convertIntoIdNameJsonArray(List<User> userList) {
        JsonArray array = getUserListJsonObject(userList);

        if (array == null) {
            return null;
        }
        String jsonString = array.toString();
        s_logger.debug("birthday list json array: " + jsonString);
        return jsonString;
    }

    public static JsonArray getUserListJsonObject(Collection<User> userList) {

        if (userList == null) {
            return null;
        }

        JsonArray array = new JsonArray();

        for (User user : userList) {
            JsonObject userObj = convertToIdNameJsonObject(user);
            array.add(userObj);
        }
        return array;
    }

    /**
     * @param user
     * @return
     */
    protected static JsonObject convertToIdNameJsonObject(User user) {
        return user.convertToIdNameJsonObject();
    }

    public Set<Team> getTeamlist() {
        return m_teamlist;
    }

    public void setTeamlist(Set<Team> teamlist) {
        m_teamlist = teamlist;
    }

    public void addTeam(Team team) {
        if (team == null) {
            return;
        }
        if (m_teamlist == null) {
            m_teamlist = new HashSet<Team>();
        }

        m_teamlist.add(team);

    }

    /**
     * @return the managedTeams
     */
    public Set<Team> getManagedTeams() {
        return m_managedTeams;
    }

    /**
     * @param managedTeams the managedTeams to set
     */
    public void setManagedTeams(Set<Team> managedTeams) {
        m_managedTeams = managedTeams;
    }

    public void addManagedTeam(Team team) {
        if (team == null) {
            return;
        }

        if (m_managedTeams == null) {
            m_managedTeams = new HashSet<Team>();
        }

        m_managedTeams.add(team);
    }

    /**
     * @return the leaveRequests
     */
    public Set<LeaveRequest> getLeaveRequests() {
        return m_leaveRequests;
    }

    /**
     * @param leaveRequests the leaveRequests to set
     */
    public void setLeaveRequests(Set<LeaveRequest> leaveRequests) {
        m_leaveRequests = leaveRequests;
    }

    public JsonArray getLeaveRequestsJson(String year) {
        int yearNum = NumberUtility.parsetIntWithDefaultOnErr(year, 0);
        List<LeaveRequest> requests = null;
        if (m_leaveRequests != null) {
            requests = new ArrayList<LeaveRequest>();
            for (LeaveRequest request : m_leaveRequests) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(request.getStartDate());
                if (cal.get(Calendar.YEAR) == yearNum) {
                    requests.add(request);
                }
            }
        }

        JsonArray array = LeaveRequest.getLeaveRequestsJson(requests);
        return array;
    }

    public void addLeaveRequest(LeaveRequest request) {
        if (request == null) {
            return;
        }

        if (m_leaveRequests == null) {
            m_leaveRequests = new HashSet<LeaveRequest>();
        }

        m_leaveRequests.add(request);

    }

    /**
     * @param user
     * @return
     */
    public JsonObject convertToIdNameJsonObject() {
        JsonObject userObj = new JsonObject();
        userObj.addProperty("name", getUserProfile().getName());
        userObj.addProperty("id", getUserId());
        return userObj;
    }

    /**
     * @return the attendanceRecords
     */
    public Set<AttendanceRecord> getAttendanceRecords() {
        return m_attendanceRecords;
    }

    /**
     * @param attendanceRecords the attendanceRecords to set
     */
    public void setAttendanceRecords(Set<AttendanceRecord> attendanceRecords) {
        m_attendanceRecords = attendanceRecords;
    }

    public JsonObject convertToIdNameEmailJsonObject() {
        JsonObject user = convertToIdNameJsonObject();
        if (user != null) {
            user.addProperty("email", getEmail());
        }

        return user;
    }

    /**
     * This api returns the displayable form of the user information, in the
     * format: Name, Email, UserId; primarily to be sent out in the e-mails.
     * 
     * @return
     */
    public String getEmailDisplayInfo() {

        return getEmailDisplayInfo(0);
    }

    public String getEmailDisplayInfo(int tabLevel) {

        String tab = StringUtility.getTabContent(tabLevel);

        StringBuilder content = new StringBuilder();

        if (getUserProfile() != null && getUserProfile().getName() != null) {
            content.append(tab).append("Name: ").append(getUserProfile().getName());
            content.append(StringUtility.getNewLineString());
        }

        content.append(tab).append("Email Id: ").append(getEmail());
        content.append(StringUtility.getNewLineString());
        content.append(tab).append("User id: ").append(getUserId());
        return content.toString();
    }

    public static String getTeamChangeSubject() {
        return "Updates in the team structure";
    }

    public String getTeamChangeEmailContent(Set<Team> deletedTeams, Set<Team> addedTeams, User profileUpdator) {
        StringBuilder content = new StringBuilder();
        content.append("Hi ").append(StringUtility.trimAndNullIsDefault(getUserProfile().getName(), getEmail()));

        String newLineString = StringUtility.getNewLineString();
        content.append(newLineString);

        content.append("Please find changes in your teams below:");
        content.append(newLineString);

        if (!deletedTeams.isEmpty()) {
            content.append(newLineString);
            content.append("You have been removed from the following teams:");
            Iterator<Team> iterator = deletedTeams.iterator();
            int i = 1;
            while (iterator.hasNext()) {
                Team deletedTeam = iterator.next();
                content.append(newLineString);
                content.append(i).append(": ").append(newLineString).append(deletedTeam.getTeamDetails(1));
                i++;
            }
        }

        if (!addedTeams.isEmpty()) {
            content.append(newLineString);
            content.append(newLineString);

            content.append("You have been added to the following teams:");
            Iterator<Team> iterator = addedTeams.iterator();
            int i = 1;
            while (iterator.hasNext()) {
                content.append(newLineString);
                Team addedTeam = iterator.next();
                content.append(newLineString);
                content.append(i).append(": ").append(newLineString).append(addedTeam.getTeamDetails(1));
                i++;
            }
        }

        content.append(newLineString);
        content.append(newLineString);
        content.append("If you need further information on this, please contact ")
                .append(StringUtility.trimAndNullIsDefault(profileUpdator.getUserProfile().getName(),
                        profileUpdator.getEmail())).append(".");

        content.append(newLineString);
        content.append("Thanks,");
        content.append(newLineString);
        content.append("Zillious Solutions Pvt Ltd");

        return content.toString();
    }

    public static String getProfileChangeEmailSubject() {
        return "Updates in Profile";
    }

    /**
     * api to populate the email content for scheduler summary message
     * 
     * @return
     */
    public String getSchedularEmailSummaryContent(WeeklySummaryMessage userSummaryMessage) {

        StringBuilder content = new StringBuilder("");

        // display the list of users having birthday today..
        List<User> usersHavingBdayToday = userSummaryMessage.getBirthdayToday();
        if (usersHavingBdayToday != null) {
            content.append("Give birthday wishes to  ").append("\r\n");
            for (User userHavingBdayToday : usersHavingBdayToday) {
                content.append("* ").append(userHavingBdayToday.getUserProfile().getName());
                content.append("\r\n");
            }

        }

        // display the list of users having birthday this week
        List<User> usersHavingBdayThisWeek = userSummaryMessage.getBirthdayThisWeek();
        if (usersHavingBdayThisWeek != null) {
            content.append(StringUtility.getNewLineString());
            content.append("Upcoming birthdays this week ").append("\r\n");
            for (User userHavingBdayThisWeek : usersHavingBdayThisWeek) {

                String dobString = userHavingBdayThisWeek.getUserProfile().getdob();
                Date dob = DateUtility.parseDateInDDMMYYYYDashNullIfError(dobString);
                String bday = DateUtility.getDateInDDMMM(dob);

                content.append("* ").append(userHavingBdayThisWeek.getUserProfile().getName() + " on " + bday);
                content.append("\r\n");
            }
        }

        // display the list of team members having a leave today
        Set<LeaveRequest> usersHavingLeaveToday = userSummaryMessage.getTeamLeavesToday();
        if (usersHavingLeaveToday != null) {
            content.append(StringUtility.getNewLineString());
            content.append("Team members on leave today  ").append("\r\n");
            for (LeaveRequest userHavingLeaveToday : usersHavingLeaveToday) {
                content.append("* ").append(userHavingLeaveToday.getRequestor().getUserProfile().getName());
                content.append("\r\n");
            }
        }

        // display list of team members having leaves this week
        Set<LeaveRequest> usersHavingLeavesThisWeek = userSummaryMessage.getLeavesThisWeek();
        if (usersHavingLeavesThisWeek != null) {
            content.append(StringUtility.getNewLineString());
            content.append("Your team members on leave this week  ").append("\r\n");
            for (LeaveRequest userHavingLeavesThisWeek : usersHavingLeavesThisWeek) {
                Date startDate = userHavingLeavesThisWeek.getStartDate();
                String startDateInString = null;
                startDateInString = DateUtility.getDateInDDMMMYYYYDash(startDate);
                Date endDate = userHavingLeavesThisWeek.getEndDate();
                String endDateInString = null;
                endDateInString = DateUtility.getDateInDDMMMYYYYDash(endDate);

                content.append("* ").append(
                        userHavingLeavesThisWeek.getRequestor().getUserProfile().getName() + " from "
                                + startDateInString + " to " + endDateInString);
                content.append(StringUtility.getNewLineString());
            }
        }

        // display the list of team's pending leave requests if the user is
        // supervisor
        Set<LeaveRequest> pendingLeaveRequests = userSummaryMessage.getPendingTeamLeaves();
        if (pendingLeaveRequests != null) {
            content.append(StringUtility.getNewLineString());
            content.append("You have the following pending leave requests ").append("\r\n");
            for (LeaveRequest pendingLeaveRequest : pendingLeaveRequests) {

                content.append("* ").append("From : " + pendingLeaveRequest.getRequestor().getUserProfile().getName())
                        .append("\r\n");
                content.append("* ").append("Start Date : " + pendingLeaveRequest.getStartDate()).append("\r\n");
                content.append("* ").append("No of Days : " + pendingLeaveRequest.getDays()).append("\r\n");
                content.append("* ").append("Reason : " + pendingLeaveRequest.getReason()).append("\r\n");
                content.append(StringUtility.getNewLineString());
            }

            content.append("Please login to the portal to approve/decline the request").append("\r\n");

        }

        content.append(StringUtility.getNewLineString());
        content.append(StringUtility.getNewLineString());
        content.append("Thanks").append("\r\n");
        content.append("Zillious Solutions Pvt Ltd ");

        return content.toString();
    }

}
