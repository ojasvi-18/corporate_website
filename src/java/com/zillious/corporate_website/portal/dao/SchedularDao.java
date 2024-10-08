package com.zillious.corporate_website.portal.dao;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;

import com.zillious.corporate_website.portal.ui.model.SchedularEmail;

/**
 * @author ojasvi.bhardwaj
 *
 */
@Component
public interface SchedularDao extends Dao {


    /**
     * this api adds the entries of schedular running in  the database
     * @param schedularEmail
     */
    void addSchedularEmail(SchedularEmail schedularEmail);


    /**
     * this api returns the last email sent
     * @return
     */
    SchedularEmail getTheLastEmail();
   
}
