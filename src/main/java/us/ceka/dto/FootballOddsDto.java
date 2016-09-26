package us.ceka.dto;

import us.ceka.domain.FootballOdds;

public interface FootballOddsDto extends AbstractDto<FootballOdds> {
	public FootballOdds getLatestById(String matchId);
}
