package us.ceka.domain;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="football_match")
public class FootballMatch extends AbstractObject<FootballMatch> implements Serializable{

	private static final long serialVersionUID = 2506737592700232440L;
	
	public enum MATCH_STATUS {
		COMPLETED("C"), RESCHEDULED("R"), PENDING("P");
		private String code;
		private MATCH_STATUS(String code) {this.code = code;}
		public String getCode() {return code;}
	}
	
	public enum MATCH_REMARK {
		CANCELLED("球賽無效");
		private String message;
		private MATCH_REMARK(String message) {this.message = message;}
		public String getMessage() {return message;}
	}
	public enum MATCH_RESULT {
		WIN('W', "勝"), DRAW('D', "和"), LOSE('L', "負"), CANCEL('-', "-");
		private char code;
		private String label;
		private MATCH_RESULT(char code, String label) {this.code = code; this.label = label;}
		public char getCode() {return code;}
		public String getLabel() {return label;}
		public static MATCH_RESULT getByLabel(String label) {
			for(MATCH_RESULT m : MATCH_RESULT.values()) if(m.getLabel().equals(label)) return m;
			return CANCEL;
		}
	}
	public enum MATCH_AT {
		HOME("主"), AWAY("客"), NEUTRAL("中");
		private String label;
		private MATCH_AT(String label) {this.label =  label;}
		public String getLabel() {return label;}
	}

	@Id
	@Column(name="MATCH_ID", nullable=false)
	private String matchId;
	
	@Column(name="MATCH_DATE", nullable=false)
	private LocalDateTime matchDate;
	
	@Column(name="MATCH_DAY", nullable=false)
	private String matchDay;
	
	@Column(name="MATCH_NUM", nullable=false)
	private int matchNum;
	
	@Enumerated(EnumType.STRING)
	@Column(name="LEAGUE", nullable=false)
	private FootballLeague league;
	
	@Column(name="HOME_TEAM", nullable=false)
	private String homeTeam;
	
	@Column(name="AWAY_TEAM", nullable=false)
	private String awayTeam;
	
	@Column(name="RESULT")
	private char result;
	
	@Column(name="HOME_SCORE")
	private int homeScore;
	
	@Column(name="AWAY_SCORE")
	private int awayScore;
	
	@Column(name="HOME_HALF_SCORE")
	private int homeHalfScore;
	
	@Column(name="AWAY_HALF_SCORE")
	private int awayHalfScore;
	
	@Column(name="status")
	private String status;
	
	@Column (name="DATE_UPDATED")
	private LocalDateTime dateUpdated;
	
	public FootballMatch() {}
	
	public FootballMatch(String matchId) {
		this.matchId = matchId;
	}

	public String getMatchId() {
		return matchId;
	}

	public void setMatchId(String matchId) {
		this.matchId = matchId;
	}

	public LocalDateTime getMatchDate() {
		return matchDate;
	}

	public void setMatchDate(LocalDateTime matchDate) {
		this.matchDate = matchDate;
	}

	public String getMatchDay() {
		return matchDay;
	}

	public void setMatchDay(String matchDay) {
		this.matchDay = matchDay;
	}

	public int getMatchNum() {
		return matchNum;
	}

	public void setMatchNum(int matchNum) {
		this.matchNum = matchNum;
	}

	public FootballLeague getLeague() {
		return league;
	}

	public void setLeague(FootballLeague league) {
		this.league = league;
	}

	public String getHomeTeam() {
		return homeTeam;
	}

	public void setHomeTeam(String homeTeam) {
		this.homeTeam = homeTeam;
	}

	public String getAwayTeam() {
		return awayTeam;
	}

	public void setAwayTeam(String awayTeam) {
		this.awayTeam = awayTeam;
	}

	public char getResult() {
		return result;
	}

	public void setResult(char result) {
		this.result = result;
	}

	public int getHomeScore() {
		return homeScore;
	}

	public void setHomeScore(int homeScore) {
		this.homeScore = homeScore;
	}

	public int getAwayScore() {
		return awayScore;
	}

	public void setAwayScore(int awayScore) {
		this.awayScore = awayScore;
	}

	public int getHomeHalfScore() {
		return homeHalfScore;
	}

	public void setHomeHalfScore(int homeHalfScore) {
		this.homeHalfScore = homeHalfScore;
	}

	public int getAwayHalfScore() {
		return awayHalfScore;
	}

	public void setAwayHalfScore(int awayHalfScore) {
		this.awayHalfScore = awayHalfScore;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public LocalDateTime getDateUpdated() {
		return dateUpdated;
	}

	public void setDateUpdated(LocalDateTime dateUpdated) {
		this.dateUpdated = dateUpdated;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this == obj) return true;
		if(obj == null) return false;
		if(!(obj instanceof FootballMatch)) return false;
		FootballMatch match = (FootballMatch)obj;
		if(homeTeam == null || awayTeam == null || matchDate == null || match.getMatchDate() == null) return false;
		//if(!matchId.equals(match.getMatchId())) return false;
		if(!homeTeam.equals(match.getHomeTeam())) return false;
		if(!awayTeam.equals(match.getAwayTeam())) return false;
		if(!matchDate.truncatedTo(ChronoUnit.DAYS).isEqual(match.getMatchDate().truncatedTo(ChronoUnit.DAYS))) return false;
		
		return true;
	}
	
	@Override
	public int hashCode() {
		int hashCode = 1;
		if(homeTeam != null)  hashCode = 31 * hashCode + homeTeam.hashCode();
		if(awayTeam != null)  hashCode = 31 * hashCode + awayTeam.hashCode();
		if(matchDate != null)  hashCode = 31 * hashCode + matchDate.truncatedTo(ChronoUnit.DAYS).hashCode();
		return hashCode;
	}
	
	/*
	@Override
	public String toString() {
		return String.format("FootballMatch [matchId=%s, matchDate=%s, league=%s, homeTeam=%s, awayTeam=%s, result=%s, homeScore=%d, awayScore=%d]", 
			this.matchId, this.matchDate, this.league, this.homeTeam, this.awayTeam, this.result, this.homeScore, this.awayScore);
	}
	*/
}
