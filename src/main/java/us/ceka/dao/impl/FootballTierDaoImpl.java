package us.ceka.dao.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import us.ceka.dao.FootballTierDao;
import us.ceka.domain.FootballLeague;
import us.ceka.domain.FootballTier;

@Repository("footballTierDao")
public class FootballTierDaoImpl extends AbstractDaoImpl<String, FootballTier> implements FootballTierDao{
	
	public Map<String, Integer> getNumTier(FootballLeague league) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		getSession().createQuery("select count(1) from FootballTier ft where ft.league = :league").getResultList();
		return map;
	}
}
