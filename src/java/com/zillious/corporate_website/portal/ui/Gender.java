package com.zillious.corporate_website.portal.ui;

import com.google.gson.JsonObject;

public enum Gender {

    Male("M"),

    Female("F"), ;

    private String m_code;

    Gender(String code) {
        m_code = code;
    }

    public String getCode() {
        return m_code;
    }

    public static Gender getByCode(String serial) {
        if (serial == null) {
            return null;
        }
        serial = serial.trim();

        for (Gender gender : Gender.values()) {
            if (gender.getCode().equals(serial)) {
                return gender;
            }
        }

        return null;
    }

    public JsonObject getJson() {
        JsonObject obj = new JsonObject();
        obj.addProperty("code", getCode());
        obj.addProperty("name", name());
        return obj;
    }

}
