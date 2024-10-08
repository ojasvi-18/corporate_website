package com.zillious.corporate_website.portal.ui.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
// portal controller
import org.springframework.web.bind.annotation.RequestMethod;

import com.zillious.corporate_website.portal.service.PortalService;
import com.zillious.corporate_website.portal.service.UserService;
import com.zillious.corporate_website.portal.ui.Gender;
import com.zillious.corporate_website.portal.ui.model.HomePageModel;
import com.zillious.corporate_website.portal.ui.model.SidePanelItems;
import com.zillious.corporate_website.portal.ui.model.User;
import com.zillious.corporate_website.portal.ui.navigation.PortalActions;
import com.zillious.corporate_website.portal.ui.navigation.RequestMappings;
import com.zillious.corporate_website.portal.utility.CloudManager;
import com.zillious.corporate_website.ui.beans.SecurityBean;
import com.zillious.corporate_website.ui.security.ZilliousSecurityRequestType;
import com.zillious.corporate_website.ui.security.ZilliousSecurityWrapperRequest;
import com.zillious.corporate_website.ui.session.SessionStore;
import com.zillious.corporate_website.utils.ConfigStore;
import com.zillious.corporate_website.utils.StringUtility;

/**
 * @author nishant.gupta
 *
 */
@Controller
public class PortalController {

    public static final String LOGINTYPE = "PORTAL.LOGINTYPE";

    private static Logger       s_logger  = Logger.getLogger(PortalController.class);

    @Autowired
    private PortalService       m_portalService;

    @Autowired
    private UserService         m_userService;

    @RequestMapping(value = RequestMappings.LOGIN, method = RequestMethod.GET)
    private String getLoginPage(HttpServletRequest request, Model model) {
        ZilliousSecurityWrapperRequest zilliousRequest = SecurityBean.getRequest(request);
        User loggedInUser = SessionStore.getLoggedInUser(zilliousRequest);

        if (loggedInUser != null) {
            return "redirect:/portal/home";
        }

        s_logger.debug("Came here in login method");

        // model to add action url to redirect to home page after login
        model.addAttribute("actionUrl", PortalActions.HOME.getActionURL(zilliousRequest));
        model.addAttribute("logintype", ConfigStore.getStringValue(LOGINTYPE, "saml"));
        return "login";
    }

    /**
     * @param request
     * @param model
     * @return the index.html page after successfully logging in to the to
     *         system
     */
    @RequestMapping(value = RequestMappings.HOME_BASE, method = RequestMethod.POST)
    private String getHomePage(HttpServletRequest request, Model model) {
        s_logger.debug("Came here in getHomePage() : ");
        try {

            ZilliousSecurityWrapperRequest zilliousRequest = SecurityBean.getRequest(request);
            User loggedInUser = SessionStore.getLoggedInUser(zilliousRequest);

            if (loggedInUser == null) {
                String emailId = null;
                try {
                    Authentication authnObj = SecurityContextHolder.getContext().getAuthentication();

                    User user = null;
                    String password = zilliousRequest.getParameter("password", "password",
                            ZilliousSecurityRequestType.DEFAULT_SAFE_STRING);
                    // saml login
                    if (password == null && authnObj != null && authnObj.isAuthenticated()) {
                        String userEmail = (String) authnObj.getPrincipal();
                        if (StringUtility.isValidEmail(userEmail)) {
                            user = new User();
                            user.setEmail(userEmail);
                            user.setIsRequirePwdCheck(false);
                        }

                        model.addAttribute("error", "Invalid Email Id content");

                    } else {
                        // normal login
                        emailId = zilliousRequest.getParameter("email id in the login details", "email",
                                ZilliousSecurityRequestType.EMAIL);

                        boolean invalidEmail = !StringUtility.isValidEmail(emailId);
                        boolean invalidPassword = !StringUtility.isNonEmpty(password);
                        if (invalidEmail || invalidPassword) {
                            model.addAttribute("error", invalidEmail ? "Invalid Email Id content"
                                    : invalidPassword ? "Invalid Password content" : "Incorrect data");
                            return "login";
                        }

                        user = new User();
                        user.setEmail(emailId);
                        user.setPassword(password);
                    }

                    loggedInUser = m_userService.getAuthorizedUser(user);
                    if (loggedInUser == null) {
                        model.addAttribute("error", emailId != null ? "No Such User in the system"
                                : "YOU ARE NOT LOGGED IN TO THE SYSTEM");
                        s_logger.debug("user not valid returning to login page from portal controller :" + model);
                        return "redirect:/portal/login";
                    }
                } catch (Exception e) {
                    s_logger.error("Error while checking for the logged in user", e);
                    model.addAttribute("error", emailId != null ? "ERROR IN USERNAME OR PASSWORD"
                            : "YOU ARE NOT LOGGED IN TO THE SYSTEM");
                    return "redirect:/portal/login";
                }
            }

            return getUserHomePage(zilliousRequest, loggedInUser, model);

        } catch (Exception e) {
            s_logger.debug("user not valid returning to login page from portal controller :", e);
            return "redirect:/portal/login";
        }

    }

    /**
     * @returns the actual html/jsp page to login a user
     * 
     * 
     *          TODO: add saml integration for zillious employees
     */
    @RequestMapping(value = RequestMappings.LOGOUT, method = RequestMethod.GET)
    private String logoutUser(HttpServletRequest request, Model model) {
        ZilliousSecurityWrapperRequest zilliousRequest = SecurityBean.getRequest(request);
        User loggedInUser = SessionStore.getLoggedInUser(zilliousRequest);
        SessionStore.removeLoggedInUser(zilliousRequest);
        SessionStore.invalidateSession(zilliousRequest);
        // model to add action url to redirect to homepage after login
        if (loggedInUser != null) {
            model.addAttribute("message", "You have been logged out successfully");
        }

        return "redirect:/portal/login";
    }

    @RequestMapping(value = RequestMappings.HOME_BASE, method = RequestMethod.GET)
    private String homePage(HttpServletRequest request, Model model) {
        return getHomePage(request, model);
    }

    private String getUserHomePage(ZilliousSecurityWrapperRequest zilliousRequest, User loggedInUser, Model model) {
        model.addAttribute("user_id", loggedInUser.getUserId());
        HomePageModel homePageModel = new HomePageModel();
        m_portalService.addURLsToHomePageModel(zilliousRequest, homePageModel);
        m_portalService.addJsonArraysToHomePageModel(homePageModel);
        m_portalService.addUserNameDesignation(homePageModel, loggedInUser);

        s_logger.debug("Came in HomePageModalJsonURLs: " + homePageModel);

        model.addAttribute("teamJsonUrl", homePageModel.getTeamJsonURL());
        model.addAttribute("teamsByNameJsonUrl", homePageModel.getTeamsByNameJsonUrl());
        model.addAttribute("supervisorDataJsonURL", homePageModel.getSupervisorDataJsonUrl());
        model.addAttribute("teamMemberSearchURL", homePageModel.getTeamMemberSearchURL());
        model.addAttribute("userProfileJsonUrl", homePageModel.getUserProfileJson());
        model.addAttribute("employeesJsonUrl", homePageModel.getEmployeesJsonURl());
        model.addAttribute("userStatusrray", homePageModel.getUserStatusrray());
        model.addAttribute("userRoleArray", homePageModel.getUserRoleArray());
        model.addAttribute("birthdayList", homePageModel.getCurrentBirthdayJsonString());
        model.addAttribute("leavePolicyJsonURL", homePageModel.getLeavePolicyJsonUrl());
        model.addAttribute("leaveTypeJsonURL", homePageModel.getLeaveTypeJsonUrl());
        model.addAttribute("leaveRequestsURL", homePageModel.getLeaveRequestUrl());
        model.addAttribute("wishBirthdayUrl", homePageModel.getWishBirthdayUrl());
        model.addAttribute("leaveRequestStatusArray", homePageModel.getLeaveRequestStatusArray());
        model.addAttribute("logOutUrl", homePageModel.getLoggedOutUser());
        model.addAttribute("attendanceUrl", homePageModel.getAttendanceBaseURL());
        model.addAttribute("broadcastYearlyCalendarURL", homePageModel.getBroadcastYearlyCalendarURL());
        model.addAttribute("isAdminCheckURL", homePageModel.getAdminCheckURL());
        model.addAttribute("randomUtilitiesURL", homePageModel.getRandomUtilitiesURL());

        model.addAttribute("userName", loggedInUser.getUserProfile().getName());// homePageModel.getLoggedinUserName());
        model.addAttribute("userDesignation", loggedInUser.getUserProfile().getDesignation());// homePageModel.getLoggedinUserDesignation());
        String profilePicture = null;
        if (loggedInUser.getUserProfile().getPictureKey() == null) {
            Gender gender = loggedInUser.getUserProfile().getGender();
            if (gender == null) {
                gender = Gender.Male;
            }
            profilePicture = "/static/portal/images/" + gender.name() + ".jpg";
        } else {
            profilePicture = CloudManager.AMAZONS3.getCompleteDisplayURL(loggedInUser.getUserProfile().getPictureKey());
        }
        model.addAttribute("userProfilePicture", profilePicture);

        model.addAttribute("sidePanelItems", SidePanelItems.getSidePanelItemList(loggedInUser));

        SessionStore.setLoggedInUser(zilliousRequest, null, loggedInUser);

        s_logger.debug("Came in HomePageModalUserRoles:" + model);
        return "index";
    }
}
