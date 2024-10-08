package com.zillious.corporate_website.portal.service;

import org.springframework.stereotype.Component;

import com.zillious.corporate_website.portal.ui.model.HomePageModel;
import com.zillious.corporate_website.portal.ui.model.User;
import com.zillious.corporate_website.ui.security.ZilliousSecurityWrapperRequest;

/**
 * @author Anil
 */
@Component
public interface PortalService {

    /**
     * @param zilliousRequest, getHomePageModelJsonURLs() used in portal
     *            navigation : it returns all the json urls required in
     *            application
     * @return
     */
    void addURLsToHomePageModel(ZilliousSecurityWrapperRequest zilliousRequest, HomePageModel homeModel);

    void addJsonArraysToHomePageModel(HomePageModel homeModel);

	void addUserNameDesignation(HomePageModel homeModel, User user);

}
