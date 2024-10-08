package com.zillious.corporate_website.portal.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.JsonArray;
import com.zillious.corporate_website.portal.service.PortalService;
import com.zillious.corporate_website.portal.service.UserService;
import com.zillious.corporate_website.portal.ui.LeaveRequestStatus;
import com.zillious.corporate_website.portal.ui.UserRoles;
import com.zillious.corporate_website.portal.ui.UserStatus;
import com.zillious.corporate_website.portal.ui.controller.PortalController;
import com.zillious.corporate_website.portal.ui.model.HomePageModel;
import com.zillious.corporate_website.portal.ui.model.User;
import com.zillious.corporate_website.portal.ui.navigation.PortalActions;
import com.zillious.corporate_website.ui.security.ZilliousSecurityWrapperRequest;
import com.zillious.corporate_website.utils.ConfigStore;

@Service
public class PortalServiceImpl implements PortalService {

    @Autowired
    private UserService m_userService;

    @Override
    public void addURLsToHomePageModel(ZilliousSecurityWrapperRequest zilliousRequest, HomePageModel homeModel) {
        String teamJsonURL = PortalActions.TEAMS.getActionURL(zilliousRequest);
        String userProfileJsonURL = PortalActions.PROFILE.getActionURL(zilliousRequest);
        String employeesProfileJsonURL = PortalActions.EMPLOYEE.getActionURL(zilliousRequest);
        String supervisorDataJsonUrl = PortalActions.USERNAMESEARCH.getActionURL(zilliousRequest);
        String teamsByNameJsonUrl = PortalActions.TEAMNAMESEARCH.getActionURL(zilliousRequest);
        String leavePolicyJsonUrl = PortalActions.LEAVEPOLICY.getActionURL(zilliousRequest);

        String teamMemberSearchUrl = PortalActions.TEAMMEMBERNAMESEARCH.getActionURL(zilliousRequest);
        homeModel.setSupervisorDataJsonUrl(supervisorDataJsonUrl);
        homeModel.setTeamMemberSearchURL(teamMemberSearchUrl);
        String leaveTypeJsonUrl = PortalActions.LEAVESTYPE.getActionURL(zilliousRequest);
        homeModel.setTeamJsonURL(teamJsonURL);
        homeModel.setEmployeesJsonURl(employeesProfileJsonURL);
        homeModel.setUserProfileJson(userProfileJsonURL);
        homeModel.setTeamsByNameJsonUrl(teamsByNameJsonUrl);
        homeModel.setLeavePolicyJsonUrl(leavePolicyJsonUrl);
        homeModel.setLeaveTypeJsonUrl(leaveTypeJsonUrl);
        homeModel.setLeaveRequestUrl(PortalActions.LEAVEREQUESTS.getActionURL(zilliousRequest));
        homeModel.setWishBirthdayUrl(PortalActions.SENDBIRTHDAYWISHES.getActionURL(zilliousRequest));
        if (ConfigStore.getStringValue(PortalController.LOGINTYPE, "saml").equals("saml")) {
            homeModel.setLoggedOutUser(PortalActions.LOGOUT_SAML.getActionURL(zilliousRequest));
        } else {
            homeModel.setLoggedOutUser(PortalActions.LOGOUT.getActionURL(zilliousRequest));
        }

        homeModel.setAttendanceBaseURL(PortalActions.ATTENDANCE.getActionURL(zilliousRequest));
        homeModel.setBroadcastYearlyCalendarURL(PortalActions.BROADCASTYEARLYCALENDAR.getActionURL(zilliousRequest));
        homeModel.setAdminCheckURL(PortalActions.CHECKADMIN.getActionURL(zilliousRequest));

        homeModel.setRandomUtilitiesURL(PortalActions.RANDOM.getActionURL(zilliousRequest));
    }

    @Override
    @Transactional
    public void addJsonArraysToHomePageModel(HomePageModel homeModel) {
        homeModel.setUserStatusrray(UserStatus.getStatusJson());
        homeModel.setUserRoleArray(UserRoles.getUserRolesJson());
        homeModel.setLeaveRequestStatusArray(LeaveRequestStatus.getStatusJson());

        List<User> usersBdayList = m_userService.getUsersBirthdayList();
        String jsonString;
        if (usersBdayList != null && !usersBdayList.isEmpty()) {
            jsonString = User.convertIntoIdNameJsonArray(usersBdayList);
        } else {
            jsonString = (new JsonArray()).toString();
        }
        homeModel.setCurrentBirthdayJsonString(jsonString);
    }

    @Override
    public void addUserNameDesignation(HomePageModel homeModel, User user) {
        homeModel.setUserName(user.getUserProfile().getName());
        homeModel.setUserDesignation(user.getUserProfile().getDesignation());
    }

}
