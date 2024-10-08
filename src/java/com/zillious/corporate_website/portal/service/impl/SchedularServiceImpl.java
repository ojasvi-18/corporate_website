package com.zillious.corporate_website.portal.service.impl;

import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import com.zillious.corporate_website.portal.dao.SchedularDao;
import com.zillious.corporate_website.portal.service.SchedularService;
import com.zillious.corporate_website.portal.ui.model.SchedularEmail;
import com.zillious.corporate_website.utils.DateUtility;

/**
 *
 * 
 * @author ojasvi.bhardwaj
 *
 */

@EnableTransactionManagement
@Service
public class SchedularServiceImpl implements SchedularService {

    @Autowired
    private SchedularDao  schedularDao;

    private static Logger s_logger = Logger.getLogger(SchedularServiceImpl.class);

    @Override
    @Transactional
    public void addScheduledEmailEntry(Character emailStatus) {

        try {
            SchedularEmail se = new SchedularEmail();
            se.setDate(DateUtility.getCurrentDate());
            se.setEmailStatus(emailStatus);
            schedularDao.addSchedularEmail(se);
        } catch (Exception e) {
            s_logger.error("Failed to save data to database", e);

        }
    }

    @Transactional
    @Override
    public void addEmail(boolean status) {
        if (status == true)
            addScheduledEmailEntry('Y');
        else
            addScheduledEmailEntry('N');

    }

    @Override
    /*
     * This method checks if the email is to be sent today
     */
    public boolean isSendEmail() {
        boolean isSendEmail = false;
        SchedularEmail email = schedularDao.getTheLastEmail();
        if (email != null) {
            String lastEmailSentDate = DateUtility.getDateInDDMMDash(email.getDate());
            String currentDate = DateUtility.getDateInDDMMDash(DateUtility.getCurrentDate());
            if ((lastEmailSentDate.equals(currentDate) && !email.isEmailSent(email.getStatus()))
                    || (!lastEmailSentDate.equals(currentDate))) {
                isSendEmail = true;
            }
        } else {
            isSendEmail = true;
        }

        return isSendEmail;
    }

}
