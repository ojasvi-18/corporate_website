package com.zillious.corporate_website.portal.dao;

import org.springframework.stereotype.Component;

import com.zillious.corporate_website.portal.ui.model.User;

@Component
public interface LoginDao extends Dao {

    void loginUser(User user);
}
