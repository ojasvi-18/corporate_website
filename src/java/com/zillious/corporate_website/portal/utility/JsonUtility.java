package com.zillious.corporate_website.portal.utility;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.zillious.corporate_website.portal.ui.model.User;
import com.zillious.corporate_website.portal.ui.ResponseStatus;

/**
 * @author nishant.gupta
 *
 */
public class JsonUtility {
    

    public static void createResponseJson(JsonObject finalJSON, boolean isSuccess, String errorMsg) {
        finalJSON.addProperty("status", isSuccess ? ResponseStatus.SUCCESS.name() : ResponseStatus.ERROR.name());
        if (!isSuccess) {
            finalJSON.addProperty("errorMsg", errorMsg);
        }
    }

    public static void addIsAdminProperty(JsonObject finalObj, User loggedInUser) {
        if (finalObj == null || loggedInUser == null || loggedInUser.getUserRole() == null) {
            finalObj.addProperty("isAdmin", false);
        } else {
            finalObj.addProperty("isAdmin", loggedInUser.getUserRole().isAdminRole());
        }

    }

    public static void addLogout(JsonObject responseObj) {
        responseObj.addProperty("isLogout", true);
    }

    /**
     * @param requestJson format: { "user_id" : 1 }
     * @return
     */
    public static int getUserIdFromJson(String requestJson) {
        JsonParser parser = new JsonParser();
        JsonObject requestElement = (JsonObject) parser.parse(requestJson);
        return getUserIdFromJson(requestElement);
    }

    public static int getUserIdFromJson(JsonObject requestElement) {
        int requestorId = requestElement.get("user_id").getAsInt();
        return requestorId;
    }
}
