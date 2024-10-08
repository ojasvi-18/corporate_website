package com.zillious.corporate_website.portal.ui.controller;

import java.util.Date;
import java.util.List;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.zillious.corporate_website.portal.dao.SchedularDao;
import com.zillious.corporate_website.portal.entity.EmailMessage;
import com.zillious.corporate_website.portal.service.SchedularService;
import com.zillious.corporate_website.portal.service.UserService;
import com.zillious.corporate_website.portal.service.impl.LeavesServiceImpl;
import com.zillious.corporate_website.portal.ui.model.SchedularEmail;
import com.zillious.corporate_website.utils.DateUtility;
import com.zillious.corporate_website.utils.EmailSender;

/**
 * The zillious Scheduler is a Spring Component, which has methods that can be
 * scheduled to run a job in intervals .
 * 
 * @author ojasvi.bhardwaj
 *
 */
@Component
public class ZilliousScheduler {

    @Autowired
    private UserService      userService;

    @Autowired
    private SchedularService schedularService;

    @Autowired
    private SchedularDao     schedulerDao;

    /**
     * 
     * api to send summary to all users according to the scheduled cron job cron
     * job is scheduled in xml .
     * 
     *  Cron expression consists of 6 fields : 
     *  (second) (minutes) (hour) (day-of-the-month) (month) (day-of-the-week) (year)
     *  "*" specifies "all" and "?" specifies "any i.e. neglect the field value" .
     *   The field year is not mandatory .
     *   
     * @return void
     */

    @Scheduled(cron = "${cron.expression}")
    public void scheduleSummaryEmailsToUsers() {
        boolean isSendEmail = schedularService.isSendEmail();
        if (isSendEmail) {
            List<EmailMessage> emailMessages = userService.getSchedularEmailSummaryForUsers();
            if (emailMessages != null && !emailMessages.isEmpty()) {
                try {
                    // send email to all users
                    for (EmailMessage emailMessage : emailMessages) {
                        EmailSender.sendEmail(emailMessage);
                    }

                    // add entry for sending mails successfully to database
                    schedularService.addEmail(true);
                } catch (Exception e) {

                    // add entry for not being able to send email successfully
                    // to database
                    schedularService.addEmail(false);
                }
            }
        }
    }
}