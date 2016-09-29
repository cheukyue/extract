package us.ceka.domain;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;

import us.ceka.extract.util.StringListConverter;

@Entity
@NamedNativeQueries( {
	@NamedNativeQuery(       
		    name = "home_team_matchup",
		    query = "CALL home_team_matchup(:team, :league)",
		    resultClass = FootballTeamMatchup.class
	),
	@NamedNativeQuery(       
		    name = "away_team_matchup",
		    query = "CALL away_team_matchup(:team, :league)",
		    resultClass = FootballTeamMatchup.class
	)
})

public class FootballTeamMatchup extends AbstractObject<FootballTeamMatchup> implements Serializable{

	private static final long serialVersionUID = -7488070164437616687L;
	
	@Id
	@Column(name="team")
	private String team;
	@Id
	@Column(name="team_tier")
	private int teamTier;
	@Id
	@Column(name="vs_tier")
	private int vsTier;
	@Id
	@Column(name="season")
	private String season;
	@Id
	@Column(name="result")
	private char result;
	
	@Column(name="match_count")
	private int numMatches;
	
	@Column(name="vsTeam")
	@Convert(converter=StringListConverter.class)
	private List<String> vsTeamList;
	
	@Column(name="goalFor")
	@Convert(converter=StringListConverter.class)
	private List<String> goalForList;
	
	@Column(name="goalAgainst")
	@Convert(converter=StringListConverter.class)
	private List<String> goalAgainstList;

	public String getSeason() {
		return season;
	}
	public void setSeason(String season) {
		this.season = season;
	}
	public char getResult() {
		return result;
	}
	public void setResult(char result) {
		this.result = result;
	}
	public int getNumMatches() {
		return numMatches;
	}
	public void setNumMatches(int numMatches) {
		this.numMatches = numMatches;
	}
	public String getTeam() {
		return team;
	}
	public void setTeam(String team) {
		this.team = team;
	}
	public int getTeamTier() {
		return teamTier;
	}
	public void setTeamTier(int teamTier) {
		this.teamTier = teamTier;
	}
	public int getVsTier() {
		return vsTier;
	}
	public void setVsTier(int vsTier) {
		this.vsTier = vsTier;
	}
	public List<String> getVsTeamList() {
		return vsTeamList;
	}
	public void setVsTeamList(List<String> vsTeamList) {
		this.vsTeamList = vsTeamList;
	}
	public List<String> getGoalForList() {
		return goalForList;
	}
	public void setGoalForList(List<String> goalForList) {
		this.goalForList = goalForList;
	}
	public List<String> getGoalAgainstList() {
		return goalAgainstList;
	}
	public void setGoalAgainstList(List<String> goalAgainstList) {
		this.goalAgainstList = goalAgainstList;
	}

}
