package com.zillious.corporate_website.portal.service;

import org.springframework.stereotype.Component;

import com.zillious.corporate_website.portal.ui.model.User;


/**
 * Interface that contains the functions that will be called under LoginService.
 * 
 * @author nishant.gupta
 * 
 */
@Component
public interface LoginService {

    public void login(User user);

    public void logout();
}
