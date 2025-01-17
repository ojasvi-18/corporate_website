package com.zillious.corporate_website.portal.dao;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.zillious.corporate_website.portal.ui.UserRoles;
import com.zillious.corporate_website.portal.ui.model.User;
import com.zillious.corporate_website.ui.navigation.WebsiteException;

@Component
public interface UserDao extends Dao {

    /**
     * This function returns the list of all employees present in the database.
     * 
     * @return
     */
    List<User> getAllUsersList();

    public User getUser(int id);

    public boolean persistUser(User user);

    public boolean updateUser(User user);

    public boolean deleteUser(User user);

    public List<User> getUsersByNamePattern(String queryString);

    public User getUserByEmail(String email) throws WebsiteException;

    /**
     * This api adds profiles for the users who do not have profiles(which were
     * created before the new portal flow)
     */
    public boolean syncUserProfiles();

    /**
     * This api fetches and returns the list of users that have birthday on the
     * current date(today) from the database
     */
    List<User> getUsersBirthdayList();

    User getProfilebyUserID(int userId);

    boolean updateProfile(User userProfile);

    boolean updateUsers(User[] users);

    /**
     * This api takes in the user roles and returns a list of users who have
     * these roles
     * 
     * @param userRolesToFetch
     * @return
     */
    List<User> getAllUsersOfRole(Set<UserRoles> userRolesToFetch);

    /**
     * @return email ids of all the users in the system
     */
    List<String> getAllEmailIDs();

    /**
     * @param usersToFetch
     * @return
     */
    Map<Integer, User> getUsers(List<User> usersToFetch);

    boolean updateUsers(Collection<User> users);

    /**
     * @param includeNonActiveUsers
     * @return
     */
    public List<User> getAllUsersList(boolean includeNonActiveUsers);

    /**
     * This api returns the list of users having birthday this week
     * 
     * @param currentDateInString
     * @param lastDateOfWeekInString
     * @return
     */
   
}
