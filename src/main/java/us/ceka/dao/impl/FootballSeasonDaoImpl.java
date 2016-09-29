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
	
	/*
update football_season set start = '2016-08-01 00:00:00', end = '2017-05-30 00:00:00' where league_name = '英超' and season = '今季';
update football_season set start = '2015-08-01 00:00:00', end = '2016-05-30 00:00:00' where league_name = '英超' and season = '2015-2016';
update football_season set start = '2014-08-01 00:00:00', end = '2015-05-30 00:00:00' where league_name = '英超' and season = '2014-2015';
update football_season set start = '2013-08-01 00:00:00', end = '2014-05-30 00:00:00' where league_name = '英超' and season = '2013-2014';
update football_season set start = '2012-08-01 00:00:00', end = '2013-05-30 00:00:00' where league_name = '英超' and season = '2012-2013';

update football_season set start = '2016-08-01 00:00:00', end = '2017-05-30 00:00:00' where league_name = '意甲' and season = '今季';
update football_season set start = '2015-08-01 00:00:00', end = '2016-05-30 00:00:00' where league_name = '意甲' and season = '2015-2016';
update football_season set start = '2014-08-01 00:00:00', end = '2015-05-30 00:00:00' where league_name = '意甲' and season = '2014-2015';
update football_season set start = '2013-08-01 00:00:00', end = '2014-05-30 00:00:00' where league_name = '意甲' and season = '2013-2014';
update football_season set start = '2012-08-01 00:00:00', end = '2013-05-30 00:00:00' where league_name = '意甲' and season = '2012-2013';

update football_season set start = '2016-08-01 00:00:00', end = '2017-05-30 00:00:00' where league_name = '意甲' and season = '今季';
update football_season set start = '2015-08-01 00:00:00', end = '2016-05-30 00:00:00' where league_name = '意甲' and season = '2015-2016';
update football_season set start = '2014-08-01 00:00:00', end = '2015-05-30 00:00:00' where league_name = '意甲' and season = '2014-2015';
update football_season set start = '2013-08-01 00:00:00', end = '2014-05-30 00:00:00' where league_name = '意甲' and season = '2013-2014';
update football_season set start = '2012-08-01 00:00:00', end = '2013-05-30 00:00:00' where league_name = '意甲' and season = '2012-2013';

update football_season set start = '2016-08-01 00:00:00', end = '2017-05-30 00:00:00' where league_name = '德甲' and season = '今季';
update football_season set start = '2015-08-01 00:00:00', end = '2016-05-30 00:00:00' where league_name = '德甲' and season = '2015-2016';
update football_season set start = '2014-08-01 00:00:00', end = '2015-05-30 00:00:00' where league_name = '德甲' and season = '2014-2015';
update football_season set start = '2013-08-01 00:00:00', end = '2014-05-30 00:00:00' where league_name = '德甲' and season = '2013-2014';
update football_season set start = '2012-08-01 00:00:00', end = '2013-05-30 00:00:00' where league_name = '德甲' and season = '2012-2013';

update football_season set start = '2016-08-01 00:00:00', end = '2017-05-30 00:00:00' where league_name = '西甲' and season = '今季';
update football_season set start = '2015-08-01 00:00:00', end = '2016-05-30 00:00:00' where league_name = '西甲' and season = '2015-2016';
update football_season set start = '2014-08-01 00:00:00', end = '2015-05-30 00:00:00' where league_name = '西甲' and season = '2014-2015';
update football_season set start = '2013-08-01 00:00:00', end = '2014-05-30 00:00:00' where league_name = '西甲' and season = '2013-2014';
update football_season set start = '2012-08-01 00:00:00', end = '2013-05-30 00:00:00' where league_name = '西甲' and season = '2012-2013';

update football_season set start = '2016-07-01 00:00:00', end = '2016-12-31 00:00:00' where league_name = '日聯' and season = '今季';
update football_season set start = '2016-02-01 00:00:00', end = '2016-06-30 00:00:00' where league_name = '日聯' and season = '2016-Stage 1';
update football_season set start = '2015-07-01 00:00:00', end = '2015-12-31 00:00:00' where league_name = '日聯' and season = '2015-Stage 2';
update football_season set start = '2015-02-01 00:00:00', end = '2015-06-30 00:00:00' where league_name = '日聯' and season = '2015-Stage 1';
update football_season set start = '2014-02-01 00:00:00', end = '2014-12-30 00:00:00' where league_name = '日聯' and season = '2014';
update football_season set start = '2013-02-01 00:00:00', end = '2013-12-30 00:00:00' where league_name = '日聯' and season = '2013';
*/
	 
	
}
