package us.ceka.domain;

public enum FootballLeague {
	ENG_PREMIER_LEAGUE ("cfEPL", "英超", "1", TYPE.LEAGUE),
	ENG_FA_CUP ("cfEFA", "英足盃", "5", TYPE.CUP),
	ENG_LEAGUE_CUP ("cfELC", "英聯盃", "8", TYPE.CUP),
	ITA_SERIE_A ("cfISA", "意甲", "2", TYPE.LEAGUE),
	ARG_DIVISION_1 ("cfAPL", "阿甲", "1126", TYPE.LEAGUE),
	EUR_CHAMPIONSHIP("cfUCL", "歐聯", "6", TYPE.CUP),
	EUR_EUROPA("cfUEC", "歐霸", "7", TYPE.CUP),
	GER_BUNDESLIGA("cfGSL", "德甲", "3", TYPE.LEAGUE),
	ESP_LALIGA("cfSFL", "西甲", "4", TYPE.LEAGUE),
	JPN_JLEAGUE("cfJD1", "日聯", "1103", TYPE.LEAGUE),
	OTHERS("others", "其他", "0", TYPE.OTHERS)
	;
	public enum TYPE {LEAGUE, CUP, OTHERS};
	private String code;
	private String name;
	private String id;
	private TYPE type;
	
	FootballLeague(String code, String name, String number, TYPE type) {
		this.code = code;
		this.name = name;
		this.id = number;
		this.type = type;
	}
	
	public static FootballLeague getByCode(String code) {
		for(FootballLeague l : FootballLeague.values()) 
			if(l.getCode().equals(code)) return l;
		return null;
	}
	public static FootballLeague getById(String id) {
		for(FootballLeague l : FootballLeague.values())
			if(l.getId().equals(id)) return l;
		return null;
	}
	
	public static FootballLeague getByName(String name) {
		for(FootballLeague l : FootballLeague.values()) 
			if(l.getName().equals(name)) return l;
		return null;		
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public TYPE getType() {
		return type;
	}

	public void setType(TYPE type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return this.name;
	}
}
