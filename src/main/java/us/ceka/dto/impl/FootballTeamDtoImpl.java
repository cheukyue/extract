package us.ceka.dto.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.math.NumberUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Repository;

import us.ceka.domain.FootballLeague;
import us.ceka.domain.FootballMatch;
import us.ceka.domain.FootballSeason;
import us.ceka.domain.FootballTeam;
import us.ceka.dto.FootballTeamDto;
import us.ceka.extract.util.RegexUtil;

@Repository("footballTeamDto")
public class FootballTeamDtoImpl extends AbstractDtoJsoupImpl<FootballTeam> implements FootballTeamDto{
	
	public List<String> getTeamNamesByLeague(FootballLeague league, FootballSeason season) {
		List<String> l = new ArrayList<String>();
		Document doc = getJsoupTemplate().getDocumnetByAlias("url.league.table", league.getId(), season.getSeasonId());
		Elements teamElts = doc.select("table.mainTable td a");
		teamElts.forEach((team)-> {
				log.info("Team Name: {}", team.text());
				l.add(team.text());
		});
		return l;
	}
	
	public List<FootballTeam> getAllTeams() {
		List<FootballTeam> list = new ArrayList<FootballTeam> ();
		Document doc = getJsoupTemplate().getDocumnetByAlias("url.teams");
		Elements teams = doc.select("#ctl00_cm_srch_DDL_TeamList option");
		for(Element t : teams) {
			String teamId = t.attr("value");
			if(log.isDebugEnabled()) log.debug("#ctl00_cm_srch_DDL_TeamList option html:{}", t);
			if(NumberUtils.isDigits(teamId)) {
				List<String> teamName = RegexUtil.findSingleGroupList("(.*?)\\s\\((.*)\\)", t.text()); 
				if(teamName.size() > 0) {
					if("Kyoto Purple Sanga".equals(teamName.get(1))) continue;
					FootballTeam ft = new FootballTeam();
					ft.setId(teamId);
					ft.setName(teamName.get(0));
					ft.setEngName(teamName.get(1));
					log.info("{}", ft);
					list.add(ft);
				}
			}
		}

		return list;
	}
	
	public Map<String, Object> getResult(String date, String teamId) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		Document doc = getJsoupTemplate().getDocumnetByAlias("url.match.result", date, date, teamId);
		Element halfResult = doc.getElementById("ctl00_cm_rptMatches_ctl01_tdHalfRes");
		if(halfResult != null) {
			if(FootballMatch.MATCH_REMARK.CANCELLED.getMessage().equals(halfResult.text())) {
				map.put("status", FootballMatch.MATCH_STATUS.RESCHEDULED.getCode());
				return map;
			}
		}
		
		if(halfResult != null) {
			List<String> hl = RegexUtil.findSingleGroupList("([0-9]+) : ([0-9]+)", halfResult.text());
			map.put("homeHalfScore", hl.get(0));
			map.put("awayHalfScore", hl.get(1));
		}
		Element fullResult = doc.getElementById("ctl00_cm_rptMatches_ctl01_tdFullRes");
		if(fullResult != null) {
			List<String> fl = RegexUtil.findSingleGroupList("([0-9]+) : ([0-9]+)", fullResult.text());
			map.put("homeScore", fl.get(0));
			map.put("awayScore", fl.get(1));
		}
		
		if(halfResult != null && fullResult != null)
			map.put("status", FootballMatch.MATCH_STATUS.COMPLETED.getCode());

		return map;
	}
}
