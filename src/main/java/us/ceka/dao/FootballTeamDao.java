package us.ceka.dao;

import us.ceka.domain.FootballTeam;

public interface FootballTeamDao extends AbstractDao<Integer, FootballTeam>{
	
	public void truncate();
	public FootballTeam getByName(String name);
}
