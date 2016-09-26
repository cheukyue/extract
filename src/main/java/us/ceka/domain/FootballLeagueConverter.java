package us.ceka.domain;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class FootballLeagueConverter implements AttributeConverter<FootballLeague, String> {

	@Override
	public String convertToDatabaseColumn(FootballLeague league) {
		return league.getId();
	}

	@Override
	public FootballLeague convertToEntityAttribute(String leagueId) {
		return FootballLeague.getById(leagueId);
	}

}
