package us.ceka.extract.service.impl;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import us.ceka.dao.FootballMatchDao;
import us.ceka.dao.FootballOddsDao;
import us.ceka.dao.FootballTeamDao;
import us.ceka.domain.FootballLeague;
import us.ceka.domain.FootballMatch;
import us.ceka.domain.FootballOdds;
import us.ceka.domain.FootballOddsId;
import us.ceka.domain.FootballSeason;
import us.ceka.domain.FootballTeam;
import us.ceka.dto.FootballLeagueDto;
import us.ceka.dto.FootballMatchDto;
import us.ceka.dto.FootballOddsDto;
import us.ceka.dto.FootballTeamDto;
import us.ceka.extract.service.FootballOddsRecordService;

@Service("footballOddsRecordService")
@Transactional
public class FootballOddsRecordServiceImpl extends GenericServiceImpl implements FootballOddsRecordService{

	@Autowired
	private FootballOddsDao footballOddsDao;
	@Autowired
	private FootballMatchDao footballMatchDao;
	@Autowired
	private FootballTeamDao footballTeamDao;

	@Autowired
	private FootballMatchDto footballMatchDto;
	@Autowired
	private FootballOddsDto footballOddsDto;
	@Autowired
	private FootballTeamDto footballTeamDto;
	@Autowired
	private FootballLeagueDto footballLeagueDto;

	public void executeRecordOdds() {
		String batch = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES).format(DateTimeFormatter.ofPattern("yyyymmdd-HHmmss"));

		Map<FootballLeague, List<FootballMatch>> map = footballMatchDto.getLatestMatches(FootballLeague.values());
		map.forEach((league, matchList) -> {
			for(FootballMatch match : matchList) {
				FootballMatch dbMatch = footballMatchDao.getByKey(match.getMatchId());
				if(dbMatch == null) footballMatchDao.persist(match);

				FootballOdds odds = footballOddsDto.getLatestById(match.getMatchId());
				if(odds != null) {
					odds.setFootballOddsId(new FootballOddsId(match.getMatchId(), batch));
					FootballOdds dbOdds = footballOddsDao.findRecentOddsRecord(match.getMatchId());
					if(!odds.rateEquals(dbOdds)) {
						log.info("Rate changed: Odds before inserted into database:{}", dbOdds);
						footballOddsDao.persist(odds);
					}
				}
			}				
		});
		
	}

	public void executeUpdateMatchResult() {
		List<FootballMatch> list = footballMatchDao.findByStatus(FootballMatch.MATCH_STATUS.PENDING.getCode());
		for(FootballMatch match : list) {
			FootballTeam t = footballTeamDao.getByName(match.getHomeTeam());
			Map<String, Object> matchMap = footballTeamDto.getResult(
					match.getMatchDate().format(DateTimeFormatter.ofPattern("yyyyMMdd")),
					t.getId()
					);
			
			//message will be given if there is an error
			if(matchMap.size() > 0) {
				matchMap.put("dateUpdated", LocalDateTime.now());
				try {
					BeanUtils.populate(match, matchMap);
					int result = NumberUtils.compare(match.getHomeScore(), match.getAwayScore());
					match.setResult(result > 0 ? 
							FootballMatch.MATCH_RESULT.WIN.getCode() : result == 0 ? 
									FootballMatch.MATCH_RESULT.DRAW.getCode() : FootballMatch.MATCH_RESULT.LOSE.getCode());

				} catch (IllegalAccessException | InvocationTargetException e) {
					log.error("Error when populating map to FootballMatch bean", e);
				}

				log.info("Update FootallMatch result: {}", match);
				footballMatchDao.persist(match);
			}
		}
	}
	
	public void executeUpdateTeamMatches() {
		//List<FootballLeague> leagueList = new java.util.ArrayList<FootballLeague>();
		//leagueList.add(FootballLeague.ENG_PREMIER_LEAGUE);
		//leagueList.add(FootballLeague.GER_BUNDESLIGA);
		//leagueList.add(FootballLeague.ESP_LALIGA);
		//leagueList.add(FootballLeague.ITA_SERIE_A);
		//leagueList.add(FootballLeague.JPN_JLEAGUE);
		//for(FootballLeague league : leagueList) {
		for(FootballLeague league : FootballLeague.values()) {
			if(league.getType().equals(FootballLeague.TYPE.LEAGUE)) {
				//Retrieve current season and insert matches of current seasons into database  
				List<FootballSeason> seasonList = footballLeagueDto.getAllSeasons(league).subList(0, 1); //footballSeasonDao.getSeasons(league, 2); 				
				seasonList.forEach( (season) -> {
					footballTeamDto.getTeamNamesByLeague(league, season).forEach((teamName) -> {
						List<FootballMatch> l = footballMatchDto.getAllMatchResults(league, season, footballTeamDao.getByName(teamName));
						for(FootballMatch match : l) {
							FootballTeam homeTeam = footballTeamDao.getByName(match.getHomeTeam());
							FootballTeam awayTeam = footballTeamDao.getByName(match.getAwayTeam());
							if(homeTeam != null && awayTeam != null){
								String matchId = String.format("A%s-%s-%s", 
										match.getMatchDate().format(DateTimeFormatter.ofPattern("yyyyMMdd")),
												StringUtils.leftPad(homeTeam.getId(), 5, "0"),
												StringUtils.leftPad(awayTeam.getId(), 5, "0")
								);
								match.setMatchId(matchId);
								int result = NumberUtils.compare(match.getHomeScore(), match.getAwayScore());
								match.setResult(result > 0 ? 
										FootballMatch.MATCH_RESULT.WIN.getCode() : result == 0 ? 
												FootballMatch.MATCH_RESULT.DRAW.getCode() : FootballMatch.MATCH_RESULT.LOSE.getCode());
								match.setSeason(season.getSeason());
								
								if(!match.equals(footballMatchDao.getMatch(match.getHomeTeam(), match.getAwayTeam(), match.getMatchDate()))) {
									log.info("Insert football match:{}", match);
									footballMatchDao.persist(match);
								}
							}
						}
					});
				});			
			}
		}
	}
	
}
