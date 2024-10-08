package com.zillious.corporate_website.portal.ui.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.zillious.corporate_website.portal.service.TeamService;
import com.zillious.corporate_website.portal.service.UserService;
import com.zillious.corporate_website.portal.ui.model.User;
import com.zillious.corporate_website.portal.ui.navigation.RequestMappings;
import com.zillious.corporate_website.portal.utility.JsonUtility;
import com.zillious.corporate_website.ui.beans.SecurityBean;
import com.zillious.corporate_website.ui.navigation.WebsiteExceptionType;
import com.zillious.corporate_website.ui.security.ZilliousSecurityWrapperRequest;
import com.zillious.corporate_website.ui.session.SessionStore;

@RestController
@RequestMapping(RequestMappings.TEAM_BASE)
public class TeamController {

    private static Logger     s_logger = Logger.getLogger(TeamController.class);

    private final TeamService teamService;

    @Autowired
    private UserService       userService;

    @Autowired
    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    /**
     * METHOD TO FETCH ALREADY PRESENT TEAMS IN DATA BASE
     * 
     * @param request
     * @return
     */
    @RequestMapping(value = RequestMappings.DIRECT_MAPPING, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody String viewTeams(HttpServletRequest request) {
        ZilliousSecurityWrapperRequest zilliousRequest = SecurityBean.getRequest(request);
        User loggedInUser = SessionStore.getLoggedInUser(zilliousRequest);
        JsonObject teams = new JsonObject();
        if (loggedInUser != null) {
            teams = this.teamService.getTeams(loggedInUser);
        } else {
            JsonUtility.createResponseJson(teams, false, WebsiteExceptionType.SYSTEM_ERROR.getDesc());
        }
        return teams.toString();
    }

    /**
     * METHOD TO ADD NEW TEAM TO DATA BASE ONLY ISADMINROLES() ARE ALLOWED TO
     * MAKE CHANGES
     * 
     * @param t
     * @param request
     * @return
     */
    @RequestMapping(value = RequestMappings.ADD, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody String addNewTeam(@RequestBody String t, HttpServletRequest request) {
        ZilliousSecurityWrapperRequest zilliousRequest = SecurityBean.getRequest(request);
        User loggedInUser = SessionStore.getLoggedInUser(zilliousRequest);
        JsonObject teamJsonObj = new JsonObject();
        if ((loggedInUser != null) && loggedInUser.getUserRole().isAdminRole()) {
            teamJsonObj = this.teamService.addTeam(t, loggedInUser);
        } else {
            JsonUtility.createResponseJson(teamJsonObj, false, WebsiteExceptionType.INVALID_Action_REQUEST.getDesc());
        }
        return teamJsonObj.toString();
    }

    /**
     * METHOD TO UPDATE ALREADY PRESENT TEAM : ONLY ISADMINROLE() USERS ARE
     * ALLOWD TO MAKE CHANGES
     * 
     * @param t
     * @param request
     * @return
     */
    @RequestMapping(value = RequestMappings.UPDATE, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody String updateTeam(@RequestBody String t, HttpServletRequest request) {
        ZilliousSecurityWrapperRequest zilliousRequest = SecurityBean.getRequest(request);
        User loggedInUser = SessionStore.getLoggedInUser(zilliousRequest);
        JsonObject teamFinalJson = new JsonObject();
        if ((loggedInUser != null) && (loggedInUser.getUserRole().isAdminRole())) {
            teamFinalJson = this.teamService.updateTeam(t);
        } else {
            JsonUtility.createResponseJson(teamFinalJson, false, WebsiteExceptionType.INVALID_Action_REQUEST.getDesc());
        }
        return teamFinalJson.toString();

    }

    /**
     * DELETE ALREADY PRESENT TEAM : ONLU ISADMINROLE() USERS ARE ALLOWED TO
     * MAKE CHANGES
     * 
     * @param requestJson
     * @param request
     * @return
     */
    @RequestMapping(value = RequestMappings.DELETE, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody String deleteTeam(@RequestBody String t, HttpServletRequest request) {
        ZilliousSecurityWrapperRequest zilliousRequest = SecurityBean.getRequest(request);
        User loggedInUser = SessionStore.getLoggedInUser(zilliousRequest);
        JsonObject teamResponseJson = new JsonObject();
        if ((loggedInUser != null) && (loggedInUser.getUserRole().isAdminRole())) {
            teamResponseJson = this.teamService.deleteTeam(t, loggedInUser);
        } else {
            JsonUtility.createResponseJson(teamResponseJson, false,
                    WebsiteExceptionType.INVALID_Action_REQUEST.getDesc());
        }
        return teamResponseJson.toString();

    }

    /**
     * THIS API WILL RETURN TEAM DETAILS WHEN SEARCHED BY NAME
     * 
     * @param requestJson
     * @return
     */
    @RequestMapping(value = RequestMappings.SEARCHBYNAME, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody String searchTeamByName(@RequestBody String requestJson) {
        JsonObject finalJSON = new JsonObject();
        if (requestJson == null || requestJson.isEmpty()) {
            JsonUtility.createResponseJson(finalJSON, false, "Incorrect Request");
            return finalJSON.toString();
        }

        JsonParser parser = new JsonParser();

        JsonObject parsedObject = (JsonObject) parser.parse(requestJson);
        if (parsedObject.get("val") == null) {
            JsonUtility.createResponseJson(finalJSON, false, "Incorrect Request");
            return finalJSON.toString();
        }

        String queryString = parsedObject.get("val").getAsString();
        return getTeamListJsonObject(finalJSON, queryString);
    }

    /**
     * @param finalJSON
     * @param queryString
     * @return
     */
    private String getTeamListJsonObject(JsonObject finalJSON, String queryString) {
        if (queryString == null || queryString.isEmpty()) {
            JsonUtility.createResponseJson(finalJSON, false, "Incorrect Request");
            return finalJSON.toString();
        }

        JsonUtility.createResponseJson(finalJSON, true, null);
        JsonArray userListJson = teamService.getTeamsByNamePattern(queryString);
        finalJSON.add("teamsList", userListJson);
        return finalJSON.toString();
    }

    /**
     * Get team members
     * 
     * @param requestJson
     * @param request
     * @return
     */
    @RequestMapping(value = RequestMappings.GETMEMBERS, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody String getTeamMembers(@RequestBody String requestJson, HttpServletRequest request) {
        ZilliousSecurityWrapperRequest zilliousRequest = SecurityBean.getRequest(request);
        User loggedInUser = SessionStore.getLoggedInUser(zilliousRequest);
        JsonObject teamResponseJson = this.teamService.getTeamMembers(requestJson, loggedInUser);
        return teamResponseJson.toString();

    }

    /**
     * METHOD TO UPDATE ALREADY PRESENT TEAM : ONLY ISADMINROLE() USERS ARE
     * ALLOWD TO MAKE CHANGES
     * 
     * @param requestJson
     * @param request
     * @return
     */
    @RequestMapping(value = RequestMappings.UPDATEMEMBERS, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody String saveChangesToTeamMembers(@RequestBody String requestJson, HttpServletRequest request) {
        ZilliousSecurityWrapperRequest zilliousRequest = SecurityBean.getRequest(request);
        User loggedInUser = SessionStore.getLoggedInUser(zilliousRequest);
        JsonObject teamFinalJson = new JsonObject();
        if ((loggedInUser != null) && (loggedInUser.getUserRole().isAdminRole())) {
            s_logger.debug("request json received: " + requestJson);
            teamFinalJson = teamService.updateTeamMembers(requestJson);
        } else {
            JsonUtility.createResponseJson(teamFinalJson, false, WebsiteExceptionType.INVALID_ACCESS.getDesc());
        }
        return teamFinalJson.toString();

    }
}
