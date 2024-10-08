package com.zillious.corporate_website.portal.ui.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.zillious.corporate_website.utils.StringUtility;

@Entity
@Table(name = "team")
public class Team implements DBObject {

    /**
     * 
     */
    private static final long serialVersionUID = 1273089309666406885L;

    @Column(name = "EmailGroups", nullable = true)
    private String            m_emailgp;

    @Id
    @GeneratedValue
    @Column(name = "team_id", nullable = false)
    private int               m_teamId;

    @Column(name = "TeamName", nullable = true)
    private String            m_name;

    @ManyToOne(fetch = FetchType.LAZY)
    @Cascade(org.hibernate.annotations.CascadeType.PERSIST)
    @JoinColumn(name = "Supervisor_id", nullable = false)
    private User              m_supervisor;

    @ManyToMany(mappedBy = "m_teamlist", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<User>         m_members        = new HashSet<User>();

    public String getName() {
        return m_name;
    }

    public void setName(String name) {
        m_name = name;
    }

    public String getEmailgp() {
        return m_emailgp;
    }

    public void setEmailgp(String emailgp) {
        m_emailgp = emailgp;
    }

    public JsonElement convertIntoJson(boolean isManagedByUser) {

        JsonObject team = new JsonObject();
        team.addProperty("EmailGroups", getEmailgp());
        // adding team id to the json required by the user
        team.addProperty("id", getTeamId());
        JsonObject jsonSup = new JsonObject();
        jsonSup.addProperty("id", m_supervisor.getUserId());
        jsonSup.addProperty("name", StringUtility.trimAndNullIsEmpty(m_supervisor.getUserProfile().getName()));
        team.add("Supervisor", jsonSup);
        team.addProperty("TeamName", getName());
        Set<User> members = getMembers();
        team.addProperty("numMembers", members == null ? 0 : members.size());
        team.addProperty("isSupervisor", isManagedByUser);
        team.addProperty("isEditable", true);

        return team;
    }

    public static JsonArray getTeamListJsonObject(List<Team> teamList) {

        if (teamList == null) {
            return null;
        }

        JsonArray array = new JsonArray();

        for (Team team : teamList) {
            JsonElement teamObj = team.convertIntoJson(false);
            if (teamObj != null) {
                array.add((JsonObject) teamObj);
            }
        }
        return array;
    }

    public User getSupervisor() {
        return m_supervisor;
    }

    public void setSupervisor(User supervisor) {
        m_supervisor = supervisor;
    }

    public Set<User> getMembers() {
        return m_members;
    }

    public void setMembers(Set<User> members) {
        m_members = members;
    }

    public void addMember(User member) {
        if (member == null) {
            return;
        }

        if (m_members == null) {
            m_members = new HashSet<User>();
        }

        m_members.add(member);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (!(obj instanceof Team)) {
            return false;
        }

        if (this == obj) {
            return true;
        }

        Team secondDTO = (Team) obj;
        return m_teamId == secondDTO.m_teamId;
    }

    @Override
    public int hashCode() {
        int hashCode = 1;
        hashCode = 31 * hashCode + m_teamId;
        return hashCode;
    }

    public int getTeamId() {
        return m_teamId;
    }

    public void setTeamId(int teamId) {
        m_teamId = teamId;
    }

    public static Set<Team> parseTeamsFromProfileJson(JsonObject profileJson) {
        Set<Team> teamlist = null;
        if (profileJson.getAsJsonArray("TeamDetails") != null) {
            JsonArray teamDetails = profileJson.getAsJsonArray("TeamDetails");
            Team temp_team;
            teamlist = new HashSet<Team>();

            int size = teamDetails.size(), i;
            for (i = 0; i < size; ++i) {

                boolean isSupervisor = false;

                JsonObject team = teamDetails.get(i).getAsJsonObject();
                if (team.get("isSupervisor") != null && team.get("isSupervisor").getAsBoolean()) {
                    isSupervisor = true;
                }

                if (isSupervisor) {
                    continue;
                }

                temp_team = new Team();
                JsonElement emailElement = team.get("EmailGroups");
                if (emailElement != null || profileJson.get("EmailGroups").toString().matches("[A-Z][a-zA-Z]*")) {
                    temp_team.setEmailgp(emailElement.toString().replaceAll("^\"|\"$", ""));
                }

                JsonElement teamNameElement = team.get("TeamName");
                if (teamNameElement != null || profileJson.get("TeamName").toString().matches("[A-Z][a-zA-Z]*")) {
                    temp_team.setName(teamNameElement.toString().replaceAll("^\"|\"$", ""));
                }

                JsonElement teamIdElement = team.get("id");
                temp_team.setTeamId(teamIdElement.getAsInt());

                JsonObject supJSON = team.getAsJsonObject("Supervisor");
                if (supJSON != null) {
                    temp_team.setSupervisor(new User(supJSON.get("id").getAsInt()));
                }
                teamlist.add(temp_team);
            }
        }

        return teamlist;
    }

    /**
     * @param worker
     * @return
     */
    public String getAddTeamNotifierEmailContent(User worker) {

        int tabLevel = 1;

        StringBuilder content = new StringBuilder("Hi,");
        content.append(StringUtility.getNewLineString());
        content.append(StringUtility.getNewLineString());

        content.append("A New Team has been added.");
        content.append(StringUtility.getNewLineString());
        content.append(StringUtility.getNewLineString());

        content.append("Team Name: ").append(StringUtility.trimAndNullIsDefault(this.getName(), this.getEmailgp()));
        content.append(StringUtility.getNewLineString());

        content.append("Team Email id: ")
                .append(StringUtility.trimAndNullIsDefault(this.getEmailgp(), this.getEmailgp()));
        content.append(StringUtility.getNewLineString());
        content.append(StringUtility.getNewLineString());
        content.append("Team Manager: ");
        content.append(StringUtility.getNewLineString());
        content.append(getSupervisor().getEmailDisplayInfo(tabLevel));
        content.append(StringUtility.getNewLineString());
        content.append(StringUtility.getNewLineString());
        content.append("Added by: ");
        content.append(StringUtility.getNewLineString());
        content.append(worker.getEmailDisplayInfo(tabLevel));
        content.append(StringUtility.getNewLineString());

        content.append(StringUtility.getNewLineString());
        content.append("Thanks").append(StringUtility.getNewLineString()).append("Zillious Solutions Pvt Ltd");
        return content.toString();
    }

    public String getDeleteTeamNotifierEmailContent(User worker) {
        int tabLevel = 1;
        StringBuilder content = new StringBuilder("Hi,");
        content.append(StringUtility.getNewLineString());
        content.append(StringUtility.getNewLineString());

        content.append("A Team has been Deleted.");
        content.append(StringUtility.getNewLineString());
        content.append(StringUtility.getNewLineString());

        content.append("Team Name: ").append(StringUtility.trimAndNullIsDefault(this.getName(), this.getEmailgp()));
        content.append(StringUtility.getNewLineString());

        content.append("Team Email id: ")
                .append(StringUtility.trimAndNullIsDefault(this.getEmailgp(), this.getEmailgp()));
        content.append(StringUtility.getNewLineString());
        content.append(StringUtility.getNewLineString());
        content.append("Team Manager: ");
        content.append(StringUtility.getNewLineString());
        content.append(getSupervisor().getEmailDisplayInfo(tabLevel));
        content.append(StringUtility.getNewLineString());
        content.append(StringUtility.getNewLineString());
        content.append("Deleted by: ");
        content.append(StringUtility.getNewLineString());
        content.append(worker.getEmailDisplayInfo(tabLevel));
        content.append(StringUtility.getNewLineString());

        content.append(StringUtility.getNewLineString());
        content.append("Thanks").append(StringUtility.getNewLineString()).append("Zillious Solutions Pvt Ltd");
        return content.toString();
    }

    public String getDeleteTeamFailureNotifierEmailContent(User worker) {
        int tabLevel = 1;
        StringBuilder content = new StringBuilder("Hi,");
        content.append(StringUtility.getNewLineString());
        content.append(StringUtility.getNewLineString());

        content.append("Below user tried to Delete Team");
        content.append(StringUtility.getNewLineString());

        content.append(worker.getEmailDisplayInfo(tabLevel));
        content.append(StringUtility.getNewLineString());

        content.append(StringUtility.getNewLineString());
        content.append("Thanks").append(StringUtility.getNewLineString()).append("Zillious Solutions Pvt Ltd");
        return content.toString();
    }

    public String getDeleteTeamEmailContentForTeamMember() {

        StringBuilder content = new StringBuilder("Hi,");
        content.append(StringUtility.getNewLineString());
        content.append(StringUtility.getNewLineString());
        content.append("You have been removed from team. ")
                .append(StringUtility.trimAndNullIsDefault(this.getName(), this.getEmailgp()));

        content.append(StringUtility.getNewLineString());
        content.append("Thanks").append(StringUtility.getNewLineString()).append("Zillious Solutions Pvt Ltd");
        return content.toString();
    }

    public String getAddTeamEmailContentForTeamMember() {

        StringBuilder content = new StringBuilder("Hi,");
        content.append(StringUtility.getNewLineString());
        content.append(StringUtility.getNewLineString());
        content.append("You have been added in team. ")
                .append(StringUtility.trimAndNullIsDefault(this.getName(), this.getEmailgp()));

        content.append(StringUtility.getNewLineString());
        content.append("Thanks").append(StringUtility.getNewLineString()).append("Zillious Solutions Pvt Ltd");
        return content.toString();
    }

    public String getAddTeamEmailSubject() {
        return "Team Added - " + StringUtility.trimAndNullIsDefault(getName(), getEmailgp());
    }

    public String getDeleteTeamEmailSubject() {
        return "Team Deleted - " + StringUtility.trimAndNullIsDefault(getName(), getEmailgp());
    }

    public String getDeleteTeamFailureNotifierEmailSubject() {
        return "Tried to Delete Team - " + StringUtility.trimAndNullIsDefault(getName(), getEmailgp());
    }

    public String getDeleteTeamEmailSubjectForTeamMember() {
        return "Removed From Team ";
    }

    public String getAddTeamEmailSubjectForTeamMember() {
        return "Added in team ";
    }

    public String getTeamDetails(int tabLevel) {
        String tab = StringUtility.getTabContent(tabLevel);
        StringBuilder content = new StringBuilder();
        content.append(tab).append("Team Name: ").append(getName());
        content.append(StringUtility.getNewLineString());
        content.append(tab).append("Team Email: ").append(getEmailgp());

        return content.toString();
    }

    public String getTeamDetails() {
        return getTeamDetails(0);
    }
}
