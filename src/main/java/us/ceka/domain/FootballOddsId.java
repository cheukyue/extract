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
}
