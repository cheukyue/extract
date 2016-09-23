package us.ceka.extract.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import us.ceka.dao.FootballSeasonDao;
import us.ceka.dao.FootballTeamDao;
import us.ceka.domain.FootballLeague;
import us.ceka.domain.FootballSeason;
import us.ceka.domain.FootballTeam;
import us.ceka.dto.FootballLeagueDto;
import us.ceka.dto.FootballTeamDto;
import us.ceka.extract.service.MaintenanceService;

@Service("maintenanceService")
@Transactional
public class MaintenanceServiceImpl extends GenericServiceImpl implements MaintenanceService{
	@Autowired
	private FootballTeamDto footballTeamDto;
	@Autowired
	private FootballLeagueDto footballLeagueDto;
	@Autowired
	private FootballTeamDao footballTeamDao;
	@Autowired
	private FootballSeasonDao footballSeasonDao;
	
	
	public void executeRefreshTeamTable() {
		log.info("truncate footballteam table");
		footballTeamDao.truncate();

		List<FootballTeam> set = footballTeamDto.getAllTeams();

		log.info("insert records into footballteam table");
		for(FootballTeam t : set) {
			footballTeamDao.persist(t);
		}
	}
	
	public void executeRefreshFootballLeague() {
		log.info("truncate footballSeason table");
		footballSeasonDao.truncate();
		
		for(FootballLeague l : FootballLeague.values()) {
			if(l.getType().equals(FootballLeague.TYPE.LEAGUE)) {
				List<FootballSeason> seasonList = footballLeagueDto.getAllSeasons(l);
				for(FootballSeason season : seasonList) footballSeasonDao.persist(season);
			}
		}
	}
}
