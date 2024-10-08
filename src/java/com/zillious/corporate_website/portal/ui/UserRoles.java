package com.zillious.corporate_website.portal.ui;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public enum UserRoles {

    EMPLOYEE('E', "Employee"),

    ADMINISTRATOR('A', "Administrator"),

    SUPER('S', "Super User"),

    DIRECTOR('D', "Director"),

    HR('H', "Human Resources"),

    NOT_LOGGED_IN('N', "Not Logged In"),

    ;

    private String m_serial;
    private String m_displayName;

    UserRoles(char serial, String displayName) {
        m_serial = "" + serial;
        m_displayName = displayName;
    }

    public String serialize() {
        return m_serial;
    }

    public String getDisplayName() {
        return m_displayName;
    }

    public static UserRoles deserialize(String serial) {
        if (serial == null || serial.length() != 1) {
            return null;
        }

        for (UserRoles r : UserRoles.values()) {
            if (r.serialize().equals(serial)) {
                return r;
            }
        }

        return null;
    }

    public static String getUserRolesJson() {

        JsonArray roleJson = new JsonArray();
        List<UserRoles> loggedInRoles = UserRoles.getLoggedInRoles();
        loggedInRoles.remove(UserRoles.SUPER);
        for (UserRoles uRole : loggedInRoles) {
            JsonObject jsonObj = new JsonObject();
            jsonObj.addProperty("code", uRole.m_serial);
            jsonObj.addProperty("name", uRole.name());
            roleJson.add(jsonObj);
        }

        String usersRoleJsonString = roleJson.toString();
        return usersRoleJsonString;
    }

    public JsonObject getJson() {
        JsonObject jsonObj = new JsonObject();
        jsonObj.addProperty("code", m_serial);
        jsonObj.addProperty("name", name());

        return jsonObj;
    }

    public static void main(String[] args) {
        getUserRolesJson();
    }

    public static List<UserRoles> getAllRoles() {
        List<UserRoles> toSendBack = new ArrayList<UserRoles>();
        for (UserRoles role : UserRoles.values()) {
            toSendBack.add(role);
        }

        return toSendBack;
    }

    public static List<UserRoles> getLoggedInRoles() {
        List<UserRoles> allRoles = getAllRoles();
        allRoles.remove(UserRoles.NOT_LOGGED_IN);

        return allRoles;
    }

    public static List<UserRoles> getNonSuperUserLoggedInRoles() {
        List<UserRoles> allRoles = getLoggedInRoles();
        allRoles.remove(UserRoles.SUPER);

        return allRoles;
    }

    public static List<UserRoles> getNotLoggedInRoles() {
        List<UserRoles> notLoggedInRoles = new ArrayList<UserRoles>();
        notLoggedInRoles.add(UserRoles.NOT_LOGGED_IN);

        return notLoggedInRoles;
    }

    public boolean isLoggedInRole() {
        return !isNotLoggedInRole();
    }

    public boolean isNotLoggedInRole() {
        return this == NOT_LOGGED_IN;
    }

    public static List<UserRoles> getAdminRoles() {
        List<UserRoles> allRoles = getAllRoles();
        allRoles.remove(UserRoles.NOT_LOGGED_IN);
        allRoles.remove(UserRoles.EMPLOYEE);

        return allRoles;
    }

    public boolean isAdministrator() {
        return this == ADMINISTRATOR;
    }

    public boolean isEmployee() {
        return this == EMPLOYEE;
    }

    public boolean isAdminRole() {
        return getAdminRoles().contains(this);
    }

    public boolean isDirector() {
        return this == DIRECTOR;
    }

    public boolean isSuper() {
        return this == SUPER;
    }
}
