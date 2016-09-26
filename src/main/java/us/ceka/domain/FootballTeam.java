package us.ceka.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="football_team")
public class FootballTeam extends AbstractObject<FootballTeam> implements Serializable{

	private static final long serialVersionUID = -8956071120958880744L;
	
	@Id
	@Column(name="team_id")
	private String id;
	
	@Column(name="team_name", nullable=false)
	private String name;
	
	@Column(name="team_name_eng")
	private String engName;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEngName() {
		return engName;
	}

	public void setEngName(String engName) {
		this.engName = engName;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj == null) return false;
		if(obj == this) return true;
		FootballTeam team = (FootballTeam)obj;
		if(team.getName() == null) return false;
		if(!team.getName().equals(name)) return false;
		
		return true;
	}
	
	@Override
	public int hashCode() {
		int hashCode = 1;
		if(name != null ) hashCode = hashCode * 31 + name.hashCode();
		return hashCode;
	}
	
}
