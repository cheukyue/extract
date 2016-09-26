package us.ceka.dto;

import java.util.List;
import java.util.Map;

import us.ceka.domain.FootballLeague;
import us.ceka.domain.FootballMatch;
import us.ceka.domain.FootballSeason;
import us.ceka.domain.FootballTeam;

public interface FootballMatchDto extends AbstractDto<FootballMatch>{
	public List<FootballMatch> getLatestMatchesByLeauge(FootballLeague league);
	public Map<FootballLeague, List<FootballMatch>> getAllLatestMatches();
	public Map<FootballLeague, List<FootballMatch>> getLatestMatches(FootballLeague... league);
	public Map<String, Object> getMatchStat(FootballMatch footballMatch);
	public List<FootballMatch> getAllMatchResults(FootballLeague league, FootballSeason season, FootballTeam team);
}
