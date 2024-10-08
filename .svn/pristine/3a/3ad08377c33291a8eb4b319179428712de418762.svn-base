package com.zillious.corporate_website.portal.ui.model;

import java.util.ArrayList;
import java.util.List;

import com.zillious.corporate_website.portal.ui.UserRoles;

/**
 * @author satyam.mittal
 *
 */
public enum SidePanelItems {
    TEAMS("Teams", "#!teams", "glyphicon glyphicon-globe", UserRoles.getLoggedInRoles()),

    EMPLOYEES("Employees", "#!employees", "glyphicon glyphicon-user", UserRoles.getLoggedInRoles()),

    LEAVESTATUS("Leaves Status", "#!leavestatus", "glyphicon glyphicon-calendar", UserRoles
            .getNonSuperUserLoggedInRoles()),

    // EMPLOYEELEAVESTATUS("Other's Leaves", "#!employeeleaves",
    // "glyphicon glyphicon-calendar", UserRoles.getAdminRoles()),

    TEAMLEAVESTATUS("Team Leaves", "#!teamleaves", "glyphicon glyphicon-calendar", UserRoles
            .getNonSuperUserLoggedInRoles()),

    LEAVEPOLICY("Leave Policy", "#!leavepolicypage", "glyphicon glyphicon-file", UserRoles
            .getNonSuperUserLoggedInRoles()),

    TRACKATTENDANCE("Attendance", "#!attrckr", "glyphicon glyphicon-stats", UserRoles.getNonSuperUserLoggedInRoles()),

    MANUALENTRY("Time Punch", "#!timepunch", "glyphicon glyphicon-stats", new UserRoles[] {
            UserRoles.ADMINISTRATOR, UserRoles.DIRECTOR, UserRoles.HR}), ;

    private String dispName;

    public String getDispName() {
        return dispName;
    }

    public void setDispName(String dispName) {
        this.dispName = dispName;
    }

    private String          routingUrl;
    private String          glyphiconName;
    private List<UserRoles> m_allowedRoles;

    SidePanelItems(String str, String Url, String glyphicon, List<UserRoles> allowedRoles) {
        dispName = str;
        routingUrl = Url;
        glyphiconName = glyphicon;
        setallowedRoles(allowedRoles);
    }

    SidePanelItems(String str, String Url, String glyphicon, UserRoles[] allowedRoles) {
        dispName = str;
        routingUrl = Url;
        glyphiconName = glyphicon;
        setallowedRoles(allowedRoles);
    }

    private void setallowedRoles(UserRoles[] allowedRoles) {
        if (allowedRoles == null || allowedRoles.length == 0) {
            return;
        }

        List<UserRoles> roles = new ArrayList<UserRoles>();
        for (UserRoles role : allowedRoles) {
            roles.add(role);
        }

        setallowedRoles(roles);

    }

    public String getRoutingUrl() {
        return routingUrl;
    }

    public String getGlyphiconName() {
        return glyphiconName;
    }

    public static List<SidePanelItems> getSidePanelItemList(User loggedInUser) {

        List<SidePanelItems> list = new ArrayList<SidePanelItems>();

        for (SidePanelItems panel : SidePanelItems.values()) {
            if (panel.isAllowedForRole(loggedInUser.getUserRole())) {
                list.add(panel);
            }
        }

        return list;
    }

    private boolean isAllowedForRole(UserRoles userRole) {
        return m_allowedRoles.contains(userRole);
    }

    public static void main(String[] arg) {
    }

    public List<UserRoles> getallowedRoles() {
        return m_allowedRoles;
    }

    public void setallowedRoles(List<UserRoles> m_allowedRoles) {
        this.m_allowedRoles = m_allowedRoles;
    }
}
