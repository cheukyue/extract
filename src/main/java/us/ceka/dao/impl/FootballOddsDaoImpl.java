package us.ceka.dao.impl;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import us.ceka.dao.FootballOddsDao;
import us.ceka.domain.FootballOdds;
import us.ceka.domain.FootballOddsId;

@Repository("footballOddsDao")
public class FootballOddsDaoImpl extends AbstractDaoImpl<FootballOddsId, FootballOdds> implements FootballOddsDao {
	
	public FootballOdds findById(FootballOddsId id) {
		return getByKey(id);
	}
	
	public FootballOdds findRecentOddsRecord(String matchId) {
		Query q = getSession().createQuery("from FootballOdds f where f.footballOddsId.matchId = :matchId and f.dateCreated = (select max(f.dateCreated) from FootballOdds f where f.footballOddsId.matchId = :matchId)" );
		q.setParameter("matchId", matchId);
		return q.getResultList().isEmpty() ? null : (FootballOdds)q.getSingleResult();
	}
	
	public FootballOdds findInitialOddsRecord(String matchId) {
		Query q = getSession().createQuery("from FootballOdds f where f.footballOddsId.matchId = :matchId and f.dateCreated = (select min(f.dateCreated) from FootballOdds f where f.footballOddsId.matchId = :matchId)" );
		q.setParameter("matchId", matchId);
		return q.getResultList().isEmpty() ? null : (FootballOdds)q.getSingleResult();
	}
	
}
