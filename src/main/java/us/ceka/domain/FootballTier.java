package us.ceka.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table (name="football_tier")
public class FootballTier extends AbstractObject<FootballTier> implements Serializable{

	private static final long serialVersionUID = 3715134928077347742L;
	
	@Id
	@Column (name="team")
	private String team;
	
	@Column (name="tier")
	private int tier;
	
	@Column (name="league")
	private String league; 
	
	public String getTeam() {
		return team;
	}
	public void setTeam(String team) {
		this.team = team;
	}
	public int getTier() {
		return tier;
	}
	public void setTier(int tier) {
		this.tier = tier;
	}
	public String getLeague() {
		return league;
	}
	public void setLeague(String league) {
		this.league = league;
	}

	

}
