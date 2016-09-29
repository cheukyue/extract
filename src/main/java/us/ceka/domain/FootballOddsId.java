package us.ceka.domain;

import java.io.Serializable;

import javax.persistence.Column;

public class FootballOddsId extends AbstractObject<FootballOddsId> implements Serializable{

	private static final long serialVersionUID = 8191674293373213281L;
	
	@Column(name="MATCH_ID", nullable=false)
	private String matchId;
	@Column(name="BATCH", nullable=false)
	private String batch ;
	
	public FootballOddsId() {};
	
	public FootballOddsId(String matchId, String batch) {
		this.matchId = matchId;
		this.batch = batch;
	}
	
	public String getMatchId() {
		return matchId;
	}
	public void setMatchId(String matchId) {
		this.matchId = matchId;
	}
	public String getBatch() {
		return batch;
	}
	public void setBatch(String batch) {
		this.batch = batch;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this == obj) return true;
		if(obj == null) return false;
		if(! (obj instanceof FootballOddsId)) return false;
		FootballOddsId id = (FootballOddsId)obj;
		if(id.getMatchId() == null  || id.getBatch() == null) return false;
		if(!id.getMatchId().equals(matchId)) return false;
		if(!id.getBatch().equals(batch)) return false;
		return true;
	}
	
	@Override
	public int hashCode() {
		int hashCode = 1;
		if(matchId != null) hashCode = 31 * hashCode + matchId.hashCode();
		if(batch != null) hashCode = 31 * hashCode + batch.hashCode();
		return hashCode;
	}
}
