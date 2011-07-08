package com.mscg.emule.bean;

public class SpeedBean {

	private String speedText;
	private double speedPerc;

	public SpeedBean(String speedText, double speedPerc) {
		setSpeedText(speedText);
		setSpeedPerc(speedPerc);
	}

	public String getSpeedText() {
		return speedText;
	}

	public void setSpeedText(String speedText) {
		this.speedText = speedText;
	}

	public double getSpeedPerc() {
		return speedPerc;
	}

	public void setSpeedPerc(double speedPerc) {
		this.speedPerc = speedPerc;
	}
}
