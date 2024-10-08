package com.zillious.corporate_website.portal.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zillious.corporate_website.portal.dao.EmployeeDao;
import com.zillious.corporate_website.portal.ui.model.Employee;

/**
 * This class implements the functions of EmployeeDao. SessionFactory Bean is
 * autowired from the servlet configuration. The annotation @EnableTransactionalManagement
 * manages all the functions who need to begin a transaction.
 * 
 * @author nishant.gupta
 */
@EnableTransactionManagement
@Repository
public class EmployeeDaoImpl implements EmployeeDao {
    private static Logger  s_logger = Logger.getLogger(EmployeeDaoImpl.class);
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
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.zillious.corporate_website.portal.dao.EmployeeDao#getAllEmployees()
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<Employee> getAllEmployees() {
        List<Employee> employees = new ArrayList<Employee>();
        Session session = null;

        try {
            session = getSession();
            Criteria cr = session.createCriteria(Employee.class);
            employees = cr.list();
        } catch (Exception e) {
            s_logger.error("stacktrace", e);
        } finally {
            closeSession(session);
        }

        return employees;
    }

    @Override
    public void addEmployee(Employee employee) {

        Session session = null;
        try {
            session = getSession();
            Employee temp = new Employee();
            temp.setName(employee.getName());
            temp.setAddress(employee.getAddress());
            temp.setEmail(employee.getEmail());
            temp.setCity(employee.getCity());
            temp.setCountry(employee.getCountry());
            temp.setState(employee.getState());
            temp.setMobile(employee.getMobile());
            temp.setEmployeeCode(employee.getEmployeeCode());

            session.persist(temp);

        } catch (Exception e) {
            s_logger.error("stacktrace", e);
        } finally {
            closeSession(session);
        }
    }

}
