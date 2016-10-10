package us.ceka.domain;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;

import us.ceka.util.StringListConverter;

@Entity
@NamedNativeQueries( {
	@NamedNativeQuery(       
		    name = "home_team_matchup",
		    query = "CALL home_team_matchup(:team, :league)",
		    resultClass = FootballMatchup.class
	),
	@NamedNativeQuery(       
		    name = "away_team_matchup",
		    query = "CALL away_team_matchup(:team, :league)",
		    resultClass = FootballMatchup.class
	)
})

public class FootballMatchup extends AbstractObject<FootballMatchup> implements Serializable{

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
	
	public int hashCode() {
		int hashCode = 1;
		if(team != null)  hashCode = 31 * hashCode + team.hashCode();
		if(season != null)  hashCode = 31 * hashCode + season.hashCode();
		hashCode = 31 * hashCode + teamTier;
		hashCode = 31 * hashCode + vsTier;
		hashCode = 31 * hashCode + result;
		
		return hashCode;
	}
	
	public boolean equals(Object obj) {
		if(this == obj) return true;
		if(obj == null) return false;
		if(!(obj instanceof FootballMatchup)) return false;
		FootballMatchup matchup = (FootballMatchup) obj;
		if(team == null || season == null) return false;
		if(!team.equals(team)) return false;
		if(!team.equals(season)) return false;
		if(teamTier != matchup.getTeamTier()) return false;
		if(vsTier != matchup.getVsTier()) return false;
		if(result != matchup.getResult()) return false;
		return true;
	}

}
