package us.ceka.dto.impl;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import us.ceka.domain.FootballLeague;
import us.ceka.domain.FootballMatch;
import us.ceka.domain.FootballSeason;
import us.ceka.domain.FootballTeam;
import us.ceka.dto.FootballMatchDto;
import us.ceka.extract.util.RegexUtil;

@Repository("footballMatchDto")
public class FootballMatchDtoImpl extends AbstractDtoJsoupImpl<FootballMatch> implements FootballMatchDto{
	
	private @Value("${footballodds.numPerPage}") int numberMatchesPerPage = 60;

	public List<FootballMatch> getLatestMatchesByLeauge(FootballLeague league) {
		return getLatestMatches(league).get(league);
	}

	public Map<FootballLeague, List<FootballMatch>> getAllLatestMatches() {		
		return getLatestMatches(FootballLeague.values());
	}

	public Map<FootballLeague, List<FootballMatch>> getLatestMatches(FootballLeague... league) {
		List<FootballMatch> matchList = new ArrayList<FootballMatch>();
		Map<FootballLeague, List<FootballMatch>> leagueMatchMap = new HashMap<FootballLeague, List<FootballMatch>>();
		for(FootballLeague l : league) leagueMatchMap.put(l, new ArrayList<FootballMatch>());

		//Retrieve all matches from <options>
		Document matchDoc = getJsoupTemplate().getDocumnetByAlias("url.allOdds");
		Elements matchTags = matchDoc.select("#footballmaincontent select option");
		for(Element elt : matchTags) {
			if(StringUtils.contains(elt.attr("value"), "matchid")) {
				if(log.isDebugEnabled()) log.debug("{}", elt.attr("value"));
				List<String> matchInfo = RegexUtil.findSingleGroupList("matchid=(\\d+)&tdate=(\\d{2}\\-\\d{2}\\-\\d{4})&tday=(\\w{3})&tnum=(\\d+)", elt.attr("value"));
				List<String> teamName = RegexUtil.findSingleGroupList("(\\S+) 對 (\\S+)", elt.text());

				Map<String, Object> propMap = new HashMap<String, Object>();
				propMap.put("matchDate", LocalDateTime.parse(String.format("%s 00:00", matchInfo.get(1)), DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")));
				propMap.put("homeTeam", teamName.get(0));
				propMap.put("awayTeam", teamName.get(1));
				propMap.put("matchDay", matchInfo.get(2));
				propMap.put("matchNum", matchInfo.get(3));
				propMap.put("status", FootballMatch.MATCH_STATUS.PENDING.getCode());
				FootballMatch footballMatch = new FootballMatch(matchInfo.get(0));
				try {
					BeanUtils.populate(footballMatch, propMap);
				} catch (IllegalAccessException | InvocationTargetException e) {
					log.error("Error on creating FootballMatch bean", e);
				}
				log.info("{}", footballMatch);
				matchList.add(footballMatch);
			}
		}
		log.info("Number of matches at [{}]: {}", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), matchList.size());

		//Categorize each matches into leagues
		int numPages = (int) Math.ceil(matchList.size() / (double) numberMatchesPerPage);
		int i, n;
		for(i = 1, n = numPages; i <= n; i++) {
			Document categoryDoc = getJsoupTemplate().getDocumnetByAlias("url.main", new Integer(i));
			for(FootballMatch match : matchList) {
				if(log.isDebugEnabled()) log.debug(String.format("#rmid%s td.cflag img", match.getMatchId()));
				Element leagueNode = categoryDoc.select(String.format("#rmid%s td.cflag img", match.getMatchId())).first();
				Element timeNode = categoryDoc.select(String.format("#rmid%s td.cesst span", match.getMatchId())).first();

				String leagueCode = leagueNode == null ? null : leagueNode.className();
				if(log.isDebugEnabled()) log.debug("leagueCode:{}", leagueNode);
				String matchDateTime = timeNode == null ? null : timeNode.text();
				if(log.isDebugEnabled()) log.debug("timeNode:{}", timeNode);

				for(FootballLeague l : league) {
					if(l.getCode().equals(leagueCode)) {
						Map<String, Object> propMap = new HashMap<String, Object>();
						propMap.put("league", l);
						if(matchDateTime != null) {
							List<String> matchDateParts = RegexUtil.findSingleGroupList("(\\d{2}/\\d{2}) (\\d{2}:\\d{2})", matchDateTime);
							LocalDateTime matchDate = LocalDateTime.parse(String.format("%s/%s %s", matchDateParts.get(0), match.getMatchDate().getYear(),
									matchDateParts.get(1)), DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
							propMap.put("matchDate", matchDate);
						}
						try {
							BeanUtils.populate(match, propMap);
						} catch (IllegalAccessException | InvocationTargetException e) {
							log.error("Error on creating FootballMatch bean", e);
						}
						log.info("{}", match);
						leagueMatchMap.get(l).add(match);
					}
				}

			}
		}
		leagueMatchMap.forEach((k, v)-> log.info("{}:{}", k, v));
		
		return leagueMatchMap;
	}

	public Map<String, Object> getMatchStat(FootballMatch footballMatch) {
		Map<String, Object> map = new HashMap<String, Object>();
		Document statDoc = getJsoupTemplate().getDocumnetByAlias("url.stat.main", 
				footballMatch.getMatchDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")), 
				footballMatch.getMatchDay(), 
				footballMatch.getMatchNum()
		);

		Elements vsElt = statDoc.select("a#topMostLayoutTemplate_H2HLayoutTemplate1_H2HPage1Control1_H2HTeamsRank1_H2H2Link");
		String params = RegexUtil.findFirstSingleGroup("\\?(.*)", vsElt.attr("href"));
		
		if(vsElt != null) {
			Document vsDoc = getJsoupTemplate().getDocumnetByAlias("url.stat.vs", params);
			Elements vslts = vsDoc.select("#topMostLayoutTemplate_H2HLayoutTemplate1_H2HPage2Control1__ctl0_htmlTable tr:eq(1) td");
			Elements vsHomeElts = vsDoc.select("#topMostLayoutTemplate_H2HLayoutTemplate1_H2HPage2Control1__ctl3_htmlTable tr:eq(1) td");
			//size = 3, win,draw,lose
			if(vslts.size() == 3) {
				map.put("vsWin", vslts.get(0).text());
				map.put("vsDraw", vslts.get(1).text());
				map.put("vsLose", vslts.get(2).text());
			}
			//size = 3, win,draw,lose
			if(vsHomeElts.size() == 3) {
				map.put("vsHomeWin", vsHomeElts.get(0).text());
				map.put("vsHomeDraw", vsHomeElts.get(1).text());
				map.put("vsHomeLose", vsHomeElts.get(2).text());
			}
		}
		return map;
	}
	
	/*
	matchElt.child(0); //賽事
	matchElt.child(1); //日期
	matchElt.child(2); //HOME/AWAY
	matchElt.child(3); //W/D/L
	matchElt.child(4); //對手
	matchElt.child(5); //賽果
	matchElt.child(6); //角球
	*/
	public List<FootballMatch> getAllMatchResults(FootballLeague league, FootballSeason season, FootballTeam team) {
		List<FootballMatch> list = new ArrayList<FootballMatch>();
		Map<String, Object> propertiesMap = new HashMap<String, Object>();
		Document doc = getJsoupTemplate().getDocumnetByAlias("url.team.stat", league.getId(), season.getSeasonId(), team.getId()) ;
		Elements matchElts = doc.select("#topMostLayoutTemplate_layoutTemplate_TeamResultControl1_htmlTable tr:not(#topMostLayoutTemplate_layoutTemplate_TeamResultControl1_header_row1)");
		if(log.isDebugEnabled()) log.debug("Team stat table: {}", matchElts);
		for(Element matchElt : matchElts) {
			if(matchElt.childNodeSize() > 0) {
				if(StringUtils.isEmpty(matchElt.child(5).text())) continue;
				FootballLeague matchLeague = FootballLeague.getByName(matchElt.child(0).text());
				propertiesMap.put("league", matchLeague == null ? FootballLeague.OTHERS : matchLeague);
				LocalDateTime matchDate = LocalDateTime.parse(String.format("%s 00:00", matchElt.child(1).text()), DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
				propertiesMap.put("matchDate", matchDate);
				propertiesMap.put("matchDay", StringUtils.upperCase(matchDate.format(DateTimeFormatter.ofPattern("EEE"))));
				List<String> result = RegexUtil.findSingleGroupList("(\\d+):(\\d+)\\((\\d+):(\\d+)\\)", matchElt.child(5).text());
				propertiesMap.put("result", FootballMatch.MATCH_RESULT.getByLabel(matchElt.child(3).text()).getCode());
				//propertiesMap.put("matchId", String.format("A%s-%s", matchDate.format(DateTimeFormatter.ofPattern("yyyyMMdd")), StringUtils.leftPad(team.getId(), 5, "0")));
				propertiesMap.put("status", FootballMatch.MATCH_STATUS.COMPLETED.getCode());
				propertiesMap.put("dateUpdated", LocalDateTime.now());
				
				if(FootballMatch.MATCH_AT.HOME.getLabel().equals(matchElt.child(2).text())) {
					propertiesMap.put("homeTeam", team.getName());
					propertiesMap.put("awayTeam", matchElt.child(4).text());
					if(result.size() > 0) {
						propertiesMap.put("homeScore", result.get(0));
						propertiesMap.put("awayScore", result.get(1));
						propertiesMap.put("homeHalfScore", result.get(2));
						propertiesMap.put("awayHalfScore", result.get(3));
					}
				} else {
					propertiesMap.put("homeTeam", matchElt.child(4).text());
					propertiesMap.put("awayTeam", team.getName());
					if(result.size() > 0) {
						propertiesMap.put("homeScore", result.get(1));
						propertiesMap.put("awayScore", result.get(0));
						propertiesMap.put("homeHalfScore", result.get(3));
						propertiesMap.put("awayHalfScore", result.get(2));
					}
				}			
				FootballMatch match = new FootballMatch();
				try {
					BeanUtils.populate(match, propertiesMap);
				} catch (IllegalAccessException | InvocationTargetException e) {
					log.debug("Error in populating FootballMatch", e);
				}
				
				list.add(match);
				log.info("FootballMatch: {}", match);
			}
		}
		return list;
	}
	
	public void getAllMatchResultsByTeam(String teamId, String leagueId, String seasonId) {
		
	}


}
