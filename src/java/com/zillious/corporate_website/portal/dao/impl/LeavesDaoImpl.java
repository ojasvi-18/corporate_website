package com.zillious.corporate_website.portal.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
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

import com.zillious.corporate_website.portal.dao.LeavesDao;
import com.zillious.corporate_website.portal.ui.model.LeaveRequest;
import com.zillious.corporate_website.portal.ui.model.User;
import com.zillious.corporate_website.utils.DateUtility;

/**
 * @author nishant.gupta
 *
 */
@EnableTransactionManagement
@Repository
public class LeavesDaoImpl implements LeavesDao {

    @Autowired
    private SessionFactory sessionFactory;

    private static Logger  s_logger = Logger.getLogger(LeavesDaoImpl.class);

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

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.zillious.corporate_website.portal.dao.LeavesDao#getLeaveRequest(int)
     */
    @Override
    public LeaveRequest getLeaveRequest(int leaveId) {
        Session session = null;

        try {
            session = getSession();

            Criteria cr = session.createCriteria(LeaveRequest.class).add(Restrictions.eq("m_id", leaveId));

            Object uniqueResult = cr.uniqueResult();
            if (uniqueResult != null && uniqueResult instanceof LeaveRequest) {
                return (LeaveRequest) uniqueResult;
            }

        } catch (Exception e) {
            s_logger.error("Error while getting Leave Request from db for id: " + leaveId, e);
        } finally {
            closeSession(session);
        }
        return null;
    }

    @Transactional
    @Override
    public boolean update(LeaveRequest leaveRequest) {
        Session session = null;
        try {
            session = getSession();
            session.update(leaveRequest);
            return true;
        } catch (Exception e) {
            throw e;
        } finally {
            closeSession(session);
        }
    }

    @Override
    public List<LeaveRequest> getLeaveRequests(String year, Set<User> users) {
        Session session = null;

        try {
            session = getSession();

            Criteria cr = session.createCriteria(LeaveRequest.class);
            cr.add(Restrictions.le("m_startDate", DateUtility.getLastDateOfYear(year)));
            cr.add(Restrictions.ge("m_startDate", DateUtility.getFirstDateOfYear(year)));

            if (users != null) {
                cr.add(Restrictions.in("m_requestor", users));
            }

            List results = cr.list();
            if (results != null) {
                return (List<LeaveRequest>) results;
            }

        } catch (Exception e) {
            s_logger.error("Error while getting Leave Request from db for year: " + year, e);
        } finally {
            closeSession(session);
        }
        return null;
    }

    @Override
    public List<LeaveRequest> getLeaveRequestDataForUser(Set<User> users, Date startDate, Date endDate) {
        Session session = null;

        try {
            session = getSession();

            Criteria cr = session.createCriteria(LeaveRequest.class);
            if (users != null) {
                cr.add(Restrictions.in("m_requestor", users));
            }

            cr.add(Restrictions.between("m_startDate", startDate, endDate));
            List list = cr.list();
            if (list == null) {
                return null;
            }

            return (List<LeaveRequest>) list;
        } catch (Exception e) {

            StringBuilder log = new StringBuilder("Error while getting Leave Requests from db for users: ");
            for (User user : users) {
                log.append(user.getUserId()).append(", ");
            }
            log.deleteCharAt(log.length() - 1);

            s_logger.error(log.toString(), e);
        } finally {
            closeSession(session);
        }
        return null;
    }

    @Override
    public List<LeaveRequest> getLeaveRequestDataForUser(User user, Date startDate, Date endDate) {
        if (user == null) {
            return null;
        }

        Set<User> users = new HashSet<User>();
        users.add(user);

        return getLeaveRequestDataForUser(users, startDate, endDate);
    }

    /* (non-Javadoc)
     * @see com.zillious.corporate_website.portal.dao.LeavesDao#getWeekLeaveRequestsForUser(com.zillious.corporate_website.portal.ui.model.User, java.util.Date, java.util.Date)
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<LeaveRequest> getWeekLeaveRequestsForUser(User teamMember, Date currentDate ,Date lastDateOfTheWeek ) {
        List<LeaveRequest> leavesForWeek = new ArrayList<LeaveRequest>();
        Session session = null;
        try {

            session = getSession();
            if(session != null){
                Criteria cr = session.createCriteria(LeaveRequest.class);
                Criterion startDateLessThanCurrentDate = Restrictions.le("m_startDate" , currentDate);  
                Criterion startDateGreaterThanCurrentDate = Restrictions.le("m_startDate", lastDateOfTheWeek);
                LogicalExpression exp1 = Restrictions.or(startDateLessThanCurrentDate, startDateGreaterThanCurrentDate);

                // end date of the leave requests is exclusive 
                Criterion endDate = Restrictions.gt("m_endDate", currentDate);
                LogicalExpression exp = Restrictions.and(exp1, endDate);

                cr.add(exp);
                leavesForWeek = cr.list();
            }
            return leavesForWeek;

        } catch (Exception e) {
            s_logger.error("stacktrace", e);
            return null;
        } finally {
            closeSession(session);
        }

    }
}
