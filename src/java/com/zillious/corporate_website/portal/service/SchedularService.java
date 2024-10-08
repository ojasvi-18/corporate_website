package com.zillious.corporate_website.portal.service;

import org.springframework.stereotype.Component;

/**
 * @author ojasvi.bhardwaj
 *
 */
@Component
public interface SchedularService {

    void addScheduledEmailEntry(Character scheduledEmailStatus);

    /**
     * passes character to assSchedulerEmailToDb depending upon the boolean status
     * 
     * @param status
     */
    void addEmail(boolean status);
    
    /**
     * returns true if the email is to be sent (no successful scheduler entry today )
     * 
     * @return
     */
    boolean isSendEmail();



}
