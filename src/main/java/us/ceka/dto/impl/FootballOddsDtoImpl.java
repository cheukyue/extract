package us.ceka.dto.impl;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Repository;

import us.ceka.domain.FootballOdds;
import us.ceka.dto.FootballOddsDto;
import us.ceka.extract.util.RegexUtil;

@Repository("footballOddsDto")
public class FootballOddsDtoImpl extends AbstractDtoJsoupImpl<FootballOdds> implements FootballOddsDto{
	public FootballOdds getLatestById(String matchId) {
		Document doc = getJsoupTemplate().getDocumnetByAlias("url.match.odd", matchId);
		Element titleNode = doc.select("a[title^=對賽往績]").first();
		List<String> teams = RegexUtil.findSingleGroupList("(.+)\\(主\\) 對 (.+)\\(客\\)", titleNode.text()); 
		log.info("{}", teams);

		//check if the match is already kicked off
		boolean kickoff = doc.getElementsByClass("nopoolmsg").size() > 0;
		if(kickoff) {
			log.info("Match {} is already kicked off", matchId);
			return null;
		}
		
		Element homeRate = doc.getElementById(String.format("%s_HAD_H", matchId));
		Element drawRate = doc.getElementById(String.format("%s_HAD_D", matchId));
		Element awayRate = doc.getElementById(String.format("%s_HAD_A", matchId));
		Element handicapLine = doc.select(String.format("#%s_HDC_HG label.lblGoal", matchId)).first();
		Element handicapHomeRate = doc.getElementById(String.format("%s_HDC_H", matchId));
		Element handicapAwayRate = doc.getElementById(String.format("%s_HDC_A", matchId));
		
		Map<String, Object> oddsPropMap = new HashMap<String, Object>();
		oddsPropMap.put("homeTeam", teams.get(0));
		oddsPropMap.put("awayTeam", teams.get(1));
		oddsPropMap.put("homeRate", homeRate == null ? -1 : homeRate.text());
		oddsPropMap.put("drawRate", drawRate == null ? -1 : drawRate.text());
		oddsPropMap.put("awayRate", awayRate == null ? -1 : awayRate.text());
		oddsPropMap.put("handicapLine", handicapLine == null ? null : handicapLine.text());
		oddsPropMap.put("handicapHomeRate", handicapHomeRate == null ? -1 : handicapHomeRate.text());
		oddsPropMap.put("handicapAwayRate", handicapAwayRate == null ? -1 : handicapAwayRate.text());
		oddsPropMap.put("dateCreated", LocalDateTime.now());
		
		FootballOdds odds = new FootballOdds(matchId, null);
		try {
			BeanUtils.populate(odds, oddsPropMap);
		} catch (IllegalAccessException | InvocationTargetException e) {
			log.error("Error on creating FootballMatch bean", e);
		}

		log.info("Latest odds: {}", odds);
		return odds;
	}
}
