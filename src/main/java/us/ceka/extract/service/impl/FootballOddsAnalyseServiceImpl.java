package us.ceka.extract.service.impl;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import us.ceka.dao.FootballTierDao;
import us.ceka.dao.FootballMatchDao;
import us.ceka.dao.FootballOddsDao;
import us.ceka.domain.Analytics;
import us.ceka.domain.FootballLeague;
import us.ceka.domain.FootballMatch;
import us.ceka.domain.FootballOdds;
import us.ceka.domain.FootballTeamMatchup;
import us.ceka.domain.FootballTier;
import us.ceka.domain.KeysObject;
import us.ceka.dto.FootballMatchDto;
import us.ceka.extract.service.FootballOddsAnalyseService;

@Service("footballOddsAnalyseService")
@Transactional
public class FootballOddsAnalyseServiceImpl extends GenericServiceImpl implements FootballOddsAnalyseService{
	@Autowired
	private FootballOddsDao footballOddsDao;
	@Autowired
	private FootballMatchDao footballMatchDao;
	@Autowired
	private FootballTierDao footballTierDao;

	@Autowired
	private FootballMatchDto footballMatchDto;
	
	public void executeAnalyseOdds() {
		//executeAnalyseMatchup("阿仙奴", "車路士", FootballLeague.ENG_PREMIER_LEAGUE);
		
		for(FootballMatch match : footballMatchDao.getLatestMatch()) {
			FootballOdds latestOdds = footballOddsDao.findRecentOddsRecord(match.getMatchId());
			FootballOdds initialOdds = footballOddsDao.findInitialOddsRecord(match.getMatchId());
			log.info("Analysing [{}] {} vs {}", match.getMatchId(), match.getHomeTeam(), match.getAwayTeam());
			
			boolean analyseMatchup = false;
			
			if(!StringUtils.equals(latestOdds.getHandicapLine(), initialOdds.getHandicapLine())) {
				log.info("***handicap line change...");
			}
			if(latestOdds.getHandicapAwayRate().subtract(initialOdds.getHandicapAwayRate()).abs()
					.compareTo(new BigDecimal(Analytics.BIG_RATE_CHANGE.getValue()) ) > 0) {
				log.info("***[{} {}] Away Rate ({},{} vs {}) has {} {} [H:{} D:{} A:{}]",  
						match.getMatchDate(), match.getMatchDay(), match.getMatchId(), match.getHomeTeam(), match.getAwayTeam(),
						latestOdds.getHandicapAwayRate().compareTo(initialOdds.getHandicapAwayRate()) > 0 ? "increased" : "decreased",
						latestOdds.getHandicapAwayRate().subtract(initialOdds.getHandicapAwayRate()).abs(),
						latestOdds.getHomeRate(), latestOdds.getDrawRate(), latestOdds.getAwayRate() 
						);
				analyseMatchup = true;
			}
			
			Map<String, Object> map = footballMatchDto.getMatchStat(match);
			int vsWin = map.get("vsWin") == null ? 0 : Integer.parseInt((String)map.get("vsWin"));
			int vsDraw = map.get("vsDraw") == null ? 0 : Integer.parseInt((String)map.get("vsDraw"));
			int vsLose = map.get("vsLose") == null ? 0 : Integer.parseInt((String)map.get("vsLose"));
			
			if(vsWin + vsDraw + vsLose > Analytics.MIN_REFERENCE_MATCHES.getValue()) {
				if(latestOdds.getHomeRate().compareTo(new BigDecimal(Analytics.DOUBT_RATE.getValue())) > 0) {			
					if((vsWin + vsDraw) / (double) (vsWin + vsDraw + vsLose) > Analytics.HIGH_WIN_POSSIBILITY.getValue()) {
						log.info("***[{} {}] Abnormal Home Rate {} ({},{} vs {}) with {}win, {}draw out of {}", 
								match.getMatchDate(), match.getMatchDay(), latestOdds.getHomeRate(), match.getMatchId(), match.getHomeTeam(), match.getAwayTeam(), 
								vsWin, vsDraw, vsWin + vsDraw + vsLose);
						analyseMatchup = true;
					}
				}
				if(latestOdds.getAwayRate().compareTo(new BigDecimal(Analytics.DOUBT_RATE.getValue())) > 0) {
					if((vsLose + vsDraw) / (double) (vsWin + vsDraw + vsLose) > Analytics.HIGH_WIN_POSSIBILITY.getValue()) {
						log.info("***[{} {}] Abnormal Away Rate {} ({},{} vs {}) with {}win, {}draw out of {}", 
								match.getMatchDate(), match.getMatchDay(), latestOdds.getAwayRate(), match.getMatchId(), match.getHomeTeam(), match.getAwayTeam(), 
								vsLose, vsDraw, vsWin + vsDraw + vsLose);
						analyseMatchup = true;
					}
				}
			}
			if(match.getLeague().getType().equals(FootballLeague.TYPE.LEAGUE) && analyseMatchup )
				executeAnalyseMatchup(match.getHomeTeam(), match.getAwayTeam(), match.getLeague());

		}
		
	}
	
	private void executeAnalyseMatchup(String homeTeam, String awayTeam, FootballLeague league) {
		List<FootballTeamMatchup> homeMatchups = footballMatchDao.getMatchup(homeTeam, league.toString(), FootballMatch.MATCH_AT.HOME);
		//Map<String, Integer> seasonTotalMatches = homeMatchups.stream().collect(Collectors.groupingBy(FootballTeamMatchup::getSeason, Collectors.summingInt(FootballTeamMatchup::getNumMatches)));
	//	Map<KeysObject, List<FootballTeamMatchup>> map = homeMatchups.stream().collect(Collectors.groupingBy(m -> new KeysObject(m.getSeason(), m.getVsTier()), 
		//		Collectors.mapping((FootballTeamMatchup m) -> m, Collectors.toList())));
		/*		
		Map<KeysObject, List<FootballTeamMatchup>> sortedMap = new TreeMap<KeysObject, List<FootballTeamMatchup>>(
				new Comparator<KeysObject>() {
					@Override
					public int compare(KeysObject key1, KeysObject key2) {
						int result = Math.negateExact(((String)key1.getKeys().get(0)).compareTo(((String)key2.getKeys().get(0)))); //m.getSeason()
						if (result != 0) return result;
						result = ((Integer)key1.getKeys().get(1)).compareTo((Integer)key2.getKeys().get(1)); //m.getVsTier()
						return result;
					}
                }
			); 
		sortedMap.putAll(map);
		log.info("{}", sortedMap);
*/
		Map<KeysObject, Integer> homeMatchesInSeason = homeMatchups.stream().collect(Collectors.groupingBy(m -> new KeysObject(m.getSeason(), m.getVsTier()), 
				Collectors.summingInt(FootballTeamMatchup::getNumMatches)));

		for(FootballTeamMatchup matchup : homeMatchups) {
			double rate = matchup.getNumMatches() / (double) homeMatchesInSeason.get(new KeysObject(matchup.getSeason(), matchup.getVsTier()));
			log.info("{} H:{} A:{} {} {}:{} total:{} Rate:{}", matchup.getTeam(), matchup.getTeamTier(), matchup.getVsTier(), 
					String.format("%-10s", matchup.getSeason()), matchup.getResult(), 
					matchup.getNumMatches(), homeMatchesInSeason.get(new KeysObject(matchup.getSeason(), matchup.getVsTier())), rate);
		}
		
		List<FootballTeamMatchup> awayMatchups = footballMatchDao.getMatchup(awayTeam, league.toString(), FootballMatch.MATCH_AT.AWAY);
		Map<KeysObject, Integer> awayMatchesInSeason = awayMatchups.stream().collect(Collectors.groupingBy(m -> new KeysObject(m.getSeason(), m.getVsTier()), 
				Collectors.summingInt(FootballTeamMatchup::getNumMatches)));
		
		for(FootballTeamMatchup matchup : awayMatchups) {
			double rate = matchup.getNumMatches() / (double) awayMatchesInSeason.get(new KeysObject(matchup.getSeason(), matchup.getVsTier()));
			log.info("{} H:{} A:{} {} {}:{} total:{} Rate:{}", matchup.getTeam(), matchup.getTeamTier(), matchup.getVsTier(), 
					String.format("%-10s", matchup.getSeason()), matchup.getResult(), 
					matchup.getNumMatches(), awayMatchesInSeason.get(new KeysObject(matchup.getSeason(), matchup.getVsTier())), rate);
		}
		
	}
	
}
