package com.zillious.corporate_website.portal.dao;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.zillious.corporate_website.portal.ui.model.Team;

@Component
public interface TeamDao extends Dao {

    public List<Team> getTeams();

    public int addTeam(Team team);

    public boolean updateTeam(Team team);

    public boolean deleteTeam(Team team);

    List<Team> getTeamsByNamePattern(String queryString);

    public Set<Team> getTeams(Set<Team> teams);

    public Team getTeam(int teamId);
}
