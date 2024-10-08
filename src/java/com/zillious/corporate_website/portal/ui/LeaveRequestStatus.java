package com.zillious.corporate_website.portal.ui;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * @author nishant.gupta
 *
 */
public enum LeaveRequestStatus {
    PENDING("P", "#ffeb3b"), APPROVED("A", "#43a047"), DECLINED("D", "#d32f2f"), ;

    private String m_code;
    private String m_colorCode;

    LeaveRequestStatus(String code, String colorCode) {
        m_code = code;
        m_colorCode = colorCode;
    }

    /**
     * @return the code
     */
    public String getCode() {
        return m_code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(String code) {
        m_code = code;
    }

    public static LeaveRequestStatus getStatusByCode(String status) {
        if (status == null) {
            return null;
        }

        for (LeaveRequestStatus lrs : LeaveRequestStatus.values()) {
            if (status.equals(lrs.getCode())) {
                return lrs;
            }
        }
        return null;
    }

    public static String getStatusJson() {

        JsonArray statusJson = new JsonArray();
        for (LeaveRequestStatus status : LeaveRequestStatus.values()) {
            JsonObject jsonObj = status.getJson();
            statusJson.add(jsonObj);
        }

        String jsonString = statusJson.toString();
        return jsonString;

    }

    private JsonObject getJson() {
        JsonObject object = new JsonObject();
        fillStatus(object);
        object.addProperty("color", m_colorCode);
        return object;
    }

    public void fillStatus(JsonObject object) {
        object.addProperty("status", name());
        object.addProperty("backgroundColor", getColorCode());
    }

    public String getColorCode() {
        return m_colorCode;
    }

    public void setColorCode(String colorCode) {
        m_colorCode = colorCode;
    }

}
