package us.ceka.extract.service.impl;

import java.math.BigDecimal;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import us.ceka.dao.FootballMatchDao;
import us.ceka.dao.FootballOddsDao;
import us.ceka.domain.Analytics;
import us.ceka.domain.FootballMatch;
import us.ceka.domain.FootballOdds;
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
	private FootballMatchDto footballMatchDto;
	
	public void executeAnalyseOdds() {
		for(FootballMatch match : footballMatchDao.getLatestMatch()) {
			FootballOdds latestOdds = footballOddsDao.findRecentOddsRecord(match.getMatchId());
			FootballOdds initialOdds = footballOddsDao.findInitialOddsRecord(match.getMatchId());
			log.info("Analysing [{}] {} vs {}", match.getMatchId(), match.getHomeTeam(), match.getAwayTeam());
			
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
			}
			
			Map<String, Object> map = footballMatchDto.getMatchStat(match);
			int vsWin = map.get("vsWin") == null ? 0 : Integer.parseInt((String)map.get("vsWin"));
			int vsDraw = map.get("vsDraw") == null ? 0 : Integer.parseInt((String)map.get("vsDraw"));
			int vsLose = map.get("vsLose") == null ? 0 : Integer.parseInt((String)map.get("vsLose"));
			
			if(latestOdds.getHomeRate().compareTo(new BigDecimal(Analytics.DOUBT_RATE.getValue())) > 0) {
				if((vsWin + vsDraw) / (double) (vsWin + vsDraw + vsLose) > Analytics.HIGH_WIN_POSSIBILITY.getValue()) {
					log.info("***[{} {}] Abnormal Home Rate {} ({},{} vs {}) with {}win, {}draw out of {}", 
							match.getMatchDate(), match.getMatchDay(), latestOdds.getHomeRate(), match.getMatchId(), match.getHomeTeam(), match.getAwayTeam(), 
							vsWin, vsDraw, vsWin + vsDraw + vsLose);
				}
			}
			if(latestOdds.getAwayRate().compareTo(new BigDecimal(Analytics.DOUBT_RATE.getValue())) > 0) {
				if((vsLose + vsDraw) / (double) (vsWin + vsDraw + vsLose) > Analytics.HIGH_WIN_POSSIBILITY.getValue()) {
					log.info("***[{} {}] Abnormal Away Rate {} ({},{} vs {}) with {}win, {}draw out of {}", 
							match.getMatchDate(), match.getMatchDay(), latestOdds.getAwayRate(), match.getMatchId(), match.getHomeTeam(), match.getAwayTeam(), 
							vsLose, vsDraw, vsWin + vsDraw + vsLose);
				}
			}

		}
	}
	
}
