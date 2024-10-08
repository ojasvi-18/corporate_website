package com.zillious.corporate_website.portal.service;

import org.springframework.stereotype.Component;

@Component
public interface ProfileService {

    // public JsonObject getProfilebyID(int ID);
    //
    // public JsonObject updateProfile(String test);

    public boolean isAuthorizedUser(int userId);

}
