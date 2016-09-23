package us.ceka.dto;

import java.util.List;

import us.ceka.domain.FootballLeague;
import us.ceka.domain.FootballSeason;

public interface FootballLeagueDto extends AbstractDto<FootballLeague>{
	public List<FootballSeason> getAllSeasons(FootballLeague league);

}
