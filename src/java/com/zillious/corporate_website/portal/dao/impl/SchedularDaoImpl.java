package com.zillious.corporate_website.portal.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import com.zillious.corporate_website.portal.dao.SchedularDao;
import com.zillious.corporate_website.portal.ui.model.SchedularEmail;
import com.zillious.corporate_website.utils.DateUtility;

@EnableTransactionManagement
@Repository
public class SchedularDaoImpl implements SchedularDao {

    @Autowired
    private SessionFactory sessionFactory;

    private static Logger  s_logger = Logger.getLogger(SchedularDaoImpl.class);

    @Override
    public Session getSession() {
        return sessionFactory.getCurrentSession();

    }

    @Override
    public void closeSession(final Session session) {
        try {
            if (session != null) {
                session.flush();
            }
        } catch (Exception e) {
            s_logger.error("Error while closing the session", e);
        }
    };

    @Override
    public void addSchedularEmail(SchedularEmail schedularEmail) {
        // TODO Auto-generated method stub
        Session session = null;

        try {
            session = getSession();
            if(session != null){
                session.save(schedularEmail);
            }
        } catch (Exception e) {
            s_logger.error("Error in adding schedular info to db", e);
        } finally {
            closeSession(session);
        }

    }

    @Transactional
    @Override
    public SchedularEmail getTheLastEmail() {
        Session session = null;
        try {
            session = getSession();
            if (session != null) {
                Criteria c = session.createCriteria(SchedularEmail.class);
                c.addOrder(Order.desc("id"));
                c.setMaxResults(1);
                SchedularEmail emailResult = (SchedularEmail) c.uniqueResult();
                return emailResult;
            }
        } catch (Exception e) {
            s_logger.error("error in retreiving the last email status ", e);

        } finally {
            closeSession(session);
        }
        return null;
    }

}
