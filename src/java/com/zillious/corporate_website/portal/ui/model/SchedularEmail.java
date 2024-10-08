package com.zillious.corporate_website.portal.ui.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.zillious.corporate_website.utils.DateUtility;

/**
 * @author ojasvi.bhardwaj
 *
 */

@Entity
@Table(name = "schedular_email")
public class SchedularEmail {

    @Column(name = "id", nullable = false)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int       m_id;

    @Column(name = "email_status", nullable = false)
    private Character m_emailStatus;

    // @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date", nullable = false)
    private Date      m_date;

    public boolean isEmailSent(Character c) {
        if (c != null) {
            if (c == 'Y') {
                return true;
            } else {
                return false;
            }
        }
        return false;

    }

    public Character getStatus() {
        return m_emailStatus;
    }

    public void setEmailStatus(Character emailStatus) {
        this.m_emailStatus = emailStatus;
    }

    public Date getDate() {
        return m_date;
    }

    public void setDate(Date date) {
        this.m_date = date;
    }

}
