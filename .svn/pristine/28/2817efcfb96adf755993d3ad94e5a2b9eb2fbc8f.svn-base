package com.zillious.corporate_website.portal.dao.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import com.zillious.corporate_website.portal.dao.TeamDao;
import com.zillious.corporate_website.portal.ui.model.Team;

@EnableTransactionManagement
@Repository
public class TeamDaoImpl implements TeamDao {
    private static Logger  s_logger = Logger.getLogger(TeamDaoImpl.class);
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public Session getSession() {
        // return sessionFactory.openSession();
        return sessionFactory.getCurrentSession();
    }

    @Override
    public void closeSession(final Session session) {
        try {
            if (session != null) {
                session.flush();
                // session.close();
            }
        } catch (Exception e) {
            s_logger.error("Error while closing the session", e);
        }
    };

    @Transactional
    @Override
    public List<Team> getTeams() {
        List<Team> team_list = new ArrayList<Team>();

        Session session = null;

        try {
            session = getSession();
            Criteria cr = session.createCriteria(Team.class);

            team_list = cr.list();

        } catch (Exception e) {
            s_logger.error("Error while getting teams from db: ", e);
        } finally {
            closeSession(session);
        }

        return team_list;

    }

    /*
     * this is to add new team in the already existing db
     */
    @Transactional
    @Override
    public int addTeam(Team team) {
        Session session = null;
        int ID = -1;
        try {
            session = getSession();
            session.saveOrUpdate(team);
            ID = team.getTeamId();
        } catch (Exception e) {
            s_logger.error("Error while adding a team: ", e);
            throw new RuntimeException("Error in adding information");
        } finally {
            closeSession(session);
        }

        return ID;
    }

    @Transactional
    @Override
    public boolean updateTeam(Team team) {
        Session session = null;
        try {
            session = getSession();
            session.update(team);
            session.flush();
        } catch (Exception e) {
            s_logger.error("Error while updating a team: ", e);
            throw new RuntimeException("Error in updating team information");

        } finally {
            closeSession(session);
        }

        return true;

    }

    @Transactional
    @Override
    public boolean deleteTeam(Team team) {
        Session session = null;
        try {
            session = getSession();
            session.delete(team);

        } catch (Exception e) {
            s_logger.error("Error while deleting a team: ", e);
            throw new RuntimeException("Error in deleting team");
        } finally {
            closeSession(session);
        }

        return true;

    }

    @Transactional
    @Override
    public List<Team> getTeamsByNamePattern(String queryString) {

        List<Team> teamList = new ArrayList<Team>();
        Session session = null;

        try {
            session = getSession();
            Criteria cr = session.createCriteria(Team.class).add(
                    Restrictions.ilike("m_name", queryString, MatchMode.ANYWHERE));
            teamList = cr.list();

        } catch (Exception e) {
            s_logger.error("Error while getting teams from db: ", e);
        } finally {
            closeSession(session);
        }

        return teamList;
    }

    @Override
    public Set<Team> getTeams(Set<Team> teams) {
        Set<Team> teamList = new HashSet<Team>();
        Session session = null;

        try {
            session = getSession();
            Integer[] teamIds = new Integer[teams.size()];
            int i = 0;
            for (Team team : teams) {
                teamIds[i++] = team.getTeamId();
            }

            Criteria cr = session.createCriteria(Team.class).add(Restrictions.in("m_teamId", teamIds));
            List objList = cr.list();
            if (objList != null) {
                for (Object obj : objList) {
                    teamList.add((Team) obj);
                }
            }

        } catch (Exception e) {
            s_logger.error("Error while getting teams from db: ", e);
        } finally {
            closeSession(session);
        }

        return teamList;
    }

    @Override
    @Transactional
    public Team getTeam(int teamId) {
        Session session = null;

        try {
            session = getSession();

            Criteria cr = session.createCriteria(Team.class).add(Restrictions.eq("m_teamId", teamId));

            Object uniqueResult = cr.uniqueResult();
            if (uniqueResult != null) {
                return (Team) uniqueResult;
            }

        } catch (Exception e) {
            s_logger.error("Error while getting teams from db: ", e);
        } finally {
            closeSession(session);
        }
        return null;
    }
}
