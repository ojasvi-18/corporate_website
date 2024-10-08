package com.zillious.corporate_website.portal.entity;

import java.util.List;
import java.util.Set;
import com.zillious.corporate_website.portal.ui.model.LeaveRequest;
import com.zillious.corporate_website.portal.ui.model.User;
/**
 * @author ojasvi.bhardwaj
 *
 */
public class WeeklySummaryMessage {

    private List<User> birthdayToday;

    private List<User> birthdayThisWeek;

    private Set<LeaveRequest> pendingTeamLeaves; 

    private Set<LeaveRequest> teamLeavesToday ;  

    private Set<LeaveRequest> leavesThisWeek;

    public Set<LeaveRequest> getPendingTeamLeaves() {
        return pendingTeamLeaves;
    }

    public void setPendingTeamLeaves(Set<LeaveRequest> pendingTeamLeaves) {
        this.pendingTeamLeaves = pendingTeamLeaves;
    }

    public Set<LeaveRequest> getTeamLeavesToday() {
        return teamLeavesToday;
    }

    public void setTeamLeavesToday(Set<LeaveRequest> teamLeavesToday) {
        this.teamLeavesToday = teamLeavesToday;
    }

    public Set<LeaveRequest> getLeavesThisWeek() {
        return leavesThisWeek;
    }

    public void setLeavesThisWeek(Set<LeaveRequest> leavesThisWeek) {
        this.leavesThisWeek = leavesThisWeek;
    }

    public List<User> getBirthdayThisWeek() {
        return birthdayThisWeek;
    }

    public void setBirthdayThisWeek(List<User> birthdayThisWeek) {
        this.birthdayThisWeek = birthdayThisWeek;
    }

    public List<User> getBirthdayToday() {
        return birthdayToday;
    }

    public void setBirthdayToday(List<User> usersHavingBirthdayToday) {
        this.birthdayToday = (List<User>) usersHavingBirthdayToday;
    }
}
