package com.zillious.corporate_website.portal.dao.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import com.zillious.corporate_website.portal.dao.UserDao;
import com.zillious.corporate_website.portal.ui.UserRoles;
import com.zillious.corporate_website.portal.ui.UserStatus;
import com.zillious.corporate_website.portal.ui.model.User;
import com.zillious.corporate_website.portal.ui.model.UserProfile;
import com.zillious.corporate_website.ui.navigation.WebsiteException;
import com.zillious.corporate_website.ui.navigation.WebsiteExceptionType;
import com.zillious.corporate_website.utils.DateUtility;

@EnableTransactionManagement
@Repository
public class UserDaoImpl implements UserDao {

    private static Logger  s_logger = Logger.getLogger(UserDaoImpl.class);
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public Session getSession() {
        // return sessionFactory.openSession();
        return sessionFactory.getCurrentSession();
    }

    @Override
    public void closeSession(final Session session) {
        try {
            if (session != null) {
                session.flush();
                // session.close();
            }
        } catch (Exception e) {
            s_logger.error("Error while closing the session", e);
        }
    };

    /*
     * this function will return the profile of particular user whose id is
     * received
     */
    @Transactional
    @Override
    public User getUser(int ID) {
        Session session = null;
        User user = null;

        try {
            session = getSession();
            Criteria cr = session.createCriteria(User.class);
            cr.add(Restrictions.eq("m_userId", ID));
            cr.add(Restrictions.ne("m_UserRole", UserRoles.SUPER));
            Object uniqueResult = cr.uniqueResult();
            if (uniqueResult != null) {
                user = (User) uniqueResult;
            }

        } catch (Exception e) {
            s_logger.error("Error while getting user from db: ", e);
        } finally {
            closeSession(session);
        }

        return user;
    }

    @Transactional
    @Override
    public boolean persistUser(final User user) {
        Session session = null;
        try {
            session = getSession();
            session.save(user);
            return true;
        } catch (Exception e) {
            s_logger.error("Error while adding a user: ", e);
            throw e;
        } finally {
            closeSession(session);
        }
    }

    @Transactional
    @Override
    public boolean updateUser(User user) {
        Session session = null;
        try {
            session = getSession();
            session.update(user);
            return true;
        } catch (Exception e) {
            throw e;
        } finally {
            closeSession(session);
        }
    }

    @Transactional
    @Override
    public boolean deleteUser(User user) {
        Session session = null;
        try {
            session = getSession();
            session.delete(user);

        } catch (Exception e) {
            s_logger.error("Error while deleting a user: ", e);
            throw new RuntimeException("Error in deleting user");
        } finally {
            closeSession(session);
        }

        return true;
    }

    @Transactional
    @Override
    public List<User> getUsersByNamePattern(String queryString) {

        List<User> userList = new ArrayList<User>();
        Session session = null;

        try {
            session = getSession();
            Criteria cr = session.createCriteria(User.class).createAlias("m_userProfile", "profile")
                    .add(Restrictions.ilike("profile.m_name", queryString, MatchMode.ANYWHERE));
            cr.add(Restrictions.ne("m_UserRole", UserRoles.SUPER));
            userList = cr.list();

        } catch (Exception e) {
            s_logger.error("Error while getting teams from db: ", e);
        } finally {
            closeSession(session);
        }

        return userList;
    }

    @Override
    @Transactional
    public List<User> getAllUsersList() {
        return getAllUsersList(false);
    }

    @Override
    @Transactional
    public User getUserByEmail(String email) throws WebsiteException {
        Session session = null;
        User user = null;

        try {
            session = getSession();
            Criteria cr = session.createCriteria(User.class);
            cr.add(Restrictions.eq("m_email", email).ignoreCase());
            Object uniqueResult = cr.uniqueResult();
            if (uniqueResult != null) {
                user = (User) uniqueResult;
            }

        } catch (Exception e) {
            s_logger.error("Error while getting user from db: ", e);
            throw new WebsiteException(WebsiteExceptionType.SYSTEM_ERROR);
        } finally {
            closeSession(session);
        }

        return user;
    }

    @Override
    @Transactional
    public boolean syncUserProfiles() {
        List<User> userList = null;
        Session session = null;

        try {
            session = getSession();
            Criteria cr = session.createCriteria(User.class);
            cr.add(Restrictions.isNull("m_userProfile"));
            List list = cr.list();
            if (list == null) {
                return true;
            }

            userList = (List<User>) list;
            for (User user : userList) {
                user.setUserProfile(new UserProfile(""));
                session.saveOrUpdate(user);
            }

            return true;
        } catch (Exception e) {
            s_logger.error("Error while creating profiles for the users who do not have a profile: ", e);
        } finally {
            closeSession(session);
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<User> getUsersBirthdayList() {
        Session session = null;

        try {
            session = getSession();
            String currentDateToMatch = DateUtility.getDateInDDMMDash(DateUtility.getCurrentDate());
            Criteria cr = session.createCriteria(User.class).createAlias("m_userProfile", "profile")
                    .add(Restrictions.like("profile.m_dob", currentDateToMatch, MatchMode.START));
            cr.add(Restrictions.ne("m_UserRole", UserRoles.SUPER));
            List list = cr.list();
            if (list == null) {
                return null;
            }

            return (List<User>) list;
        } catch (Exception e) {
            s_logger.error("Error while getting users who have birthdays today: ", e);
        } finally {
            closeSession(session);
        }

        return null;
    }

    @Override
    @Transactional
    public User getProfilebyUserID(int userId) {
        User user = getUser(userId);
        return user;
    }

    @Override
    public boolean updateProfile(User user) {
        Session session = null;

        try {
            session = getSession();
            session.saveOrUpdate(user);
        } catch (Exception e) {
            s_logger.error("Error in updating profile information profile", e);
            throw new RuntimeException("Error in updating profile information profile" + e);
        } finally {
            closeSession(session);
        }

        return true;

    }

    /*
     * Imperfect implementation. need to check for hibernate template to
     * saveandupdate all entities together. (non-Javadoc)
     */
    @Transactional
    @Override
    public boolean updateUsers(User[] users) {
        Session session = null;
        try {
            session = getSession();
            Transaction transaction = session.getTransaction();
            for (User user : users) {
                session.saveOrUpdate(user);
            }
            transaction.commit();
            return true;
        } catch (Exception e) {
            throw new RuntimeException("Error in updating users");
        } finally {
            closeSession(session);
        }
    }

    @Override
    public List<User> getAllUsersOfRole(Set<UserRoles> userRolesToFetch) {

        if (userRolesToFetch == null || userRolesToFetch.isEmpty()) {
            return null;
        }

        Session session = null;
        List<User> userList = null;

        try {
            session = getSession();

            Criteria cr = session.createCriteria(User.class).add(Restrictions.in("m_UserRole", userRolesToFetch));
            cr.add(Restrictions.ne("m_UserRole", UserRoles.SUPER));
            List objList = cr.list();
            if (objList != null) {
                userList = new ArrayList<User>();
                for (Object obj : objList) {
                    userList.add((User) obj);
                }
            }

        } catch (Exception e) {
            s_logger.error("Error while getting user from db: ", e);
        } finally {
            closeSession(session);
        }

        return userList;
    }

    @Override
    public List<String> getAllEmailIDs() {

        Session session = null;

        try {
            session = getSession();

            Criteria cr = session.createCriteria(User.class);
            cr.setProjection(Projections.projectionList().add(Projections.property("m_email")));
            List objList = cr.list();
            if (objList != null) {
                return (List<String>) objList;
            }

        } catch (Exception e) {
            s_logger.error("Error while getting email id's from the database", e);
        } finally {
            closeSession(session);
        }

        return null;
    }

    @Override
    public Map<Integer, User> getUsers(List<User> usersToFetch) {

        if (usersToFetch == null || usersToFetch.isEmpty()) {
            return null;
        }

        Session session = null;
        Map<Integer, User> userMap = null;

        try {
            session = getSession();

            List<Integer> ids = new ArrayList<Integer>();
            for (User user : usersToFetch) {
                ids.add(user.getUserId());
            }

            Criteria cr = session.createCriteria(User.class).add(Restrictions.in("m_userId", ids));
            cr.add(Restrictions.ne("m_UserRole", UserRoles.SUPER));
            List objList = cr.list();
            if (objList != null) {
                userMap = new HashMap<Integer, User>();
                for (Object obj : objList) {
                    User user = (User) obj;
                    userMap.put(user.getUserId(), user);
                }
            }

        } catch (Exception e) {
            s_logger.error("Error while getting user from db: ", e);
        } finally {
            closeSession(session);
        }

        return userMap;
    }

    /*
     * Imperfect implementation. need to check for hibernate template to
     * saveandupdate all entities together. (non-Javadoc)
     */
    @Transactional
    @Override
    public boolean updateUsers(Collection<User> users) {
        Session session = null;
        try {
            session = getSession();
            for (User user : users) {
                session.saveOrUpdate(user);
            }
            return true;
        } catch (Exception e) {
            throw new RuntimeException("Error in updating users");
        } finally {
            closeSession(session);
        }
    }

    @Transactional
    @Override
    public List<User> getAllUsersList(boolean includeNonActiveUsers) {
        List<User> usersList = new ArrayList<User>();
        Session session = null;
        try {
            session = getSession();
            Criteria cr = session.createCriteria(User.class);
            cr.add(Restrictions.ne("m_UserRole", UserRoles.SUPER));
            if (!includeNonActiveUsers) {
                cr.add(Restrictions.eq("m_status", UserStatus.Enabled));
            }
            usersList = (List<User>) cr.list();
        } catch (Exception e) {
            s_logger.error("stacktrace", e);
            return null;
        } finally {
            closeSession(session);
        }
        return usersList;
    }

}
