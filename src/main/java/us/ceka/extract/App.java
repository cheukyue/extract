package us.ceka.extract;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import us.ceka.extract.config.AppConfig;
import us.ceka.extract.service.FootballOddsAnalyseService;
import us.ceka.extract.service.FootballOddsRecordService;
import us.ceka.extract.service.MaintenanceService;

/**
 * 
 *
 */
public class App 
{
	private static final Logger log = LoggerFactory.getLogger(App.class);
	
	public static void main( String[] args )
	{
		log.info("Application start");
		
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
		ctx.register(AppConfig.class);
		ctx.refresh();
		log.info(ctx.getMessage("app.name", null, Locale.getDefault()));
		FootballOddsRecordService recordService = ctx.getBean(FootballOddsRecordService.class);
		MaintenanceService mainService = ctx.getBean(MaintenanceService.class);
		FootballOddsAnalyseService analyseService = ctx.getBean(FootballOddsAnalyseService.class);
		
		if(args.length > 0) {
			switch (args[0]) {
			case "ANALYSE_ODDS":
				analyseService.executeAnalyseOdds();
				break;
			case "RECORD_ODDS":
				recordService.executeRecordOdds();
				break;
			case "UPDATE_MATCH_RESULT":
				recordService.executeUpdateMatchResult();
				break;
			case "UPDATE_MATCH_RESULT_ARCHIVE":
				recordService.executeUpdateTeamMatches();
				break;
			case "REFRESH_SEASON_TABLE":
				mainService.executeRefreshFootballLeague();
				break;
			case "REFRESH_TEAM_TABLE":
				mainService.executeRefreshTeamTable();
				break;

			}
		}	

		ctx.close();

		log.info("Application end");
	}
}
