package com.zillious.corporate_website.portal.dao.impl;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zillious.corporate_website.portal.dao.ProfileDao;
import com.zillious.corporate_website.portal.service.UserService;
import com.zillious.corporate_website.portal.ui.model.User;

@EnableTransactionManagement
@Repository
public class ProfileDaoImpl implements ProfileDao {
    public static Logger   s_logger = Logger.getLogger(ProfileDaoImpl.class);
    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    UserService            userService;

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

    @Override
    public boolean addProfile(User user) {
        Session session = null;

        try {
            session = getSession();
            // UserProfile userp = user.getUserprofile();
            // session.save(userp);
            // int id = userp.getid();

            session.flush();

        } catch (Exception e) {
            s_logger.error(e);
            throw new RuntimeException("Error in adding profile information");
        } finally {
            closeSession(session);
        }

        return true;
    }

}
