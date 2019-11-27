package com.bohaienko.lr2.model;

public class VerificationData {
	private final String header;
	private final String actualTopic;
	private final String classificatedTopic;
	private final double classificatedProbability;
	private final boolean isClassificatedCorrectly;

	public VerificationData(
			String header,
			String actualTopic,
			String classificatedTopic,
			double classificatedProbability,
			boolean isClassificatedCorrectly
	) {
		this.header = header;
		this.actualTopic = actualTopic;
		this.classificatedTopic = classificatedTopic;
		this.classificatedProbability = classificatedProbability;
		this.isClassificatedCorrectly = isClassificatedCorrectly;
	}

	public String getHeader() {
		return header;
	}

	public String getActualTopic() {
		return actualTopic;
	}

	public String getClassificatedTopic() {
		return classificatedTopic;
	}

	public double getClassificatedProbability() {
		return classificatedProbability;
	}

	public boolean isClassificatedCorrectly() {
		return isClassificatedCorrectly;
	}
}
