package us.ceka.dao;

import us.ceka.domain.FootballOdds;
import us.ceka.domain.FootballOddsId;

public interface FootballOddsDao extends AbstractDao<FootballOddsId, FootballOdds>{
	public FootballOdds findById(FootballOddsId id);
	public FootballOdds findRecentOddsRecord(String matchId);
	public FootballOdds findInitialOddsRecord(String matchId);
}
