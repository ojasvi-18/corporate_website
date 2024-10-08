package com.zillious.corporate_website.portal.service.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zillious.corporate_website.portal.dao.ProfileDao;
import com.zillious.corporate_website.portal.service.ProfileService;

@EnableTransactionManagement
@Service
public class ProfileServiceImpl implements ProfileService {
    public static Logger s_logger = Logger.getLogger(ProfileServiceImpl.class);

    @Autowired
    private ProfileDao   m_profileDao;

    // this method is used to check the authorization of user before allowing to
    // edit/delete profile here userId is the id of user searched or logged in
    @Override
    public boolean isAuthorizedUser(int userId) {
        // change this id to the user loged in id by gettiing therough
        // authentication && change "auth.getName()== username of loggedin user"
        int id = 0;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        s_logger.debug("logedin  user: " + auth.getName());
        if ((auth.getName() == "anonymousUser") || (userId == id) || (auth.getName() == "admin")) {
            return true;
        }
        return false;
    }

}
