package us.ceka.domain;

public enum Analytics {
	DOUBT_RATE(2.0),
	BIG_RATE_CHANGE(0.03),
	HIGH_WIN_POSSIBILITY(0.7);
	
	private double value;
	
	public double getValue() {
		return value;
	}

	Analytics(double value) {
		this.value = value;
	}
}
