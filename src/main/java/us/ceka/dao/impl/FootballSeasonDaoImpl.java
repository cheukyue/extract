package us.ceka.dao.impl;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import us.ceka.dao.FootballSeasonDao;
import us.ceka.domain.FootballLeague;
import us.ceka.domain.FootballSeason;

@Repository("footballSeasonDao")
public class FootballSeasonDaoImpl extends AbstractDaoImpl<String, FootballSeason> implements FootballSeasonDao{
	
	public void truncate() {
		int rowsUpdated = getSession().createNativeQuery("truncate table football_season").executeUpdate();
		log.info("No. of rows affected in truncate query: {}", rowsUpdated);
	}
	
	public FootballSeason getLatestSeason(FootballLeague league) {
		Query q = getSession().createQuery("from FootballSeason s where s.season = (select max(fs.season) from FootballSeason fs where fs.league = :league) and s.league = :league");
		q.setParameter("league", league);
		return q.getResultList().isEmpty() ? null : (FootballSeason)q.getSingleResult(); 
	}
	
	@SuppressWarnings("unchecked")
	public List<FootballSeason> getSeasons(FootballLeague league, int limit) {
		Query q =  getSession().createQuery("from FootballSeason s where s.league = :league order by season desc");
		q.setParameter("league", league);
		q.setMaxResults(limit);
		return q.getResultList();
	}
	
	
	
}
