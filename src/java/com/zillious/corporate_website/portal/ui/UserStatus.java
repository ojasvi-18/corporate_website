package com.zillious.corporate_website.portal.ui;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public enum UserStatus {

    /**
     * Can login to the system
     */
    Enabled("E", "Enabled"),

    /**
     * Cannot login to the system
     */
    Disabled("D", "Disabled"),

    // /**
    // * Working from on-site location of either client or company
    // */
    // OnSite("O", "OnSite"),
    //
    // /**
    // * Suspended from the organization
    // */
    // Suspended("S", "Suspended"),

    ;

    private String m_code;
    private String m_displayStatusName;

    UserStatus(String status, String userStatusName) {
        m_code = status;
        m_displayStatusName = userStatusName;
    }

    public static UserStatus getStatusByCode(String status) {
        if (status == null) {
            return null;
        }
        status = status.trim();
        for (UserStatus userstatus : UserStatus.values()) {
            if (userstatus.getCode().equals(status)) {
                return userstatus;
            }
        }
        return null;
    }

    public static String getStatusJson() {

        JsonArray statusJson = new JsonArray();
        for (UserStatus status : UserStatus.values()) {
            JsonObject jsonObj = status.getJson();
            statusJson.add(jsonObj);
        }

        String jsonString = statusJson.toString();
        // s_logger.debug("status json: " + jsonString);
        return jsonString;

    }

    public String getDisplayName() {
        return m_displayStatusName;
    }

    public String getCode() {
        return m_code;
    }

    public String serialize() {
        return m_code;
    }

    public static UserStatus deserialize(String status) {
        if (status == null || status.length() != 1) {
            return null;
        }

        for (UserStatus s : UserStatus.values()) {
            if (s.serialize().equals(status)) {
                return s;
            }
        }

        return null;
    }

    public JsonObject getJson() {
        JsonObject jsonObj = new JsonObject();
        jsonObj.addProperty("id", getCode());
        jsonObj.addProperty("status", name());

        return jsonObj;
    }

}
