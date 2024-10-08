package com.zillious.corporate_website.portal.dao.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import com.zillious.corporate_website.portal.dao.AttendanceDao;
import com.zillious.corporate_website.portal.ui.model.AttendanceRecord;
import com.zillious.corporate_website.portal.ui.model.User;

/**
 * @author nishant.gupta
 *
 */
@Repository
@EnableTransactionManagement
public class AttendanceDaoImpl implements AttendanceDao {

    private static Logger  s_logger = Logger.getLogger(AttendanceDaoImpl.class);

    @Autowired
    private SessionFactory sessionFactory;

    /*
     * (non-Javadoc)
     * 
     * @see com.zillious.corporate_website.portal.dao.Dao#getSession()
     */
    @Override
    public Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.zillious.corporate_website.portal.dao.Dao#closeSession(org.hibernate
     * .Session)
     */
    @Override
    public void closeSession(Session session) {
        try {
            if (session != null) {
                session.flush();
                // session.close();
            }
        } catch (Exception e) {
            s_logger.error("Error while closing the session", e);
        }
    }

    @Override
    @Transactional
    public List<AttendanceRecord> getAttendanceRecords(Date startDate, Date endDate, Set<User> forUser) {
        List<AttendanceRecord> records = new ArrayList<AttendanceRecord>();
        Session session = null;
        try {
            session = getSession();
            Criteria cr = session.createCriteria(AttendanceRecord.class);
            Calendar end = Calendar.getInstance();
            end.setTime(endDate);
            end.add(Calendar.DATE, 1);
            cr.add(Restrictions.ge("m_key.m_genTs", startDate));
            cr.add(Restrictions.lt("m_key.m_genTs", end.getTime()));
            if (forUser != null) {
                cr.add(Restrictions.in("m_key.m_employee", forUser));
            }
            records = cr.list();
            return records;
        } catch (Exception e) {
            s_logger.error("stacktrace", e);
            return null;
        } finally {
            closeSession(session);
        }
    }
    
    @Override
    @Transactional
    public List<AttendanceRecord> getAttendanceRecordsForUser(Date startDate , Date endDate , User user){
        List<AttendanceRecord> records = new ArrayList<AttendanceRecord>();
        Session session = null;
        try{
            session = getSession();
            if(session != null){
                Criteria cr = session.createCriteria(AttendanceRecord.class);
                Criterion start = Restrictions.gt("m_key.m_genTs", startDate);
                Criterion end = Restrictions.lt("m_key.m_genTs", endDate);
                LogicalExpression exp = Restrictions.and(start, end);
                cr.add(exp);
                records = cr.list();
                
            }
        } catch(Exception e){
            s_logger.error("stacktrace",e);
        } finally{
            closeSession(session);
        }
        return records;
    }

    @Transactional
    @Override
    public boolean saveAttendanceEntry(AttendanceRecord record) {

        Session session = null;
        try {
            session = getSession();
            session.save(record);
            return true;
        } catch (Exception e) {
            s_logger.error("Error while adding a user: ", e);
            throw e;
        } finally {
            closeSession(session);
        }
    }

    @Override
    public void deleteAttendanceEntry(AttendanceRecord record) {

        Session session = null ;
        try{
            session = getSession();
            if(session !=null && record != null ){
                session.delete(record);
            }
        }catch (Exception e){
            s_logger.error("Error while deleteing attendance record");
        } finally {
            closeSession(session);
        }
    }
}


