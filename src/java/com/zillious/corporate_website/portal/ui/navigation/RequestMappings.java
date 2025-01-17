package com.zillious.corporate_website.portal.ui.navigation;

/**
 * REQUEST MAPPING FOR ALL ACTIONS PRESENT ON BACK END
 * 
 * @author satyam.mittal
 *
 */
public interface RequestMappings {

    /**
     * COMMAN ACTIONS FOR PORTAL
     */
    String DIRECT_MAPPING                     = "";

    String LOGIN                              = "/login";

    String LOGOUT                             = "/logout";

    String DELETE                             = "/delete";

    String UPDATE                             = "/update";

    String EDIT                               = "/edit";

    String VIEW                               = "/view";

    String UPDATESTATUS                       = "/updateStatus";

    String ATTENDANCELIST                       = "/attendancelist";
    String ADD                                = "/add";

    String SEARCHBYNAME                       = "/searchbyname";
    String SEARCHTEAMMEMBERBYNAME             = "/searchmemberbyname";

    String HOME_BASE                          = "/home";

    String EMPLOYEE_BASE                      = "/employees";

    String SEND_REMINDER                      = "/sendReminder";

    String LOGIN_SAML                         = "/login_saml";

    String LOGOUT_SAML                        = "/saml/logout?local=true";

    /**
     * actions related to apply leaves by user
     */
    String APPLYLEAVE_BASE                    = "/applyleave";
    String APPLYLEAVE_ADD                     = APPLYLEAVE_BASE + ADD;
    String APPLYLEAVE_DELETE                  = APPLYLEAVE_BASE + DELETE;
    String APPLYLEAVE_UPDATE                  = APPLYLEAVE_BASE + UPDATE;
    String APPLYLEAVE_UPDATESTATUS            = APPLYLEAVE_BASE + UPDATESTATUS;
    String USERNAMESEARCH                     = EMPLOYEE_BASE + SEARCHBYNAME;
    String TEAMMEMBERNAMESEARCH               = EMPLOYEE_BASE + SEARCHTEAMMEMBERBYNAME;
    String APPLYLEAVE_REMINDER                = APPLYLEAVE_BASE + SEND_REMINDER;

    /**
     * url actions for leave policy and leave type with holiday calendar
     */

    String LEAVESPOLICY_BASE                  = "/leavepolicy";

    String HOLIDAYCALENDAR                    = "/holidaycalendar";

    /**
     * REQUIRED IN PORTAL ACTIONS FOR MAPPING HOLIDAY CALENDAR PRESENT IN LEAVE
     * POLICY CONTROLLER
     */
    String LEAVEPOLICY_HOLIDAYCALENDAR        = LEAVESPOLICY_BASE + HOLIDAYCALENDAR;
    String LEAVEPOLICY_HOLIDAYCALENDAR_ADD    = LEAVEPOLICY_HOLIDAYCALENDAR + ADD;
    String LEAVEPOLICY_HOLIDAYCALENDAR_DELETE = LEAVEPOLICY_HOLIDAYCALENDAR + DELETE;
    String LEAVEPOLICY_HOLIDAYCALENDAR_UPDATE = LEAVEPOLICY_HOLIDAYCALENDAR + UPDATE;

    /**
     * ACTIONS REQUIRD IN LEAVEPOLICY CONTROLLER FOR HOLIDAY CALENDAR
     */
    String HOLIDAYCALENDAR_ADD                = HOLIDAYCALENDAR + ADD;
    String HOLIDAYCALENDAR_UPDATE             = HOLIDAYCALENDAR + UPDATE;
    String HOLIDAYCALENDAR_DELETE             = HOLIDAYCALENDAR + DELETE;

    /**
     * REQUIRED IN LEAVE POLICY CONTROLLER FOR MAPPING LEAVES TYPE
     */
    String LEAVETYPE_BASE                     = "/leavetype";
    String LEAVETYPE_ADD                      = LEAVETYPE_BASE + ADD;
    String LEAVETYPE_UPDATE                   = LEAVETYPE_BASE + UPDATE;
    String LEAVETYPE_DELETE                   = LEAVETYPE_BASE + DELETE;

    /**
     * REQUIRED IN PORTAL ACTION FOR MAPPING URLS TO LEAVESTYPE PRESENT IN LEAVE
     * POLICY CONTROLLER
     */
    String LEAVEPOLICY_LEAVETYPE              = LEAVESPOLICY_BASE + LEAVETYPE_BASE;
    String LEAVEPOLICY_LEAVESTYPE_DELETE      = LEAVEPOLICY_LEAVETYPE + DELETE;
    String LEAVEPOLICY_LEAVESTYPE_ADD         = LEAVEPOLICY_LEAVETYPE + ADD;
    String LEAVEPOLICY_LEAVESTYPE_UPDATE      = LEAVEPOLICY_LEAVETYPE + UPDATE;

    /**
     * ACTIONS RELATED TO TEAMS FOR PORTAL ACTIONS
     */
    String TEAM_BASE                          = "/team";
    String TEAMS_ADD                          = TEAM_BASE + ADD;
    String TEAM_UPDATE                        = TEAM_BASE + UPDATE;
    String TEAM_DELETE                        = TEAM_BASE + DELETE;
    String SEARCHBYTEAMNAME                   = TEAM_BASE + SEARCHBYNAME;
    String GETMEMBERS                         = "/members";
    String GETTEAMMEMBERS                     = TEAM_BASE + GETMEMBERS;
    String UPDATEMEMBERS                      = "/updateMembers";
    String UPDATETEAMMEMBERS                  = TEAM_BASE + UPDATEMEMBERS;

    /**
     * actions realted to profile for PORTAL ACTION
     */
    String PROFILE_BASE                       = "/profile";
    String USERPROFILESYNC                    = "/utilityController/syncUserProfiles";
    String WISHBIRTHDAY                       = "/sendWishes";
    String PROFILE_EDIT                       = PROFILE_BASE + EDIT;
    String PROFILE_VIEW                       = PROFILE_BASE + VIEW;
    String PROFILE_UPDATE                     = PROFILE_BASE + UPDATE;

    /**
     * Actions mapping related to Employees
     */
    String EMPLOYEE_ADD                       = EMPLOYEE_BASE + ADD;
    String EMPLOYEE_UPDATE                    = EMPLOYEE_BASE + UPDATE;
    String EMPLOYEE_DELETE                    = EMPLOYEE_BASE + DELETE;
    String EMPLOYEE_ATTENDANCE_LIST           = EMPLOYEE_BASE + ATTENDANCELIST;
    
    

    String PICTUREADD                         = "/picture/add";
    String PICTUREDELETE                      = "/picture/delete";

    String PROFILE_PICTUREADD                 = PROFILE_BASE + PICTUREADD;
    String PROFILE_PICTUREDELETE              = PROFILE_BASE + PICTUREDELETE;

    String ATTENDANCE_BASE                    = "/attendance";
    

    String ATTENDANCECALENDAR                 = ATTENDANCE_BASE + "/calendar";

    String BROADCASTYEARLYCALENDAR            = "/notifyyearcalendar";

    String LEAVEREPORT_BASE                   = "/attendance/leavereport";

    String LEAVEREPORTCALENDAR                = LEAVEREPORT_BASE + "/calendar";
    
    String RANDOM                             = "/utilityController";

    String CHECKADMIN                         = RANDOM +"/checkAdmin";


    String MANUALATTENDANCEENTRY              = "/utilityController/manualEntry";
}
