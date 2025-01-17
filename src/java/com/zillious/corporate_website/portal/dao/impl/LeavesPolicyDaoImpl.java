
package com.zillious.corporate_website.portal.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import com.zillious.corporate_website.portal.dao.LeavesPolicyDao;
import com.zillious.corporate_website.portal.ui.EventType;
import com.zillious.corporate_website.portal.ui.model.LeavePolicy;
import com.zillious.corporate_website.portal.ui.model.YearlyCalendar;
import com.zillious.corporate_website.utils.DateUtility;

/**
 * @author satyam.mittal
 *
 */
@EnableTransactionManagement
@Repository
public class LeavesPolicyDaoImpl implements LeavesPolicyDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public Session getSession() {
        return sessionFactory.openSession();
    }

    @Override
    public void closeSession(final Session session) {
        try {
            if (session != null) {

                session.flush();
                session.close();
            }
        } catch (Exception e) {
            s_logger.error("Error while closing the session", e);
        }
    };

    private static Logger s_logger = Logger.getLogger(LeavesPolicyDaoImpl.class);

    @Transactional
    @Override
    public List<YearlyCalendar> getEventsFromCalendar(EventType eventType, String year) {
        try {
            Date firstDateOfYear = null;
            Date lastDateOfYear = null;
            if (year != null) {
                firstDateOfYear = DateUtility.getFirstDateOfYear(year);
                lastDateOfYear = DateUtility.getLastDateOfYear(year);
            }

            return getEventsFromCalendar(eventType, firstDateOfYear, lastDateOfYear);
        } catch (Exception e) {
            s_logger.error("error in getting holidays from calendar", e);
            return null;
        }
    }

    @Transactional
    @Override
    public boolean addHolidayToCalendar(YearlyCalendar newHoliday) {
        Session session = null;
        try {
            session = getSession();
            session.saveOrUpdate(newHoliday);
            return true;
        } catch (Exception e) {
            s_logger.error("Error while adding a holiday: ", e);
            throw new RuntimeException("Error in adding information");
        } finally {
            closeSession(session);
        }

    }

    @Transactional
    @Override
    public boolean updateHolidayCalendar(YearlyCalendar updatedHoliday) {
        Session session = null;
        try {
            session = getSession();
            session.update(updatedHoliday);
            session.flush();
            return true;
        } catch (Exception e) {
            s_logger.error("Error while updating a holiday: ", e);
            throw new RuntimeException("Error in updating holiday information");

        } finally {
            closeSession(session);
        }
    }

    @Transactional
    @Override
    public List<LeavePolicy> getLeavesPolicy() {
        List<LeavePolicy> leavesPolicyList = new ArrayList<LeavePolicy>();
        Session session = null;
        try {
            session = getSession();
            Criteria cr = session.createCriteria(LeavePolicy.class);
            leavesPolicyList = cr.list();
        } catch (Exception e) {
            s_logger.error("stacktrace", e);
        } finally {
            closeSession(session);
        }
        return leavesPolicyList;
    }

    @Transactional
    @Override
    public boolean addHolidayToHolidayType(LeavePolicy newHoliday) {
        Session session = null;
        try {
            session = getSession();
            session.save(newHoliday);
            return true;
        } catch (Exception e) {
            s_logger.error("Error while adding a holiday type: ", e);
            throw new RuntimeException("Error in adding information");
        } finally {
            closeSession(session);
        }

    }

    @Transactional
    @Override
    public boolean updateHolidayType(LeavePolicy updatedHoliday) {
        Session session = null;
        try {
            session = getSession();
            session.update(updatedHoliday);
            session.flush();
        } catch (Exception e) {
            s_logger.error("Error while updating a holiday type: ", e);
            throw new RuntimeException("Error in updating team information");

        } finally {
            closeSession(session);
        }

        return true;
    }

    @Transactional
    @Override
    public boolean deleteHolidayType(LeavePolicy holidayTypeData) {
        Session session = null;
        try {
            session = getSession();
            session.delete(holidayTypeData);

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
    public boolean deleteHolidayCalendarEvent(YearlyCalendar calendarEvent) {
        Session session = null;
        try {
            session = getSession();
            session.delete(calendarEvent);

        } catch (Exception e) {
            s_logger.error("Error while deleting the holiday: ", e);
            throw new RuntimeException("Error in deleting holiday");
        } finally {
            closeSession(session);
        }

        return true;
    }

    @Override
    @Transactional
    public List<YearlyCalendar> getEventsFromCalendar(EventType eventType, Date startDate, Date endDate) {
        List<YearlyCalendar> calendarEventsList = new ArrayList<YearlyCalendar>();
        Session session = null;
        try {
            session = getSession();
            Criteria cr = session.createCriteria(YearlyCalendar.class);

            cr.add(Restrictions.eq("m_eventType", eventType));

            if (startDate != null && endDate != null) {
                cr.add(Restrictions.between("m_startDate", startDate, endDate));
            }

            calendarEventsList = cr.list();

            return calendarEventsList;
        } catch (Exception e) {
            s_logger.error("error in getting holidays from calendar", e);
            return null;
        } finally {
            closeSession(session);
        }
    }

}
