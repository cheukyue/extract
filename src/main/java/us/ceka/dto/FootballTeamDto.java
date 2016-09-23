package us.ceka.dto;

import java.util.List;
import java.util.Map;

import us.ceka.domain.FootballLeague;
import us.ceka.domain.FootballSeason;
import us.ceka.domain.FootballTeam;

public interface FootballTeamDto extends AbstractDto<FootballTeam> {
	public List<String> getTeamNamesByLeague(FootballLeague league, FootballSeason season);
	public List<FootballTeam> getAllTeams();
	public Map<String, Object> getResult(String date, String teamId); 
}
