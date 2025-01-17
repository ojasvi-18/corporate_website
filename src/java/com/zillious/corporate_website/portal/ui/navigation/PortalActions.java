package com.zillious.corporate_website.portal.ui.navigation;

import java.util.ArrayList;
import java.util.List;

import com.zillious.corporate_website.portal.ui.UserRoles;
import com.zillious.corporate_website.ui.navigation.WebsiteNavigation;
import com.zillious.corporate_website.ui.security.ZilliousSecurityWrapperRequest;

/**
 * @author satyam.mittal
 *
 */
public enum PortalActions {
    HOME(RequestMappings.HOME_BASE, UserRoles.getAllRoles()),

    LOGIN(RequestMappings.LOGIN, UserRoles.getAllRoles()),

    LOGIN_SAML(RequestMappings.LOGIN_SAML, UserRoles.getAllRoles()),

    LOGOUT_SAML(RequestMappings.LOGOUT_SAML, UserRoles.getAllRoles()),

    LOGOUT(RequestMappings.LOGOUT, UserRoles.getAllRoles()),

    RANDOM(RequestMappings.RANDOM, UserRoles.getAllRoles()),

    /**
     * PROFILE PORTAL ACTIONS
     */
    PROFILE(RequestMappings.PROFILE_BASE), PROFILE_EDIT(RequestMappings.PROFILE_EDIT), PROFILE_VIEW(
            RequestMappings.PROFILE_VIEW), PROFILE_UPDATE(RequestMappings.PROFILE_UPDATE), PROFILE_PICTUREADD(
            RequestMappings.PROFILE_PICTUREADD), PROFILE_PICTUREDELETE(RequestMappings.PROFILE_PICTUREDELETE),

    SYNCUSERPROFILES(RequestMappings.USERPROFILESYNC), SENDBIRTHDAYWISHES(RequestMappings.WISHBIRTHDAY), USERNAMESEARCH(
            RequestMappings.USERNAMESEARCH), TEAMMEMBERNAMESEARCH(RequestMappings.TEAMMEMBERNAMESEARCH),

    /**
     * TEAMS PORTAL ACTIONS
     */
    TEAMS(RequestMappings.TEAM_BASE, UserRoles.getLoggedInRoles()),

    TEAMS_ADD(RequestMappings.TEAMS_ADD, UserRoles.getAdminRoles()),

    TEAM_UPDATE(RequestMappings.TEAM_UPDATE, UserRoles.getAdminRoles()),

    TEAM_DELETE(RequestMappings.TEAM_DELETE, UserRoles.getAdminRoles()),

    TEAMNAMESEARCH(RequestMappings.SEARCHBYTEAMNAME, UserRoles.getAdminRoles()),

    GETTEAMMEMBERS(RequestMappings.GETTEAMMEMBERS, UserRoles.getLoggedInRoles()),

    UPDATEMEMBERS(RequestMappings.UPDATETEAMMEMBERS, UserRoles.getAdminRoles()),

    /**
     * EMPLOYEE PORTAL ACTIONS
     */
    EMPLOYEE(RequestMappings.EMPLOYEE_BASE, UserRoles.getLoggedInRoles()), EMPLOYEE_ADD(RequestMappings.EMPLOYEE_ADD,
            UserRoles.getAdminRoles()), EMPLOYEE_UPDATE(RequestMappings.EMPLOYEE_UPDATE, UserRoles.getAdminRoles()), EMPLOYEE_DELETE(
            RequestMappings.EMPLOYEE_DELETE, UserRoles.getAdminRoles()), EMPLOYEE_ATTENDANCE_LIST(RequestMappings.EMPLOYEE_ATTENDANCE_LIST, UserRoles.getAdminRoles()),

    /**
     * LEAVEPOLICY containing HOLIDAYCALENDAR for holiday calendar lists and
     * LEAVETYPE
     */

    LEAVEPOLICY(RequestMappings.LEAVESPOLICY_BASE, UserRoles.getLoggedInRoles()), LEAVEPOLICY_HOLIDAYCALENDAR(
            RequestMappings.LEAVEPOLICY_HOLIDAYCALENDAR, UserRoles.getLoggedInRoles()), LEAVEPOLICY_HOLIDAYCALENDAR_ADD(
            RequestMappings.LEAVEPOLICY_HOLIDAYCALENDAR_ADD, UserRoles.getAdminRoles()), LEAVEPOLICY_HOLIDAYCALENDAR_UPDATE(
            RequestMappings.LEAVEPOLICY_HOLIDAYCALENDAR_UPDATE, UserRoles.getAdminRoles()), LEAVEPOLICY_HOLIDAYCALENDAR_DELETE(
            RequestMappings.LEAVEPOLICY_HOLIDAYCALENDAR_DELETE, UserRoles.getAdminRoles()),

    LEAVESTYPE(RequestMappings.LEAVETYPE_BASE, UserRoles.getLoggedInRoles()), LEAVEPOLICY_LEAVESTYPE(
            RequestMappings.LEAVEPOLICY_LEAVETYPE, UserRoles.getLoggedInRoles()), LEAVEPOLICY_LEAVESTYPE_ADD(
            RequestMappings.LEAVEPOLICY_LEAVESTYPE_ADD, UserRoles.getAdminRoles()), LEAVEPOLICY_LEAVESTYPE_DELETE(
            RequestMappings.LEAVEPOLICY_LEAVESTYPE_DELETE, UserRoles.getAdminRoles()), LEAVEPOLICY_LEAVESTYPE_UPDATE(
            RequestMappings.LEAVEPOLICY_LEAVESTYPE_UPDATE, UserRoles.getAdminRoles()),

    /**
     * leaveRequest raised by user , Portal actions
     */
    LEAVEREQUESTS(RequestMappings.APPLYLEAVE_BASE), LEAVEREQUESTS_ADD(RequestMappings.APPLYLEAVE_ADD), LEAVEREQUESTS_DELETE(
            RequestMappings.APPLYLEAVE_DELETE), LEAVEREQUESTS_UPDATE(RequestMappings.APPLYLEAVE_UPDATE), LEAVEREQUESTS_UPDATESTATUS(
            RequestMappings.APPLYLEAVE_UPDATESTATUS), LEAVEREQUESTS_REMINDER(RequestMappings.APPLYLEAVE_REMINDER),

    ATTENDANCE(RequestMappings.ATTENDANCE_BASE), ATTENDANCECALENDAR(RequestMappings.ATTENDANCECALENDAR),

    LEAVEREPORTS(RequestMappings.LEAVEREPORT_BASE, UserRoles.getNonSuperUserLoggedInRoles()), LEAVEREPORTSCALENDAR(
            RequestMappings.LEAVEREPORTCALENDAR, UserRoles.getNonSuperUserLoggedInRoles()),

    BROADCASTYEARLYCALENDAR(RequestMappings.BROADCASTYEARLYCALENDAR, UserRoles.getAdminRoles()),

    CHECKADMIN(RequestMappings.CHECKADMIN),

    MANUALATTENDANCEENTRY(RequestMappings.MANUALATTENDANCEENTRY, new UserRoles[] { UserRoles.ADMINISTRATOR,
            UserRoles.DIRECTOR, UserRoles.HR })

    ;

    private String          m_displayName;
    /**
     * Default all urls should be https
     */
    private boolean         m_forceSSL = true;
    private List<UserRoles> m_allowedRoles;

    PortalActions(String displayName) {
        m_displayName = displayName;
        setAllowedRoles(UserRoles.getNonSuperUserLoggedInRoles());
    }

    private void setAllowedRoles(List<UserRoles> nonSuperUserLoggedInRoles) {
        m_allowedRoles = nonSuperUserLoggedInRoles;
    }

    PortalActions(String displayName, List<UserRoles> allowedRoles) {
        this(displayName);
        setAllowedRoles(allowedRoles);
    }

    PortalActions(String displayName, UserRoles[] allowedRoles) {
        this(displayName);
        setAllowedRoles(allowedRoles);
    }

    private void setAllowedRoles(UserRoles[] allowedRoles) {
        if (allowedRoles == null || allowedRoles.length == 0) {
            return;
        }

        List<UserRoles> roles = new ArrayList<UserRoles>();
        for (UserRoles role : allowedRoles) {
            roles.add(role);
        }

        setAllowedRoles(roles);

    }

    PortalActions(String displayName, boolean isForceSSL) {
        this(displayName);
        m_forceSSL = isForceSSL;
    }

    public String getActionURL(ZilliousSecurityWrapperRequest request) {

        StringBuilder b = new StringBuilder(128);
        b.append(isforceSSL() ? "https://" : "http://").append(request.getServerName())
                .append(WebsiteNavigation.getSprinServletPath());

        // if (requiresCsrfCheck()) {
        // b.append(getCsrfUrlToken(request));
        // }

        b.append(getDisplayName());

        return b.toString();

    }

    // function to return hopepage url without server name(localhost)
    // public String getHomePageURL() {
    //
    // StringBuilder b = new StringBuilder(128);
    // b.append(WebsiteNavigation.getSprinServletPath());
    // b.append(getDisplayName());
    // return b.toString();
    // }

    private boolean isforceSSL() {
        return m_forceSSL;
    }

    public String getDisplayName() {
        return m_displayName;
    }

    public List<UserRoles> getAllowedRoles() {
        return m_allowedRoles;
    }

    public static PortalActions getPortalActionByDisplayName(String actionURI) {

        if (actionURI == null || (actionURI = actionURI.trim()).isEmpty()) {
            return null;
        }

        for (PortalActions action : PortalActions.values()) {
            if (action.getDisplayName().equals(actionURI)) {
                return action;
            }
        }

        return null;

    }

    /**
     * This api checks if the requested action is available to the user with the
     * role user_role
     * 
     * @param user_role
     * @return
     */
    public boolean isAllowedForRole(UserRoles user_role) {
        if (user_role == null) {
            return false;
        }
        return m_allowedRoles.contains(user_role);
    }

}
