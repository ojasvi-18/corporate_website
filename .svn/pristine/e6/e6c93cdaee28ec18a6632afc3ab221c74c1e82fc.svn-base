package com.zillious.corporate_website.portal.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.zillious.corporate_website.portal.dao.TeamDao;
import com.zillious.corporate_website.portal.entity.EmailMessage;
import com.zillious.corporate_website.portal.service.TeamService;
import com.zillious.corporate_website.portal.service.UserService;
import com.zillious.corporate_website.portal.ui.model.Team;
import com.zillious.corporate_website.portal.ui.model.User;
import com.zillious.corporate_website.portal.utility.JsonUtility;
import com.zillious.corporate_website.ui.navigation.WebsiteException;
import com.zillious.corporate_website.ui.navigation.WebsiteExceptionType;
import com.zillious.corporate_website.utils.EmailSender;
import com.zillious.corporate_website.utils.StringUtility;

@Service
public class TeamServiceImpl implements TeamService {
    private static Logger s_logger = Logger.getLogger(TeamServiceImpl.class);

    @Autowired
    TeamDao               teamDao;

    @Autowired
    private UserService   userService;

    @Override
    @Transactional
    public JsonObject getTeams(User loggedInUser) {

        JsonArray teamjson = new JsonArray();
        JsonObject finalJson = new JsonObject();
        try {
            List<Team> teamlist = this.teamDao.getTeams();

            if (teamlist != null) {
                teamjson = genJson(teamlist);
            }
            finalJson.add("TeamList", teamjson);
            JsonUtility.addIsAdminProperty(finalJson, loggedInUser);

            s_logger.debug("get teams : " + finalJson);
            return finalJson;

        } catch (Exception e) {
            JsonUtility.createResponseJson(finalJson, false, WebsiteExceptionType.EXCEPTION_ERROR.getDesc());
            s_logger.debug("Exception in get teams: ", e);
            return finalJson;
        }

    }

    private JsonArray genJson(List<Team> teamlist) {
        JsonArray teamdetails = new JsonArray();

        if (teamlist != null) {
            for (Team team : teamlist) {
                teamdetails.add(team.convertIntoJson(false));
            }
        }
        return teamdetails;
    }

    @Override
    @Transactional
    public JsonObject addTeam(String jsonString, User loggedInUser) {
        JsonParser parser = new JsonParser();
        JsonObject jobj = (JsonObject) parser.parse(jsonString);
        Team team = new Team();

        team.setEmailgp(jobj.get("EmailGroups").toString().replaceAll("^\"|\"$", ""));
        team.setName(jobj.getAsJsonPrimitive("TeamName").toString().replaceAll("^\"|\"$", ""));
        // team.setId(jobj.get("id").getAsInt());
        JsonObject supervisorObj = (JsonObject) jobj.get("Supervisor");
        JsonObject finalJSON = new JsonObject();

        boolean isSuccess = false;
        int supId = supervisorObj.get("id").getAsInt();
        User supervisor = userService.getUser(supId);
        team.setSupervisor(supervisor);
        supervisor.addManagedTeam(team);
        String errorMsg = "";
        try {
            // isSuccess = this.teamDao.addTeam(team);
            isSuccess = userService.updateUser(supervisor);
            finalJSON.addProperty("id", String.valueOf(team.getTeamId()));
        } catch (Exception e) {
            s_logger.debug("add team: ", e);
            errorMsg = e.getMessage();
        }

        try {
            EmailMessage emailMessage = new EmailMessage();

            List<User> adminUsers = userService.getAdminRoleUsers();
            if (adminUsers != null) {
                for (User admin : adminUsers) {
                    emailMessage.addTo(admin.getEmail());
                }
            }

            emailMessage.addCC(team.getSupervisor().getEmail());

            emailMessage.setSubject(team.getAddTeamEmailSubject());
            String content = team.getAddTeamNotifierEmailContent(loggedInUser);
            emailMessage.setContent(content);
            EmailSender.sendEmail(emailMessage);
        } catch (Exception e) {
            // , e will give you the stack trace of the error instead of just
            // the message
            s_logger.error("Error on adding team", e);
        }


        JsonUtility.createResponseJson(finalJSON, isSuccess, errorMsg);
        return finalJSON;
    }

    @Override
    @Transactional
    public JsonObject updateTeam(String jsonString) {
        JsonParser parser = new JsonParser();
        JsonObject jobj = (JsonObject) parser.parse(jsonString);
        Team team = new Team();
        team.setTeamId(jobj.get("id").getAsInt());

        team = teamDao.getTeam(team.getTeamId());

        if (jobj.get("EmailGroups") != null || StringUtility.isValidEmail(jobj.get("EmailGroups").toString())) {
            team.setEmailgp(jobj.get("EmailGroups").toString().replaceAll("^\"|\"$", ""));
        }

        User newSupervisor = null;
        if (jobj.get("Supervisor") != null) {
            int supervisorId = ((JsonObject) jobj.get("Supervisor")).get("id").getAsInt();

            newSupervisor = userService.getUser(supervisorId);

            newSupervisor.addManagedTeam(team);
            team.setSupervisor(newSupervisor);
        }

        if (jobj.get("TeamName") != null || StringUtility.isValidName(jobj.get("TeamName").toString())) {
            team.setName(jobj.getAsJsonPrimitive("TeamName").toString().replaceAll("^\"|\"$", ""));
        }

        JsonObject finalJSON = new JsonObject();
        finalJSON.addProperty("id", team.getTeamId());
        boolean isSuccess = false;
        String errorMsg = "";
        try {
            isSuccess = userService.updateUser(newSupervisor);
        } catch (Exception e) {
            s_logger.debug("update team: ", e);
            errorMsg = e.getMessage();
        }

        JsonUtility.createResponseJson(finalJSON, isSuccess, errorMsg);

        return finalJSON;

    }

    @Override
    @Transactional
    public JsonObject deleteTeam(String jsonString, User loggedInUser) {
        JsonParser parser = new JsonParser();
        JsonObject jobj = (JsonObject) parser.parse(jsonString);
        Team team = teamDao.getTeam(jobj.get("id").getAsInt());

        Set<User> teamMembers = new HashSet<User>();
        teamMembers.addAll(team.getMembers());

        team.getMembers().removeAll(team.getMembers());

        User supervisor = team.getSupervisor();

        supervisor.getManagedTeams().remove(team);

        JsonObject finalJSON = new JsonObject();
        finalJSON.addProperty("id", team.getTeamId());
        boolean isSuccess = false;
        String errorMsg = "";

        try {

            isSuccess = userService.updateUser(supervisor);

        } catch (Exception e) {
            s_logger.debug("deleted team : ", e);
            errorMsg = e.getMessage();
        }

        try {
            if (isSuccess) {
                // code to send mail to administrators that a team has been
                // deleted starts here.

                // Email if Any team is deleted
                EmailMessage emailMessage = new EmailMessage();

                List<User> adminUsers = userService.getAdminRoleUsers();
                if (adminUsers != null) {
                    for (User admin : adminUsers) {
                        emailMessage.addTo(admin.getEmail());
                    }
                }

                emailMessage.addCC(team.getSupervisor().getEmail());

                emailMessage.setSubject(team.getDeleteTeamEmailSubject());
                String content = team.getDeleteTeamNotifierEmailContent(loggedInUser);
                emailMessage.setContent(content);
                EmailSender.sendEmail(emailMessage);

                // code to send mail to administrators that a team has been
                // deleted
                // ends here.

                // code to send mail to team members that they have been removed
                // from the team.
                for (User member : teamMembers) {
                    userService.sendUserRemovedFromTeamNotification(team, member, null);
                }

            } else {
                // if team was not able to be deleted

                EmailMessage emailMessage = new EmailMessage();

                List<User> adminUsers = userService.getAdminRoleUsers();
                if (adminUsers != null) {
                    for (User admin : adminUsers) {
                        emailMessage.addTo(admin.getEmail());
                    }
                }

                emailMessage.addCC(team.getSupervisor().getEmail());

                emailMessage.setSubject(team.getDeleteTeamFailureNotifierEmailSubject());
                String content = team.getDeleteTeamFailureNotifierEmailContent(loggedInUser);
                emailMessage.setContent(content);
                EmailSender.sendEmail(emailMessage);
            }

        } catch (Exception e) {
            // , e will give you the stack trace of the error instead of just
            // the message
            s_logger.error("Error on deleting team", e);
        }
        // .....................................................................

        JsonUtility.createResponseJson(finalJSON, isSuccess, errorMsg);
        return finalJSON;
    }

    @Override
    @Transactional
    public JsonArray getTeamsByNamePattern(String queryString) {
        List<Team> teamList = teamDao.getTeamsByNamePattern(queryString);
        JsonArray teamListJson = Team.getTeamListJsonObject(teamList);
        if (teamListJson == null) {
            teamListJson = new JsonArray();
        }
        return teamListJson;
    }

    @Override
    public Set<Team> getTeams(Set<Team> teams) {
        if (teams != null && !teams.isEmpty()) {
            teams = teamDao.getTeams(teams);
            s_logger.debug("getTeams :" + teams);

            return teams;
        } else {
            s_logger.debug("getTeams in teamService is null:");
            return null;
        }
    }

    @Override
    @Transactional
    public JsonObject getTeamMembers(String requestJson, User loggedInUser) {
        JsonParser parser = new JsonParser();
        try {
            JsonObject jobj = (JsonObject) parser.parse(requestJson);
            JsonElement getEmployeesElement = jobj.get("getEmployees");
            JsonObject response = new JsonObject();

            if (getEmployeesElement != null && getEmployeesElement.getAsBoolean()) {
                if (loggedInUser == null || !loggedInUser.getUserRole().isAdminRole()) {
                    throw new WebsiteException(WebsiteExceptionType.INVALID_ACCESS);
                }

                JsonArray allEmployeesJsonArray = userService.getAllUsersAsJson();
                if (allEmployeesJsonArray == null) {
                    allEmployeesJsonArray = new JsonArray();
                }

                response.add("employees", allEmployeesJsonArray);
            }

            Team team = teamDao.getTeam(jobj.get("id").getAsInt());
            response.addProperty("id", team.getTeamId());
            JsonArray members = new JsonArray();
            if (team.getMembers() != null && !team.getMembers().isEmpty()) {
                for (User member : team.getMembers()) {
                    members.add(member.convertToIdNameEmailJsonObject());
                }
            }

            response.add("members", members);

            JsonUtility.createResponseJson(response, true, null);

            return response;

        } catch (Exception e) {
            JsonObject response = new JsonObject();
            JsonUtility.createResponseJson(response, false, e.getMessage());
            return response;
        }
    }

    @Override
    @Transactional
    public JsonObject updateTeamMembers(String requestJson) {
        JsonObject finalJson = null;
        try {

            JsonParser parser = new JsonParser();
            JsonObject parsedRequest = (JsonObject) parser.parse(requestJson);
            int teamId = parsedRequest.get("id").getAsInt();
            Team currentTeam = teamDao.getTeam(teamId);

            JsonArray addToTeamArray = null;
            JsonArray removeFromTeamArray = null;

            List<User> usersToFetch = new ArrayList<User>();
            List<User> addToTeam = null;
            List<User> removeFromTeam = null;

            JsonElement jsonElement = parsedRequest.get("addToTeam");
            if (jsonElement != null) {
                addToTeamArray = jsonElement.getAsJsonArray();
                addToTeam = new ArrayList<User>();
                Iterator<JsonElement> iterator = addToTeamArray.iterator();
                while (iterator.hasNext()) {
                    JsonObject obj = (JsonObject) iterator.next();
                    User user = new User(obj.get("id").getAsInt());
                    addToTeam.add(user);
                    usersToFetch.add(user);
                    
                    try {
                        EmailMessage emailMessage = new EmailMessage();
                        Team team = new Team();

                        emailMessage.addTo(user.getEmail());

                        emailMessage.setSubject(team.getAddTeamEmailSubjectForTeamMember());
                        String content = team.getAddTeamEmailContentForTeamMember();
                        emailMessage.setContent(content);
                        EmailSender.sendEmail(emailMessage);
                    } catch (Exception e) {
                        // , e will give you the stack trace of the error
                        // instead of just
                        // the message
                        s_logger.error("Error in adding", e);
                    }
                   
                    
                   
                }
            }

            jsonElement = parsedRequest.get("removeFromTeamArray");
            if (jsonElement != null) {
                removeFromTeamArray = jsonElement.getAsJsonArray();
                removeFromTeam = new ArrayList<User>();
                Iterator<JsonElement> iterator = removeFromTeamArray.iterator();
                while (iterator.hasNext()) {
                    JsonObject obj = (JsonObject) iterator.next();
                    User user = new User(obj.get("id").getAsInt());
                    removeFromTeam.add(user);
                    usersToFetch.add(user);
                    
                    try {
                        EmailMessage emailMessage = new EmailMessage();

                        emailMessage.addTo(user.getEmail());
                        Team team = new Team();

                        emailMessage.setSubject(team.getDeleteTeamEmailSubjectForTeamMember());
                        String content = team.getDeleteTeamEmailContentForTeamMember();
                        emailMessage.setContent(content);
                        EmailSender.sendEmail(emailMessage);
                    } catch (Exception e) {
                        // , e will give you the stack trace of the error
                        // instead of just
                        // the message
                        s_logger.error("Error in removing", e);
                    }
                }
            }

            Map<Integer, User> fetchedUsers = userService.getUsers(usersToFetch);
            if (fetchedUsers == null || fetchedUsers.size() != usersToFetch.size()) {
                throw new WebsiteException(WebsiteExceptionType.MISSING_USER);
            }

            if (addToTeam != null) {
                for (User toAdd : addToTeam) {
                    User addedUser = fetchedUsers.get(toAdd.getUserId());
                    addedUser.addTeam(currentTeam);
                }
            }

            if (removeFromTeam != null) {
                for (User toRemove : removeFromTeam) {
                    User removedUser = fetchedUsers.get(toRemove.getUserId());
                    Set<Team> teamlist = removedUser.getTeamlist();
                    teamlist.remove(currentTeam);
                    removedUser.setTeamlist(teamlist);
                }
            }

            boolean isUpdated = userService.updateUsers(fetchedUsers.values());
            finalJson = new JsonObject();
            JsonUtility.createResponseJson(finalJson, isUpdated, isUpdated ? null : "Could not save changes");

            finalJson.add("team", currentTeam.convertIntoJson(true));

        } catch (Exception e) {
            finalJson = new JsonObject();

            String errorMsg = null;
            if (e instanceof WebsiteException) {
                errorMsg = ((WebsiteException) e).getType().getDesc();
            }

            if (errorMsg == null) {
                errorMsg = WebsiteExceptionType.OTHER.getDesc();
            }
            JsonUtility.createResponseJson(finalJson, false, errorMsg);
        }

        return finalJson;
    }

  

}
