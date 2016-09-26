package us.ceka.dao.impl;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import us.ceka.dao.FootballTeamDao;
import us.ceka.domain.FootballTeam;

@Repository("footballTeamDao")
public class FootballTeamDaoImpl extends AbstractDaoImpl<Integer, FootballTeam> implements FootballTeamDao{
	
	public void truncate() {
		int rowsUpdated = getSession().createNativeQuery("truncate table football_team").executeUpdate();
		log.info("No. of rows affected in truncate query: {}", rowsUpdated);
	}
	
	public FootballTeam getByName(String name) {
		Query query = getSession().createQuery("FROM FootballTeam t where t.name = :name");
		query.setParameter("name", name);
		return query.getResultList().isEmpty() ? null : (FootballTeam) query.getSingleResult();  
	}
}
