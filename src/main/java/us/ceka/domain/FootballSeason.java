package us.ceka.domain;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="football_season")
public class FootballSeason extends AbstractObject<FootballSeason> implements Serializable{

	private static final long serialVersionUID = -4398738267182339700L;

	@Id
	@Column(name="SEASON_ID", nullable=false)
	private String seasonId;
	
	@Column(name="SEASON", nullable=false)
	private String season;
	
	@Basic
	@Column(name="LEAGUE_ID")
	@Convert( converter=FootballLeagueConverter.class )
	private FootballLeague league;
	
	@Column(name="LEAGUE_NAME")
	private String leagueName;
	
	public FootballSeason() {}

	public String getSeasonId() {
		return seasonId;
	}

	public void setSeasonId(String seasonId) {
		this.seasonId = seasonId;
	}

	public String getSeason() {
		return season;
	}

	public void setSeason(String season) {
		this.season = season;
	}

	public FootballLeague getLeague() {
		return league;
	}

	public void setLeague(FootballLeague league) {
		this.league = league;
	}

	public String getLeagueName() {
		return leagueName;
	}

	public void setLeagueName(String leagueName) {
		this.leagueName = leagueName;
	}
}
