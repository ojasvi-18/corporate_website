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
import com.zillious.corporate_website.portal.ui.model.User;
import com.zillious.corporate_website.portal.service.UserService;
import com.zillious.corporate_website.portal.ui.navigation.RequestMappings;
import com.zillious.corporate_website.portal.utility.JsonUtility;
import com.zillious.corporate_website.ui.beans.SecurityBean;
import com.zillious.corporate_website.ui.navigation.WebsiteException;
import com.zillious.corporate_website.ui.navigation.WebsiteExceptionType;
import com.zillious.corporate_website.ui.security.ZilliousSecurityWrapperRequest;
import com.zillious.corporate_website.ui.session.SessionStore;

@RestController
// @RequestMapping(RequestMappings.EMPLOYEE_BASE)
public class UserController {

    private static Logger s_logger = Logger.getLogger(UserController.class);

    private UserService   userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * this function will return list of all users present in the db for the
     * employee page(employee.html)
     */
    @RequestMapping(value = RequestMappings.EMPLOYEE_BASE, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String getEmployeeList(HttpServletRequest request) {
        JsonObject finalObj = new JsonObject();
        try {
            ZilliousSecurityWrapperRequest zilliousRequest = SecurityBean.getRequest(request);
            User loggedInUser = SessionStore.getLoggedInUser(zilliousRequest);
            if (loggedInUser == null) {
                throw new WebsiteException(WebsiteExceptionType.INVALID_ACCESS);
            }

            JsonArray usersList = this.userService.getAllUsersAsJson();
            finalObj.add("empList", usersList);
            JsonUtility.addIsAdminProperty(finalObj, loggedInUser);
            JsonUtility.createResponseJson(finalObj, true, null);
        } catch (Exception e) {
            s_logger.error("error in getting employee list as json from the database", e);
            if (e instanceof WebsiteException) {
                JsonUtility.createResponseJson(finalObj, false, ((WebsiteException) e).getType().getDesc());
            } else {
                JsonUtility.createResponseJson(finalObj, false, e.getMessage());
            }
        }
        return finalObj.toString();
    }
    
    
    @RequestMapping(value = RequestMappings.EMPLOYEE_ATTENDANCE_LIST, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody String getEmployeeListWithAttendanceTime(@RequestBody String dateStr , HttpServletRequest request){
        JsonObject finalObj = new JsonObject();
        try{
            ZilliousSecurityWrapperRequest zilliousRequest = SecurityBean.getRequest(request);
            User loggedInUser = SessionStore.getLoggedInUser(zilliousRequest);
            if (loggedInUser == null) {
                throw new WebsiteException(WebsiteExceptionType.INVALID_ACCESS);
            }
            

            JsonParser parser = new JsonParser();
            JsonObject jobj =(JsonObject)parser.parse(dateStr);
            String dateString = null;
            if(jobj.get("date") !=null){
                dateString =jobj.get("date").getAsString();
            }
            
            JsonArray usersList = this.userService.getAllUsersAsJsonForAttendance(dateString);
            finalObj.add("empList", usersList);
            JsonUtility.addIsAdminProperty(finalObj, loggedInUser);
            JsonUtility.createResponseJson(finalObj, true, null);

        } catch(Exception e){
            s_logger.error("error in getting employee list as json from the database", e);
            if (e instanceof WebsiteException) {
                JsonUtility.createResponseJson(finalObj, false, ((WebsiteException) e).getType().getDesc());
            } else {
                JsonUtility.createResponseJson(finalObj, false, e.getMessage());
            }
        }
        
        return finalObj.toString();
        
    }

    @RequestMapping(value = RequestMappings.EMPLOYEE_ADD, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody String addNewUser(@RequestBody String userJson, HttpServletRequest request) {
        JsonObject responseJson = new JsonObject();
        try {
            ZilliousSecurityWrapperRequest zilliousRequest = SecurityBean.getRequest(request);
            User loggedInUser = SessionStore.getLoggedInUser(zilliousRequest);
            if ((loggedInUser == null) || (!loggedInUser.getUserRole().isAdminRole())) {
                JsonUtility.createResponseJson(responseJson, false,
                        WebsiteExceptionType.INVALID_Action_REQUEST.getDesc());
                s_logger.debug("not a valid user for this request in addnewuser function" + responseJson);
            } else {

                responseJson = this.userService.addUser(userJson);
                s_logger.debug("add new user json " + responseJson);
            }
            return responseJson.toString();

        } catch (Exception e) {
            JsonUtility.createResponseJson(responseJson, false, WebsiteExceptionType.EXCEPTION_ERROR.getDesc());
            s_logger.debug("exception in add new user:", e);
            return responseJson.toString();
        }
    }

    @RequestMapping(value = RequestMappings.EMPLOYEE_UPDATE, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody String updateTeam(@RequestBody String userJson, HttpServletRequest request) {
        JsonObject responseJson = new JsonObject();
        try {
            ZilliousSecurityWrapperRequest zilliousRequest = SecurityBean.getRequest(request);
            User loggedInUser = SessionStore.getLoggedInUser(zilliousRequest);
            if ((loggedInUser == null) || (!loggedInUser.getUserRole().isAdminRole())) {
                JsonUtility.createResponseJson(responseJson, false,
                        WebsiteExceptionType.INVALID_Action_REQUEST.getDesc());
                s_logger.debug("not a valid user for this request in update function" + responseJson);
            } else {
                responseJson = this.userService.updateUser(userJson);
                s_logger.debug("update employee json: " + userJson);
            }
            return responseJson.toString();
        } catch (Exception e) {
            JsonUtility.createResponseJson(responseJson, false, WebsiteExceptionType.EXCEPTION_ERROR.getDesc());
            s_logger.debug("exception in update new user: ", e);
            return responseJson.toString();

        }
    }

    @RequestMapping(value = RequestMappings.EMPLOYEE_DELETE, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody String deleteTeam(@RequestBody String userJson, HttpServletRequest request) {
        JsonObject responseJson = new JsonObject();
        try {
            ZilliousSecurityWrapperRequest zilliousRequest = SecurityBean.getRequest(request);
            User loggedInUser = SessionStore.getLoggedInUser(zilliousRequest);
            if ((loggedInUser == null) || (!loggedInUser.getUserRole().isAdminRole())) {
                JsonUtility.createResponseJson(responseJson, false,
                        WebsiteExceptionType.INVALID_Action_REQUEST.getDesc());
                s_logger.debug("not a valid user for this request in delete function" + responseJson);
            } else {
                responseJson = this.userService.deleteUser(userJson);
                s_logger.debug("delete employee json: " + userJson);
            }
            return responseJson.toString();

        } catch (Exception e) {
            JsonUtility.createResponseJson(responseJson, false, WebsiteExceptionType.EXCEPTION_ERROR.getDesc());
            s_logger.debug("exception in delete new user: ", e);
            return responseJson.toString();
        }
    }

    @RequestMapping(value = RequestMappings.USERNAMESEARCH, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody String searchEmployeesByName(@RequestBody String requestJson) {
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
        return getEmployeeListJsonObject(finalJSON, queryString);

    }

    @RequestMapping(value = RequestMappings.TEAMMEMBERNAMESEARCH, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody String searchTeamMemberByName(@RequestBody String requestJson, HttpServletRequest request) {
        try {
            ZilliousSecurityWrapperRequest zilliousRequest = SecurityBean.getRequest(request);
            User loggedInUser = SessionStore.getLoggedInUser(zilliousRequest);
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
            return getTeamMemberJsonObject(queryString, loggedInUser);
        } catch (Exception e) {
            s_logger.error("Error in getting team members through ajax", e);
        }

        JsonObject response = new JsonObject();
        response.add("employees", new JsonArray());
        return response.toString();
    }

    private String getTeamMemberJsonObject(String queryString, User loggedInUser) {
        JsonObject finalJSON = new JsonObject();
        if (queryString == null || queryString.isEmpty()) {
            JsonUtility.createResponseJson(finalJSON, false, "Incorrect Request");
            return finalJSON.toString();
        }

        JsonUtility.createResponseJson(finalJSON, true, null);
        JsonArray userListJson = userService.getTeamMembersByNamePattern(queryString, loggedInUser);
        finalJSON.add("employees", userListJson);
        return finalJSON.toString();
    }

    /**
     * @param finalJSON
     * @param queryString
     * @return
     */
    private String getEmployeeListJsonObject(JsonObject finalJSON, String queryString) {
        if (queryString == null || queryString.isEmpty()) {
            JsonUtility.createResponseJson(finalJSON, false, "Incorrect Request");
            return finalJSON.toString();
        }

        JsonUtility.createResponseJson(finalJSON, true, null);
        JsonArray userListJson = userService.getUsersByNamePattern(queryString);
        finalJSON.add("employees", userListJson);
        return finalJSON.toString();
    }

    @RequestMapping(value = RequestMappings.SEARCHBYNAME, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody String getEmployeesByName(@RequestBody String queryString) {
        JsonObject finalJSON = new JsonObject();
        return getEmployeeListJsonObject(finalJSON, queryString);
    }

}
