package com.zillious.corporate_website.portal.service;

import java.util.Set;

import org.springframework.stereotype.Component;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.zillious.corporate_website.portal.ui.model.Team;
import com.zillious.corporate_website.portal.ui.model.User;

@Component
public interface TeamService {

    /**
     * this method is used in fetching all the tems present in data base
     * 
     * @param loggedInUser
     * @return
     */
    public JsonObject getTeams(User loggedInUser);

    public JsonObject addTeam(String t, User loggedInUser);

    public JsonObject updateTeam(String t);

    public JsonObject deleteTeam(String t,User loggedInUser);

    /**
     * this API return team name according to the search pattern on UI used in
     * TypeAhead on UI
     * 
     * @param queryString
     * @return
     */
    public JsonArray getTeamsByNamePattern(String queryString);

    /**
     * this amethod is used in updateProfile in UpdateServiceImpl
     * 
     * @param teams
     * @return
     */
    public Set<Team> getTeams(Set<Team> teams);

    public JsonObject getTeamMembers(String requestJson, User loggedInUser);

   public JsonObject updateTeamMembers(String requestJson);

  


}
