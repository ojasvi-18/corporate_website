package com.zillious.corporate_website.portal.dao.impl;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.zillious.corporate_website.portal.dao.LoginDao;
import com.zillious.corporate_website.portal.ui.model.User;

/**
 * This class implements the functions of class LoginDao.
 * 
 * @author nishant.gupta
 *
 */

@Repository
public class LoginDaoImpl implements LoginDao {

    @Autowired
    SessionFactory        sessionFactory;

    private static Logger s_logger = Logger.getLogger(LoginDaoImpl.class);

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
    public void loginUser(User user) {

    }

}
