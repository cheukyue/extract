package us.ceka.dao;

import java.util.List;

import us.ceka.domain.FootballLeague;
import us.ceka.domain.FootballSeason;

public interface FootballSeasonDao extends AbstractDao<String, FootballSeason>{
	public void truncate();
	public FootballSeason getLatestSeason(FootballLeague league);
	public List<FootballSeason> getSeasons(FootballLeague league, int limit);
}
