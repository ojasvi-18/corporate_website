package com.zillious.corporate_website.portal.ui.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;

/**
 * @author nishant.gupta
 *
 */
@Entity
@Table(name = "attendance_records")
public class AttendanceRecord implements DBObject {

    private static final long serialVersionUID = 2604239520600892168L;

    @Embeddable
    public static class AttendancePK implements DBObject {
        private static final long serialVersionUID = -279197219650781090L;

        /**
         * The user id in the device.
         */
        @ManyToOne(fetch = FetchType.LAZY)
        @Cascade(org.hibernate.annotations.CascadeType.PERSIST)
        @JoinColumn(name = "dev_user_id", referencedColumnName="device_id", insertable = false, updatable = false)
        private User              m_employee;

        @Column(name = "gen_ts", nullable = false, updatable = false)
        private Date              m_genTs;

        @Column(name = "device_id", nullable = false, updatable = false)
        private String            m_deviceId;

        /**
         * @return the employee
         */
        public User getEmployee() {
            return m_employee;
        }

        /**
         * @param employee the employee to set
         */
        public void setEmployee(User employee) {
            m_employee = employee;
        }

        /**
         * @return the genTs
         */
        public Date getGenTs() {
            return m_genTs;
        }

        /**
         * @param genTs the genTs to set
         */
        public void setGenTs(Date genTs) {
            m_genTs = genTs;
        }

        /**
         * @return the deviceId
         */
        public String getDeviceId() {
            return m_deviceId;
        }

        /**
         * @param deviceId the deviceId to set
         */
        public void setDeviceId(String deviceId) {
            m_deviceId = deviceId;
        }
    }

    @EmbeddedId
    private AttendancePK m_key;

    @Column(name = "ipaddress", nullable = false)
    private String       m_ipAddress;

    /**
     * @return the key
     */
    public AttendancePK getKey() {
        return m_key;
    }

    /**
     * @param key the key to set
     */
    public void setKey(AttendancePK key) {
        m_key = key;
    }

    /**
     * @return the ipAddress
     */
    public String getIpAddress() {
        return m_ipAddress;
    }

    /**
     * @param ipAddress the ipAddress to set
     */
    public void setIpAddress(String ipAddress) {
        m_ipAddress = ipAddress;
    }
}
