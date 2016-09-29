package us.ceka.dao.impl;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import us.ceka.dao.FootballMatchDao;
import us.ceka.domain.FootballMatch;
import us.ceka.domain.FootballTeamMatchup;

@Repository("footballMatchDao")
public class FootballMatchDaoImpl extends AbstractDaoImpl<String, FootballMatch> implements FootballMatchDao{
	
	@SuppressWarnings("unchecked")
	public List<FootballMatch> findByStatus(String... status) {
		Query q = getSession().createQuery("from FootballMatch m where m.status in :status", FootballMatch.class);
		q.setParameter("status", Arrays.asList(status));
		return q.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<FootballMatch> getLatestMatch() {
		Query q = getSession().createQuery("from FootballMatch m where m.matchDate > now() order by m.matchDate");
		return q.getResultList();
	}
	
	public FootballMatch getMatch(String hometeam, String awayTeam, LocalDateTime matchDate) {
		//Query q = getSession().createNativeQuery("select * from football_match where home_team = :home and away_team =:away and DATE(match_date) = :date", FootballMatch.class);
		Query q = getSession().createQuery("from FootballMatch m where m.homeTeam = :home and m.awayTeam = :away and m.matchDate >= :start and m.matchDate < :end");
		q.setParameter("home", hometeam);
		q.setParameter("away", awayTeam);
		q.setParameter("start", matchDate.truncatedTo(ChronoUnit.DAYS));
		q.setParameter("end", matchDate.truncatedTo(ChronoUnit.DAYS).plusDays(1));
		return q.getResultList().isEmpty() ? null : (FootballMatch)q.getSingleResult();
	}
	
	@SuppressWarnings("unchecked")
	public List<FootballTeamMatchup> getMatchup(String team, String league, FootballMatch.MATCH_AT matchAt) {
		if(matchAt == null) return null;
		String namedQuery = null;
		switch(matchAt) {
			case HOME: namedQuery = "home_team_matchup"; break;
			case AWAY: namedQuery = "away_team_matchup"; break;
			case NEUTRAL: return null;
		}
		Query q = getSession().createNamedQuery(namedQuery).setParameter("team", team).setParameter("league", league);
		return q.getResultList();
	}
	
}
