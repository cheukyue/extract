package us.ceka.domain;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.StringUtils;

@Entity
@Table(name="football_odds")
public class FootballOdds extends AbstractObject<FootballOdds> implements Serializable{

	private static final long serialVersionUID = -1517608380196931242L;
	
	public FootballOdds() {}

	@EmbeddedId
	private FootballOddsId footballOddsId;

	@Size(max=60)
	@Column(name="HOME_TEAM", nullable=false)
	private String homeTeam;

	@Size(max=60)
	@Column(name="AWAY_TEAM", nullable=false)
	private String awayTeam;

	@Digits(integer=7, fraction=3)
	@Column(name="HOME_RATE", nullable=false)
	private BigDecimal homeRate;

	@Digits(integer=7, fraction=3)
	@Column(name="AWAY_RATE", nullable=false)
	private BigDecimal awayRate;

	@Digits(integer=7, fraction=3)
	@Column(name="DRAW_RATE", nullable=false)
	private BigDecimal drawRate;
	
	@Column(name="HANDICAP_LINE")
	private String handicapLine;
	
	@Digits(integer=7, fraction=3)
	@Column(name="HAND_HOME_RATE")
	private BigDecimal handicapHomeRate;
	
	@Digits(integer=7, fraction=3)
	@Column(name="HAND_AWAY_RATE")
	private BigDecimal handicapAwayRate;

	@Column(name="DATE_CREATED", nullable=false)
	private LocalDateTime dateCreated;



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

	public BigDecimal getHomeRate() {
		return homeRate;
	}

	public void setHomeRate(BigDecimal homeRate) {
		this.homeRate = homeRate;
	}

	public BigDecimal getAwayRate() {
		return awayRate;
	}

	public void setAwayRate(BigDecimal awayRate) {
		this.awayRate = awayRate;
	}

	public BigDecimal getDrawRate() {
		return drawRate;
	}

	public void setDrawRate(BigDecimal drawRate) {
		this.drawRate = drawRate;
	}

	public String getHandicapLine() {
		return handicapLine;
	}

	public void setHandicapLine(String handicapLine) {
		this.handicapLine = handicapLine;
	}

	public BigDecimal getHandicapHomeRate() {
		return handicapHomeRate;
	}

	public void setHandicapHomeRate(BigDecimal handicapHomeRate) {
		this.handicapHomeRate = handicapHomeRate;
	}

	public BigDecimal getHandicapAwayRate() {
		return handicapAwayRate;
	}

	public void setHandicapAwayRate(BigDecimal handicapAwayRate) {
		this.handicapAwayRate = handicapAwayRate;
	}

	public LocalDateTime getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(LocalDateTime dateCreated) {
		this.dateCreated = dateCreated;
	}

	@Override
	public int hashCode() {
		int hashCode = 1;
		if(footballOddsId.getMatchId() != null) hashCode = 31 * hashCode + footballOddsId.getMatchId().hashCode();
		if(footballOddsId.getBatch() != null) hashCode = 31 * hashCode + footballOddsId.getBatch().hashCode();
		return hashCode;
	}

	@Override
	public boolean equals(Object obj) {
		if(this == obj) return true;
		if(obj == null) return false;
		if(!(obj instanceof FootballOdds)) return false;
		FootballOdds odds = (FootballOdds)obj;
		if(odds.footballOddsId.getMatchId() != null) {
			if(!odds.footballOddsId.getMatchId().equals(footballOddsId.getMatchId())) return false;
		}
		if(odds.footballOddsId.getBatch() != null) {
			if(odds.footballOddsId.getBatch().equals(footballOddsId.getBatch())) return false;
		}
		return true;
	}
	
	public boolean rateEquals(Object obj) {
		if(this == obj) return true;
		if(obj == null) return false;
		if(!(obj instanceof FootballOdds)) return false;
		
		for(Field f : obj.getClass().getDeclaredFields()) {
			if(StringUtils.endsWith(f.getName(), "Rate")) {
				try {
					if(f.get(obj) instanceof BigDecimal) {
						BigDecimal n = (BigDecimal)f.get(obj);
						if(n != null)
							if(n.compareTo((BigDecimal)f.get(this)) != 0) return false ;
					}
				} catch (IllegalArgumentException e) {
					return false;
				} catch (IllegalAccessException e) {
					return false;
				}
			}
		}
		return true;
	}
	
	public FootballOdds(String matchId, String batch) {
		this.footballOddsId = new FootballOddsId(matchId, batch);
	}

	public FootballOddsId getFootballOddsId() {
		return footballOddsId;
	}

	public void setFootballOddsId(FootballOddsId footballOddsId) {
		this.footballOddsId = footballOddsId;
	}
	
/*
	@Override
	public String toString() {
		return String.format("FootballOdds [matchId=%s, batch=%s, homeTeam=%s, awayTeam=%s, homeRate=%2f, drawRate=%2f, awayRate=%2f, handicapLine=%s, handdicapHomeRate=%2f, handicapAwayRate=%2f, dateCreated=%s]", 
				this.footballOddsId.getMatchId(), this.footballOddsId.getBatch(), this.homeTeam, this.awayTeam, this.homeRate, this.drawRate, this.awayRate, this.handicapLine, this.handicapHomeRate, this.handicapAwayRate, this.dateCreated);

	}
	*/
}
