package com.zillious.corporate_website.portal.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;

import org.springframework.stereotype.Component;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.zillious.corporate_website.portal.entity.EmailMessage;
import com.zillious.corporate_website.portal.ui.dto.FormDataWithFile;
import com.zillious.corporate_website.portal.ui.model.Team;
import com.zillious.corporate_website.portal.ui.model.User;
import com.zillious.corporate_website.ui.navigation.WebsiteException;
import com.zillious.corporate_website.ui.security.ZilliousSecurityWrapperRequest;

/**
 * @author satyam.mittal
 *
 */
@Component
public interface UserService {

    /**
     * this method will return list of all employees for employee.html page
     * 
     * @return
     */
    public JsonArray getAllUsersAsJson();

    /**
     * this method will return user details according to the id provided
     * 
     * @param ID
     * @return
     */
    public User getUser(int ID);

    public JsonObject addUser(String t);

    public JsonObject updateUser(String t);

    public JsonObject deleteUser(String t);

    /*
     * this function will return the list of users(all employees) for the type
     * ahead on the UI
     */
    public JsonArray getUsersByNamePattern(String queryString);

    public boolean isValidUser(User user) throws WebsiteException;

    public boolean syncUserProfiles();

    /**
     * This api fetches and returns the list of users that have birthday on the
     * current date(today)
     */
    public List<User> getUsersBirthdayList();

    public JsonObject getJsonForProfileByUserID(User profileUpdator, int userId);

    public JsonObject updateProfile(User profileUpdator, String jsonRequest);

    public boolean updateUser(User user);

    public boolean updateUsers(User... users);

    /**
     * 
     * this api will return the valid authorized user trying to login if it
     * exists in the system
     * 
     * @param email
     * @param password
     * @return
     * @throws WebsiteException
     */
    public User getAuthorizedUser(User user) throws Exception;

    /**
     * @returns a list of users from the database who have user role as Director
     * 
     */
    public List<User> getAdminRoleUsers();

    /**
     * this method is to send emails to the person whose bday is on current date
     * 
     * @param from
     * @param wishesJson
     * @return
     * @throws Exception
     */
    public boolean sendBirthdayWishes(User from, String wishesJson) throws Exception;

    public List<User> getAllUsers();

    public JsonObject updateProfilePicture(User profileUpdator, FormDataWithFile formDataWithFile,
            ZilliousSecurityWrapperRequest zReq) throws Exception;

    public JsonObject deleteProfilePicture(User profileUpdator, String requestJson,
            ZilliousSecurityWrapperRequest zilliousRequest);

    /**
     * @return list of e-mail ids of all the users in the system
     */
    public List<String> getAllEmailIDs();

    public JsonObject checkIfAdmin(String requestJson);

    public JsonArray getTeamMembersByNamePattern(String queryString, User loggedInUser);

    /**
     * @param usersToFetch
     * @return
     */
    public Map<Integer, User> getUsers(List<User> usersToFetch);

    public boolean updateUsers(Collection<User> users);

    /**
     * @param team
     * @param member
     * @param ccUsers
     * @throws MessagingException
     */
    public void sendUserRemovedFromTeamNotification(Team team, User member, List<User> ccUsers)
            throws MessagingException;

    /**
     * This api gets the schedular email message summary for all active users (including to , from and content )
     * 
     * @return list of email messages of all users
     * 
     */
    public List<EmailMessage> getSchedularEmailSummaryForUsers();

    /**
     * Checks whether to include users who are disabled/suspended
     * 
     * @param includeNonActiveUsers
     * @return
     */
    public List<User> getAllUsers(boolean includeNonActiveUsers);

    public JsonArray getAllUsersAsJsonForAttendance(String dateStr);

    
}
