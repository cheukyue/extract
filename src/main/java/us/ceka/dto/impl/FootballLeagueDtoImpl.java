package us.ceka.dto.impl;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Repository;

import us.ceka.domain.FootballLeague;
import us.ceka.domain.FootballSeason;
import us.ceka.dto.FootballLeagueDto;

@Repository("footballLeagueDto")
public class FootballLeagueDtoImpl extends AbstractDtoJsoupImpl<FootballLeague> implements FootballLeagueDto{
	public List<FootballSeason> getAllSeasons(FootballLeague league) {
		
		Document doc = getJsoupTemplate().getDocumnetByAlias("url.league.current.table", league.getId());
		List<FootballSeason> list = new ArrayList<FootballSeason> ();
		Elements seasonElts = doc.select("#topMostLayoutTemplate_layoutTemplate_TeamHeader1_SeasonSelector1_seasonList option");
		if(log.isDebugEnabled()) log.debug("Elements retrived: {}", seasonElts);
		for(Element season : seasonElts) {
			if(season.attr("value") != null) {
				FootballSeason s = new FootballSeason();
				s.setSeasonId(season.attr("value"));
				s.setSeason(season.text());
				s.setLeague(league);
				s.setLeagueName(league.getName());
				log.info("FootballSeason: {}", s);
				list.add(s);
			}
		}
		return list;
	}

}
