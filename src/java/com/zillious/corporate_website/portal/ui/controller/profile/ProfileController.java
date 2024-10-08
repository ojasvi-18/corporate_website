package com.zillious.corporate_website.portal.ui.controller.profile;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.zillious.corporate_website.portal.service.UserService;
import com.zillious.corporate_website.portal.ui.dto.FormDataWithFile;
import com.zillious.corporate_website.portal.ui.model.User;
import com.zillious.corporate_website.portal.ui.navigation.RequestMappings;
import com.zillious.corporate_website.portal.utility.JsonUtility;
import com.zillious.corporate_website.ui.beans.SecurityBean;
import com.zillious.corporate_website.ui.navigation.WebsiteException;
import com.zillious.corporate_website.ui.navigation.WebsiteExceptionType;
import com.zillious.corporate_website.ui.security.ZilliousSecurityWrapperRequest;
import com.zillious.corporate_website.ui.session.SessionStore;
import com.zillious.corporate_website.utils.NumberUtility;

@RestController
public class ProfileController {

    private static Logger s_logger = Logger.getLogger(ProfileController.class);

    @Autowired
    private UserService   userService;

    /**
     * this method will e called when user wants to edit profile and it will
     * fetch user details from back end with the provided user_id
     * 
     * @param userId
     * @return
     */
    @RequestMapping(value = RequestMappings.PROFILE_EDIT, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody String editProfile(@RequestBody String requestJson, HttpServletRequest request) {
        return handleEditViewRequest(requestJson, request);
    }

    @RequestMapping(value = RequestMappings.PROFILE_VIEW, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody String viewProfile(@RequestBody String requestJson, HttpServletRequest request) {

        return handleEditViewRequest(requestJson, request);
    }

    private String handleEditViewRequest(String requestJson, HttpServletRequest request) {
        ZilliousSecurityWrapperRequest zilliousRequest = SecurityBean.getRequest(request);
        User loggedInUser = SessionStore.getLoggedInUser(zilliousRequest);
        if (loggedInUser == null) {
            JsonObject json = new JsonObject();
            JsonUtility.createResponseJson(json, false, WebsiteExceptionType.INVALID_ACCESS.getDesc());
            return json.toString();
        }

        JsonParser parser = new JsonParser();
        JsonObject useridJsonObj = (JsonObject) parser.parse(requestJson);
        int user_id = useridJsonObj.get("user_id").getAsInt();

        JsonObject json = userService.getJsonForProfileByUserID(loggedInUser, user_id);
        if (json.isJsonNull()) {
            json = new JsonObject();
            JsonUtility.createResponseJson(json, false, "Error. Please contact the System Administrator");
        }
        return json.toString();
    }

    /**
     * @param userProfileJson, obtained after making changes in the ui
     * @return
     */
    @RequestMapping(value = RequestMappings.PROFILE_UPDATE, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody String updateProfile(HttpServletRequest request, @RequestBody String userProfileJson) {

        ZilliousSecurityWrapperRequest zilliousRequest = SecurityBean.getRequest(request);
        User loggedInUser = SessionStore.getLoggedInUser(zilliousRequest);

        JsonObject json = userService.updateProfile(loggedInUser, userProfileJson);

        if (json.isJsonNull()) {
            json = new JsonObject();
            JsonUtility.createResponseJson(json, false, "Error. Please contact the System Administrator");
        }

        return json.toString();
    }

    @RequestMapping(value = RequestMappings.PROFILE_PICTUREADD, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody String addOrUpdateProfilePic(HttpServletRequest request,
            @RequestParam(value = "file", required = false) MultipartFile file, @RequestParam("user_id") String userId) {
        JsonObject response = null;
        try {
            ZilliousSecurityWrapperRequest zilliousRequest = SecurityBean.getRequest(request);

            User loggedInUser = SessionStore.getLoggedInUser(zilliousRequest);
            FormDataWithFile formDataWithFile = new FormDataWithFile();
            formDataWithFile.setFile(file);
            formDataWithFile.setUser_id(NumberUtility.parsetIntWithDefaultOnErr(userId, 0));
            s_logger.debug("User id: " + formDataWithFile.getUser_id());
            s_logger.debug("file properties " + formDataWithFile.getFile().getOriginalFilename());
            response = userService.updateProfilePicture(loggedInUser, formDataWithFile, zilliousRequest);

        } catch (Exception e) {
            s_logger.error("Error while updating the profile picture in the user profile", e);
            String errorMessage = null;
            if (e instanceof WebsiteException) {
                errorMessage = ((WebsiteException) e).getType().getDesc();
            } else {
                errorMessage = e.getMessage();
            }
            response = new JsonObject();
            JsonUtility.createResponseJson(response, false, errorMessage);
        }
        return response.toString();
    }

    @RequestMapping(value = RequestMappings.PROFILE_PICTUREDELETE, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody String deleteProfilePic(HttpServletRequest request, @RequestBody String requestJson) {
        JsonObject response = null;
        try {
            ZilliousSecurityWrapperRequest zilliousRequest = SecurityBean.getRequest(request);
            User loggedInUser = SessionStore.getLoggedInUser(zilliousRequest);
            response = userService.deleteProfilePicture(loggedInUser, requestJson, zilliousRequest);
        } catch (Exception e) {
            s_logger.error("Error while deleting the profile picture in the user profile", e);
            String errorMessage = null;
            if (e instanceof WebsiteException) {
                errorMessage = ((WebsiteException) e).getType().getDesc();
            } else {
                errorMessage = e.getMessage();
            }
            response = new JsonObject();
            JsonUtility.createResponseJson(response, false, errorMessage);
        }
        return response.toString();
    }

}
