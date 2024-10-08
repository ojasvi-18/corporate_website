package com.zillious.corporate_website.portal.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.mail.MessagingException;

import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.zillious.corporate_website.portal.dao.AttendanceDao;
import com.zillious.corporate_website.portal.dao.LeavesDao;
import com.zillious.corporate_website.portal.dao.UserDao;
import com.zillious.corporate_website.portal.entity.EmailMessage;
import com.zillious.corporate_website.portal.entity.WeeklySummaryMessage;
import com.zillious.corporate_website.portal.service.LeavesService;
import com.zillious.corporate_website.portal.service.TeamService;
import com.zillious.corporate_website.portal.service.UserService;
import com.zillious.corporate_website.portal.ui.LeaveRequestStatus;
import com.zillious.corporate_website.portal.ui.UserRoles;
import com.zillious.corporate_website.portal.ui.UserStatus;
import com.zillious.corporate_website.portal.ui.dto.FormDataWithFile;
import com.zillious.corporate_website.portal.ui.dto.UserProfileChanger;
import com.zillious.corporate_website.portal.ui.model.AttendanceRecord;
import com.zillious.corporate_website.portal.ui.model.LeaveRequest;
import com.zillious.corporate_website.portal.ui.model.Team;
import com.zillious.corporate_website.portal.ui.model.User;
import com.zillious.corporate_website.portal.ui.model.UserProfile;
import com.zillious.corporate_website.portal.utility.CloudManager;
import com.zillious.corporate_website.portal.utility.CollectionsUtility;
import com.zillious.corporate_website.portal.utility.JsonUtility;
import com.zillious.corporate_website.ui.navigation.WebsiteException;
import com.zillious.corporate_website.ui.navigation.WebsiteExceptionType;
import com.zillious.corporate_website.ui.security.ZilliousSecurityWrapperRequest;
import com.zillious.corporate_website.ui.session.SessionStore;
import com.zillious.corporate_website.utils.DateUtility;
import com.zillious.corporate_website.utils.EmailSender;
import com.zillious.corporate_website.utils.StringUtility;

@EnableTransactionManagement
@Service
public class UserServiceImpl implements UserService {

    private static Logger s_logger = Logger.getLogger(UserServiceImpl.class);

    @Autowired
    private UserDao       m_userDao;

    @Autowired
    private TeamService   m_teamService;

    @Autowired
    private LeavesService m_leavesService;

    @Autowired
    private LeavesDao     m_leavesDao;

    @Autowired
    private AttendanceDao m_attendanceDao;

    @Transactional
    @Override
    public User getUser(int ID) {
        User temp = this.m_userDao.getUser(ID);
        return temp;
    }

    @Override
    @Transactional
    public JsonObject addUser(String userJson) {
        JsonParser parser = new JsonParser();
        JsonObject jobj = (JsonObject) parser.parse(userJson);

        User user = new User();
        if (jobj.get("id") != null) {
            int id = jobj.get("id").getAsInt();
            user.setUserId(id);
        }

        if (jobj.get("email").getAsString() != null) {
            String emailFromJson = jobj.get("email").getAsString().replaceAll("^\"|\"$", "");
            if (StringUtility.isValidEmail(emailFromJson)) {
                user.setEmail(emailFromJson);
            }
        }

        if (jobj.getAsJsonObject("role") != null) {
            user.setUserRole(UserRoles.deserialize(jobj.getAsJsonObject("role").get("code").toString()
                    .replaceAll("^\"|\"$", "")));
        }

        if (jobj.get("password") != null) {
            String password = jobj.get("password").getAsString();
            user.setPassword(password);
            user.hashPassword();
        }

        if (jobj.get("status") != null) {
            user.setStatus(UserStatus.deserialize(jobj.getAsJsonObject("status").get("id").toString()
                    .replaceAll("^\"|\"$", "")));
        }

        if (jobj.get("d_id") != null) {
            user.setDeviceUserID(jobj.get("d_id").getAsInt());
        }
        user.setUserProfile(new UserProfile());
        if (jobj.get("name") != null) {
            String name = jobj.get("name").getAsString().replaceAll("^\"|\"$", "");
            if (StringUtility.isValidName(name)) {
                user.getUserProfile().setName(name);
            }
        }

        JsonObject finalJSON = new JsonObject();

        boolean isSuccess = false;
        String errorMsg = "";
        try {
            isSuccess = saveUser(user);
            if (isSuccess) {
                finalJSON.addProperty("id", user.getUserId());
            }
        } catch (Exception e) {
            errorMsg = e.getMessage();
        }

        JsonUtility.createResponseJson(finalJSON, isSuccess, errorMsg);
        return finalJSON;
    }

    public boolean saveUser(User user) {
        boolean isSuccess = m_userDao.persistUser(user);
        return isSuccess;
    }

    @Override
    @Transactional
    public boolean updateUser(User user) {
        try {
            return m_userDao.updateUser(user);
        } catch (Exception e) {
            s_logger.error("Error while updating the user", e);
        }
        return false;
    }

    @Override
    @Transactional
    public JsonObject updateUser(String t) {
        JsonParser parser = new JsonParser();
        JsonObject jobj = (JsonObject) parser.parse(t);

        int userId = jobj.get("id").getAsInt();

        User user = getUser(userId);
        if (user == null) {
            JsonObject responseObject = new JsonObject();
            JsonUtility.createResponseJson(responseObject, false, WebsiteExceptionType.INVALID_ACCESS.getDesc());
            return responseObject;
        }

        if (StringUtility.isValidEmail(jobj.get("email").getAsString())) {
            user.setEmail(jobj.get("email").getAsString().replaceAll("^\"|\"$", ""));
        }

        if (StringUtility.isValidName(jobj.get("name").getAsString())) {
            user.getUserProfile().setName(jobj.get("name").getAsString().replaceAll("^\"|\"$", ""));
        }

        String roleSerial = jobj.get("role").getAsJsonObject().get("code").getAsString();
        user.setUserRole(UserRoles.deserialize(roleSerial));

        JsonElement passwordElement = jobj.get("password");
        if (passwordElement != null && !passwordElement.getAsString().isEmpty()) {
            user.setPassword(passwordElement.getAsString().replaceAll("^\"|\"$", ""));
            user.hashPassword();
        }

        user.setStatus(UserStatus.deserialize(jobj.get("status").getAsJsonObject().get("id").getAsString()));

        user.setDeviceUserID(jobj.get("d_id").getAsInt());

        JsonObject finalJSON = new JsonObject();
        finalJSON.addProperty("id", user.getUserId());
        boolean isSuccess = false;
        String errorMsg = "";
        try {
            isSuccess = updateUser(user);
        } catch (Exception e) {
            errorMsg = e.getMessage();
        }

        JsonUtility.createResponseJson(finalJSON, isSuccess, errorMsg);

        return finalJSON;

    }

    @Override
    @Transactional
    public JsonObject deleteUser(String t) {
        // TODO Auto-generated method stub
        // return null;
        JsonParser parser = new JsonParser();
        JsonObject jobj = (JsonObject) parser.parse(t);
        User user = new User();
        user.setUserId(jobj.get("userid").getAsInt());
        JsonObject finalJSON = new JsonObject();
        finalJSON.addProperty("id", user.getUserId());
        boolean isSuccess = false;
        String errorMsg = "";
        try {
            isSuccess = this.m_userDao.deleteUser(user);
        } catch (Exception e) {
            errorMsg = e.getMessage();
        }

        JsonUtility.createResponseJson(finalJSON, isSuccess, errorMsg);
        return finalJSON;
    }

    @Override
    @Transactional
    public JsonArray getUsersByNamePattern(String queryString) {
        List<User> userList = m_userDao.getUsersByNamePattern(queryString);
        JsonArray userListJson = User.getUserListJsonObject(userList);
        if (userListJson == null) {
            userListJson = new JsonArray();
        }

        return userListJson;
    }

    @Override
    @Transactional
    public boolean isValidUser(User user) throws WebsiteException {
        User userByEmail = m_userDao.getUserByEmail(user.getEmail());
        if (userByEmail == null) {
            return false;
        }

        return User.authenthicateUser(userByEmail.getUserId(), userByEmail.getAuthToken(), user.getPassword())
                && userByEmail.isEnabled();
    }

    @Override
    @Transactional
    public boolean syncUserProfiles() {
        return m_userDao.syncUserProfiles();
    }

    @Override
    @Transactional
    public JsonArray getAllUsersAsJson() {
        List<User> usersList = getAllUsers();
        JsonArray userDetails = new JsonArray();

        for (User user : usersList) {
            user.setInTime("");
            user.setOutTime("");
            userDetails.add(user.convertUserListToCompleteJson());
        }
        return userDetails;
    }



    /**
     * @return
     */
    @Override
    public List<User> getAllUsers() {
        return getAllUsers(false);
    }

    @Override
    public List<User> getUsersBirthdayList() {
        return m_userDao.getUsersBirthdayList();
    }

    @Override
    @Transactional
    public JsonObject getJsonForProfileByUserID(User profileUpdator, int userId) {
        User user = m_userDao.getProfilebyUserID(userId);
        JsonObject profileJson;
        UserProfile userProfile;

        // isSupervisor will check whether the logged in user is has ADMIN roles
        // or not? if YES then team details will be available for editing on UI
        // otherwise only view UI will be provided
        boolean isAdmin = false;
        isAdmin = (profileUpdator.getUserRole().isAdminRole()) ? true : false;

        boolean canEdit = isAdmin || (profileUpdator.getUserId() == userId);

        if (user != null && ((userProfile = user.getUserProfile()) != null)) {
            try {
                profileJson = userProfile.convertIntoJson(isAdmin);
                profileJson.addProperty("user_id", user.getUserId());
                Set<Team> teamlist = null;
                JsonArray teamdetails = new JsonArray();
                teamlist = user.getManagedTeams();
                if (teamlist != null && !teamlist.isEmpty()) {
                    for (Team team : teamlist) {
                        teamdetails.add(team.convertIntoJson(true));
                    }
                }

                // Teams that user is a member of
                teamlist = user.getTeamlist();
                if (teamlist != null && !teamlist.isEmpty()) {
                    for (Team team : teamlist) {
                        teamdetails.add(team.convertIntoJson(false));
                    }
                }

                // Can edit basic details of profile
                profileJson.addProperty("canEdit", canEdit);

                // has admin rights to edit advanced details
                JsonUtility.addIsAdminProperty(profileJson, profileUpdator);
                profileJson.add("TeamDetails", teamdetails);
                JsonUtility.createResponseJson(profileJson, true, null);
            } catch (Exception e) {
                profileJson = new JsonObject();
                JsonUtility.createResponseJson(profileJson, false, WebsiteExceptionType.SYSTEM_ERROR.getDesc());
            }
        } else {
            profileJson = new JsonObject();
            JsonUtility.createResponseJson(profileJson, false, "User does not exists");
        }
        return profileJson;
    }

    @Transactional
    @Override
    public JsonObject updateProfile(User profileUpdator, String profileJson) {

        JsonObject finalJSON = new JsonObject();

        boolean isSuccess = false;
        String errorMsg = "";
        try {
            JsonParser parser = new JsonParser();
            JsonObject userProfileJsonObj = (JsonObject) parser.parse(profileJson);

            JsonElement userIdElement = userProfileJsonObj.get("user_id");
            if (userIdElement == null) {
                throw new WebsiteException(WebsiteExceptionType.INVALID_REQUEST);
            }

            int idOfUserToBeUpdated = userIdElement.getAsInt();

            if (profileUpdator.getUserId() != idOfUserToBeUpdated && !profileUpdator.getUserRole().isAdminRole()) {
                throw new WebsiteException(WebsiteExceptionType.INVALID_ACCESS);
            }

            User userToBeUpdated = m_userDao.getUser(idOfUserToBeUpdated);

            if (userToBeUpdated == null) {
                throw new WebsiteException(WebsiteExceptionType.MISSING_USER);
            }

            Set<Team> oldTeams = userToBeUpdated.getTeamlist();
            Set<Team> newTeams = null;
            // Designation can only be updated by an Admin Role user

            // user can be added to or removed from a team by admin only
            if (profileUpdator.getUserRole().isAdminRole()) {

                try {
                    newTeams = Team.parseTeamsFromProfileJson(userProfileJsonObj);
                    if (newTeams != null) {
                        newTeams = m_teamService.getTeams(newTeams);
                    }
                    userToBeUpdated.setTeamlist(newTeams);
                } catch (Exception e) {
                    throw new RuntimeException("Problem with Teams information in the request");
                }
            }

            // oldTeams, newTeams;

            Set<Team> deletedTeams = new HashSet<Team>();
            Set<Team> addedTeams = new HashSet<Team>();

            if (oldTeams != null) {
                for (Team oldTeam : oldTeams) {
                    if (newTeams == null || !newTeams.contains(oldTeam)) {
                        deletedTeams.add(oldTeam);
                    }
                }
            }

            if (newTeams != null) {
                for (Team newTeam : newTeams) {
                    if (oldTeams == null || !oldTeams.contains(newTeam)) {
                        addedTeams.add(newTeam);
                    }
                }
            }

            EmailMessage emailMessage = null;
            if (!deletedTeams.isEmpty() || !addedTeams.isEmpty()) {
                try {
                    emailMessage = new EmailMessage();

                    emailMessage.setSubject(User.getTeamChangeSubject());
                    emailMessage.addTo(userToBeUpdated.getEmail());

                    List<User> admins = getAdminRoleUsers();
                    if (admins != null) {
                        for (User director : admins) {
                            emailMessage.addCC(director.getEmail());
                        }
                    }

                    String content = userToBeUpdated
                            .getTeamChangeEmailContent(deletedTeams, addedTeams, profileUpdator);
                    emailMessage.setContent(content);

                } catch (Exception e) {
                    s_logger.error("Error in email", e);
                    emailMessage = null;
                }
            }

            UserProfileChanger changesTrackingProfile = new UserProfileChanger();

            userToBeUpdated.getUserProfile().updateValuesFromJson(userProfileJsonObj,
                    profileUpdator.getUserRole().isAdminRole(), changesTrackingProfile);

            isSuccess = m_userDao.updateProfile(userToBeUpdated);

            if (isSuccess && emailMessage != null) {
                EmailSender.sendEmail(emailMessage);
            }
            try {
                EmailMessage emailMessages = new EmailMessage();

                emailMessages.addTo(userToBeUpdated.getEmail());

                emailMessages.setSubject(User.getProfileChangeEmailSubject());
                String emailString = changesTrackingProfile.generateProfileChangeMailContent(userToBeUpdated,
                        profileUpdator);
                emailMessages.setContent(emailString);
                EmailSender.sendEmail(emailMessages);
            } catch (Exception e) {

                s_logger.error("Error in email", e);
            }

        } catch (Exception e) {
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
    public boolean updateUsers(User... users) {
        return m_userDao.updateUsers(users);
    }

    @Override
    public List<User> getAdminRoleUsers() {
        Set<UserRoles> userRolesToFetch = new HashSet<UserRoles>();
        userRolesToFetch.add(UserRoles.DIRECTOR);
        userRolesToFetch.add(UserRoles.HR);
        return m_userDao.getAllUsersOfRole(userRolesToFetch);
    }

    @Override
    @Transactional
    public User getAuthorizedUser(User user) {
        try {

            User userByEmail = m_userDao.getUserByEmail(user.getEmail());

            s_logger.debug("in isUserAuthorized function " + userByEmail);

            if (userByEmail == null) {
                return null;
            }

            if (userByEmail.isEnabled()
                    && (!user.isRequirePwdCheck() || User.authenthicateUser(userByEmail.getUserId(),
                            userByEmail.getAuthToken(), user.getPassword()))) {
                Hibernate.initialize(userByEmail.getUserProfile());
                return userByEmail;
            }

            s_logger.info("could not authenticate email : " + userByEmail.getEmail() + ", and password : "
                    + userByEmail.getPassword());
        } catch (Exception e) {
            s_logger.error("error in fetching user details from DB in isUserAuthorized funtion ", e);
        }

        return null;
    }

    @Override
    @Transactional
    public boolean sendBirthdayWishes(User from, String wishesJson) throws Exception {
        User toUser = null;
        try {
            JsonParser parser = new JsonParser();
            JsonObject parsedObject = (JsonObject) parser.parse(wishesJson);
            int toUserId = parsedObject.get("to_user_id").getAsInt();

            if (from.getUserId() == toUserId) {
                throw new WebsiteException(WebsiteExceptionType.SAME_USER);
            }

            toUser = getUser(toUserId);

            String toEmail = null;
            if (toUser == null || (toEmail = StringUtility.trimAndEmptyIsNull(toUser.getEmail())) == null) {
                throw new WebsiteException(WebsiteExceptionType.MISSING_USER);
            }

            EmailMessage emailMessage = new EmailMessage();

            emailMessage.addTo(toEmail);

            // subject
            JsonElement subjectElement = parsedObject.get("subject");
            String subject = null;
            if (subjectElement != null) {
                subject = StringUtility.trimAndEmptyIsNull(subjectElement.getAsString());
            }

            emailMessage.setSubject(subject == null ? "Birthday Wishes" : subject);

            String content = null;
            JsonElement contentElement = parsedObject.get("content");
            if (contentElement != null) {
                content = StringUtility.trimAndEmptyIsNull(contentElement.getAsString());
            }

            content = (content == null ? "Wish you a very Happy Birthday" : content);

            content += StringUtility.NEW_LINE + StringUtility.NEW_LINE;
            if (from.getUserProfile().getName() != null) {
                content += from.getUserProfile().getName();
            }

            emailMessage.setFrom(from.getEmail());

            emailMessage.setContent(content);

            EmailSender.sendEmail(emailMessage);
            return true;
        } catch (Exception e) {
            s_logger.error("Error in sending birthday wishes from: " + from.getUserProfile().getName()
                    + (toUser != null ? (", to: " + toUser.getUserProfile().getName()) : ""));
            if (e instanceof WebsiteException) {
                throw e;
            }
            return false;
        }
    }

    @Override
    @Transactional
    public JsonObject updateProfilePicture(User profileUpdator, FormDataWithFile formDataWithFile,
            ZilliousSecurityWrapperRequest zilliousReq) throws Exception {
        JsonObject responseObject = new JsonObject();

        MultipartFile file = formDataWithFile.getFile();

        if (formDataWithFile.getUser_id() < 1 || file == null || file.isEmpty()) {
            JsonUtility.createResponseJson(responseObject, false, WebsiteExceptionType.INVALID_REQUEST.getDesc());
            return responseObject;
        }

        User userToBeUpdated = m_userDao.getUser(formDataWithFile.getUser_id());
        if (userToBeUpdated == null) {
            JsonUtility.createResponseJson(responseObject, false, WebsiteExceptionType.INVALID_PROFILE.getDesc());
            return responseObject;
        }

        if (profileUpdator.getUserId() != formDataWithFile.getUser_id() && !profileUpdator.getUserRole().isAdminRole()) {
            JsonUtility.createResponseJson(responseObject, false, WebsiteExceptionType.INVALID_ACCESS.getDesc());
            return responseObject;
        }

        boolean isProfileUpdated = false;
        boolean isAddedToCloud = false;
        CloudManager s3CloudClient = CloudManager.AMAZONS3;
        String objectKey = null;
        try {
            objectKey = s3CloudClient.uploadFileAndReturnKey(userToBeUpdated.getUserId(), userToBeUpdated
                    .getUserProfile().getPictureKey(), file);
            isAddedToCloud = true;
            if (objectKey == null) {
                JsonUtility.createResponseJson(responseObject, false, "Error in saving profile picture");
                return responseObject;
            } else {
                if (userToBeUpdated.getUserProfile().getPictureKey() == null) {
                    // new save
                    userToBeUpdated.getUserProfile().setPictureKey(objectKey);
                    isProfileUpdated = m_userDao.updateProfile(userToBeUpdated);
                    if (!isProfileUpdated) {
                        throw new WebsiteException(WebsiteExceptionType.SYSTEM_ERROR);
                    }

                    if (profileUpdator.getUserId() == userToBeUpdated.getUserId()) {
                        SessionStore.setLoggedInUser(zilliousReq, null, userToBeUpdated);
                    }
                }
            }

            String completeURL = s3CloudClient.getCompleteDisplayURL(objectKey);
            if (completeURL != null) {
                userToBeUpdated.getUserProfile().addDisplayPictureURL(responseObject, completeURL);
            }

            responseObject.addProperty("user_id", userToBeUpdated.getUserId());

        } catch (Exception e) {
            s_logger.error("Exception while adding/updating the user profile picture data", e);

            if (isAddedToCloud && !isProfileUpdated) {
                s_logger.info("Since profile has not been updated and the file has already been uploaded, the file is deleted from the cloud");
                s3CloudClient.deleteFile(objectKey);
            }

            throw e;
        }

        JsonUtility.createResponseJson(responseObject, true, "");
        return responseObject;
    }

    @Override
    @Transactional
    public JsonObject deleteProfilePicture(User profileUpdator, String requestJson,
            ZilliousSecurityWrapperRequest zilliousReq) {
        JsonObject responseObject = new JsonObject();
        JsonParser parser = new JsonParser();

        try {
            JsonObject requestObject = (JsonObject) parser.parse(requestJson);

            User userToBeUpdated = m_userDao.getUser(requestObject.get("user_id").getAsInt());

            if (userToBeUpdated == null) {
                JsonUtility.createResponseJson(responseObject, false, WebsiteExceptionType.INVALID_PROFILE.getDesc());
                return responseObject;
            }

            if (profileUpdator.getUserId() != userToBeUpdated.getUserId()
                    && !profileUpdator.getUserRole().isAdminRole()) {
                JsonUtility.createResponseJson(responseObject, false, WebsiteExceptionType.INVALID_ACCESS.getDesc());
                return responseObject;
            }

            if (userToBeUpdated.getUserProfile().getPictureKey() == null) {
                JsonUtility.createResponseJson(responseObject, false, WebsiteExceptionType.MISSINGDATA.getDesc());
                return responseObject;
            }

            CloudManager s3Uploader = CloudManager.AMAZONS3;
            boolean isSuccess = s3Uploader.deleteFile(userToBeUpdated.getUserProfile().getPictureKey());
            if (isSuccess) {
                // deleting from database
                userToBeUpdated.getUserProfile().setPictureKey(null);
                m_userDao.updateProfile(userToBeUpdated);
                if (profileUpdator.getUserId() == userToBeUpdated.getUserId()) {
                    SessionStore.setLoggedInUser(zilliousReq, null, userToBeUpdated);
                }
            }
        } catch (Exception e) {
            s_logger.error("Exception while deleting the user profile picture", e);
            throw e;
        }

        JsonUtility.createResponseJson(responseObject, true, "");
        return responseObject;
    }

    @Override
    @Transactional
    public List<String> getAllEmailIDs() {
        return m_userDao.getAllEmailIDs();
    }

    @Override
    @Transactional
    public JsonObject checkIfAdmin(String requestJson) {
        JsonObject response = new JsonObject();
        try {
            JsonParser parser = new JsonParser();
            JsonObject useridJsonObj = (JsonObject) parser.parse(requestJson);
            int userId = useridJsonObj.get("user_id").getAsInt();

            User user = m_userDao.getProfilebyUserID(userId);
            boolean isAdminRole = user.getUserRole().isAdminRole();
            response.addProperty("isAdmin", isAdminRole);
        } catch (Exception e) {
            response = new JsonObject();
            response.addProperty("isAdmin", false);
            JsonUtility.createResponseJson(response, false, WebsiteExceptionType.OTHER.getDesc());
        }
        return response;
    }

    @Override
    @Transactional
    public JsonArray getTeamMembersByNamePattern(String queryString, User loggedInUser) {
        try {

            if (loggedInUser.getUserRole().isAdminRole()) {
                return getUsersByNamePattern(queryString);
            }

            queryString = queryString.toLowerCase();
            loggedInUser = m_userDao.getUser(loggedInUser.getUserId());
            Set<User> users = new HashSet<User>();

            if (loggedInUser.getUserProfile().getName().toLowerCase().contains(queryString)) {
                users.add(loggedInUser);
            }

            for (Team team : loggedInUser.getManagedTeams()) {
                for (User user : team.getMembers()) {
                    if (user.getUserProfile().getName().toLowerCase().contains(queryString)) {
                        users.add(user);
                    }
                }
            }

            JsonArray userListJson = User.getUserListJsonObject(users);
            if (userListJson == null) {
                userListJson = new JsonArray();
            }

            return userListJson;

        } catch (Exception e) {
            s_logger.error("Error", e);
        }

        return new JsonArray();
    }

    @Override
    @Transactional
    public Map<Integer, User> getUsers(List<User> usersToFetch) {
        if (usersToFetch == null || usersToFetch.isEmpty()) {
            return null;
        }

        return m_userDao.getUsers(usersToFetch);
    }

    @Override
    @Transactional
    public boolean updateUsers(Collection<User> users) {
        return m_userDao.updateUsers(users);
    }

    @Override
    public void sendUserRemovedFromTeamNotification(Team team, User member, List<User> ccUsers)
            throws MessagingException {
        EmailMessage emailMessage = new EmailMessage();
        emailMessage.setSubject(sendUserRemovedFromTeamEmailSubject());
        String content = sendUserRemovedFromTeamEmailContent(team);
        emailMessage.setContent(content);
        EmailSender.sendEmail(emailMessage);

    }

    public String sendUserRemovedFromTeamEmailContent(Team team) {

        StringBuilder content = new StringBuilder("Hi,");
        content.append(StringUtility.getNewLineString());
        content.append(StringUtility.getNewLineString());

        content.append("You have been removed from team "
                + StringUtility.trimAndNullIsDefault(team.getName(), team.getEmailgp()) + ".");
        content.append(StringUtility.getNewLineString());

        content.append(StringUtility.getNewLineString());
        content.append("Thanks").append(StringUtility.getNewLineString()).append("Zillious Solutions Pvt Ltd");
        return content.toString();
    }

    public String sendUserRemovedFromTeamEmailSubject() {
        return "Removed From Team ";
    }

    /**
     * get email message summaries for all users
     * 
     * @return list of email messages of all users
     */
    @Transactional
    @Override
    public List<EmailMessage> getSchedularEmailSummaryForUsers() {
        Map<User, WeeklySummaryMessage> usersSchedularEmailSummmary = null;
        List<EmailMessage> emailMessagesOfAllUsers = null;

        // get all the active users in the system
        List<User> allUsers = getAllUsers(false);

        if (CollectionsUtility.isCollectionEmpty(allUsers)) {
            s_logger.error("Could not get the list of active users");
            return null;
        }
        if (allUsers != null && !allUsers.isEmpty()) {
            usersSchedularEmailSummmary = new HashMap<User, WeeklySummaryMessage>();
            emailMessagesOfAllUsers = new ArrayList<EmailMessage>();
        }

        List<User> usersHavingBirthdayToday = new ArrayList<User>();
        List<User> usersHavingBirthdayThisWeek = new ArrayList<User>();

        Date currentDate = DateUtility.getCurrentDate();
        String currentDateStr = DateUtility.getDateInDDMMYYYYDash(currentDate);

        // --------------for birthdays----------------
        for (User user : allUsers) {
            String userBdayStr = user.getUserProfile().getdob();

            // comparing month and date of birthday as birthday year stored in
            // database
            // and current year are not same .
            Date userBdayDate = DateUtility.parseDateInDDMMYYYYDashNullIfError(userBdayStr);

            if (userBdayDate == null) {
                continue;
            }

            int monthOfUserBirthday = DateUtility.getMonthOfDate(userBdayDate);
            int currentMonth = DateUtility.getMonthOfDate(currentDate);
            if (currentMonth == monthOfUserBirthday) {

                String dateToday = DateUtility.getDateDD(currentDate);
                String dateOfUserBday = DateUtility.getDateDD(userBdayDate);
                String lastDateOfWeekStr = DateUtility.getDateInDDMMYYYYDash(DateUtility.getLastDateOfTheWeek());

                if (dateToday.equals(dateOfUserBday)) {
                    // for birthdays today
                    usersHavingBirthdayToday.add(user);
                } else if (userBdayStr.compareTo(currentDateStr) > 0 && userBdayStr.compareTo(lastDateOfWeekStr) < 0) {
                    // for birthdays this week
                    usersHavingBirthdayThisWeek.add(user);
                }
            }
        }

        // ----------------for leaves--------------
        for (User user : allUsers) {

            Set<LeaveRequest> pendingLeaveRequests = null;
            Set<LeaveRequest> teamLeavesToday = null;
            Set<LeaveRequest> teamLeavesThisWeek = null;

            // set of teams where user is a supervisor
            Set<Team> teamsWhereUserIsSupervisor = user.getManagedTeams();

            // set of teams where user is a member
            Set<Team> teamsWhereUserIsMember = user.getTeamlist();

            // set of all teams of a user
            Set<Set<Team>> userAllTeams = new HashSet<Set<Team>>();
            if (teamsWhereUserIsSupervisor != null && !teamsWhereUserIsSupervisor.isEmpty()) {
                userAllTeams.add(teamsWhereUserIsSupervisor);
            }
            if (teamsWhereUserIsMember != null && !teamsWhereUserIsMember.isEmpty()) {
                userAllTeams.add(teamsWhereUserIsMember);
            }

            if (userAllTeams != null) {
                teamLeavesToday = new HashSet<LeaveRequest>();
                teamLeavesThisWeek = new HashSet<LeaveRequest>();
                pendingLeaveRequests = new HashSet<LeaveRequest>();

                for (Set<Team> teams : userAllTeams) {

                    for (Team team : teams) {
                        Set<User> teamMembers = team.getMembers();
                        for (User teamMember : teamMembers) {

                            // gets leave requests of the user in this week
                            List<LeaveRequest> leaveRequestsThisWeek = m_leavesDao.getWeekLeaveRequestsForUser(
                                    teamMember, currentDate, DateUtility.getLastDateOfTheWeek());

                            if (leaveRequestsThisWeek != null) {

                                for (LeaveRequest leaveRequest : leaveRequestsThisWeek) {

                                    // get week of the month in which leave
                                    // requests lies
                                    int leaveStartWeek = DateUtility.getWeekOfMonth(leaveRequest.getStartDate());
                                    int leaveEndWeek = DateUtility.getWeekOfMonth(leaveRequest.getEndDate());

                                    // get current week
                                    int currentWeek = DateUtility.getWeekOfMonth(currentDate);

                                    String leaveStartDateStr = DateUtility.getDateInDDMMYYYYDash(leaveRequest
                                            .getStartDate());
                                    String leaveEndDateStr = DateUtility.getDateInDDMMYYYYDash(leaveRequest
                                            .getEndDate());

                                    if (leaveStartWeek == currentWeek || leaveEndWeek == currentWeek
                                            && (currentDateStr.compareTo(leaveStartDateStr) > 0)
                                            && (currentDateStr.compareTo(leaveEndDateStr) < 0)) {

                                        // only for leaves of a team member that
                                        // are approved
                                        if (leaveRequest.getRequestStatus() == LeaveRequestStatus.APPROVED) {

                                            // for leaves today that are
                                            // approved
                                            if (leaveStartDateStr.equals(currentDateStr)) {
                                                teamLeavesToday.add(leaveRequest);
                                            }

                                            // for leaves this week that
                                            // are approved
                                            else {
                                                teamLeavesThisWeek.add(leaveRequest);
                                            }
                                        }
                                    }

                                    // for pending leave requests-- only for
                                    // users who are supervisors
                                    if (team.getSupervisor().getUserId() == (user.getUserId())) {
                                        if (leaveRequest.getRequestStatus() == LeaveRequestStatus.PENDING) {
                                            pendingLeaveRequests.add(leaveRequest);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // check for not adding users whose content is empty
            if (!usersHavingBirthdayToday.isEmpty() || !usersHavingBirthdayThisWeek.isEmpty()
                    || !pendingLeaveRequests.isEmpty() || !teamLeavesToday.isEmpty() || !teamLeavesThisWeek.isEmpty()) {

                // data population block
                WeeklySummaryMessage dailyUserSummary = new WeeklySummaryMessage();
                if (!usersHavingBirthdayToday.isEmpty()) {
                    dailyUserSummary.setBirthdayToday(usersHavingBirthdayToday);
                }
                if (!usersHavingBirthdayThisWeek.isEmpty()) {
                    dailyUserSummary.setBirthdayThisWeek(usersHavingBirthdayThisWeek);
                }
                if (!pendingLeaveRequests.isEmpty()) {
                    dailyUserSummary.setPendingTeamLeaves(pendingLeaveRequests);
                }
                if (!teamLeavesToday.isEmpty()) {
                    dailyUserSummary.setTeamLeavesToday(teamLeavesToday);
                }
                if (!teamLeavesThisWeek.isEmpty()) {
                    dailyUserSummary.setLeavesThisWeek(teamLeavesThisWeek);
                }

                // add scheduler summary for a user
                usersSchedularEmailSummmary.put(user, dailyUserSummary);
            }
        }

        // Email content building logic
        if (!usersSchedularEmailSummmary.isEmpty()) {
            for (Map.Entry<User, WeeklySummaryMessage> entry : usersSchedularEmailSummmary.entrySet()) {
                User user = entry.getKey();
                WeeklySummaryMessage userSummaryMessage = entry.getValue();

                EmailMessage emailMessage = new EmailMessage();
                String content = user.getSchedularEmailSummaryContent(userSummaryMessage);
                emailMessage.setContent(content);
                emailMessage.addTo(user.getEmail());
                emailMessage.setSubject("Weekly Summary");
                emailMessage.setFrom("info@zillious.com");

                // add email message for a particular user to the list
                // emailMessageOfAllUsers
                emailMessagesOfAllUsers.add(emailMessage);
            }
        }
        return emailMessagesOfAllUsers;
    }

    @Override
    public List<User> getAllUsers(boolean includeNonActiveUsers) {
        List<User> userslist = this.m_userDao.getAllUsersList(includeNonActiveUsers);
        return userslist;
    }

    @Transactional
    @Override
    public JsonArray getAllUsersAsJsonForAttendance(String dateStr) {
        // TODO Auto-generated method stub
        List<User> usersList = getAllUsers();
        JsonArray userDetails = new JsonArray();
        if (usersList != null) {

            Calendar cal1 = Calendar.getInstance();
            
            Date date=DateUtility.parseDateInDDMMYYYYNullIfError(dateStr);
            //String d = DateUtility.getDateInDDMMYYYYHHMMSS(date);

            for(User user : usersList){
                Set<User> thisUser = new HashSet<>();
                thisUser.add(user);
                List<AttendanceRecord> records=    m_attendanceDao.getAttendanceRecords(date, date, thisUser);
                if(!records.isEmpty() && records !=null){
                    Date inTime  = records.get(0).getKey().getGenTs();
                    Date outTime  = records.get(records.size() - 1).getKey().getGenTs();
                    String inTimeStr = DateUtility.getDateInHHMM(inTime);
                    String outTimeStr = DateUtility.getDateInHHMM(outTime);
                    user.setInTime(inTimeStr);
                    user.setOutTime(outTimeStr);
                    userDetails .add(user.convertUserListToCompleteJson());
                } else {
                    user.setInTime(DateUtility.getDateInHHMM(cal1.getTime()));
                    user.setOutTime(DateUtility.getDateInHHMM(cal1.getTime()));
                    userDetails .add(user.convertUserListToCompleteJson());
                }
//               userDetails .add(user.convertUserListToCompleteJson());
            }
        }

//        for (User user : usersList) {
//           
//        }
        return userDetails;
    
    }

}
