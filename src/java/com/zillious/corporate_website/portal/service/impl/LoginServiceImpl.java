package com.zillious.corporate_website.portal.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



import com.zillious.corporate_website.portal.dao.LoginDao;
import com.zillious.corporate_website.portal.service.LoginService;
import com.zillious.corporate_website.portal.ui.model.User;

/**
 * Implements the functions of LoginService class. LoginDao bean has been
 * autowired from the servlet configuration file.
 * 
 * @author nishant.gupta
 *
 */

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    LoginDao dao;

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.zillious.corporate_website.ui.service.LoginService#login(com.zillious
     * .corporate_website.data.user.User)
     */
    @Override
    @Transactional
    public void login(User user) {
        dao.loginUser(user);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.zillious.corporate_website.ui.service.LoginService#logout()
     */
    @Override
    @Transactional
    public void logout() {
        // TODO Auto-generated method stub

    }

}
