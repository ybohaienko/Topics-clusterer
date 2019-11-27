package com.bohaienko.LR2.model;

public class VerificationData {
	private String header;
	private String actualTopic;
	private String classifiedTopic;
	private double classifiedProbability;
	private boolean isClassifiedCorrectly;

	public VerificationData(
			String header,
			String actualTopic,
			String classifiedTopic,
			double classifiedProbability,
			boolean isClassifiedCorrectly
	) {
		this.header = header;
		this.actualTopic = actualTopic;
		this.classifiedTopic = classifiedTopic;
		this.classifiedProbability = classifiedProbability;
		this.isClassifiedCorrectly = isClassifiedCorrectly;
	}

	public String getHeader() {
		return header;
	}

	public String getActualTopic() {
		return actualTopic;
	}

	public String getClassifiedTopic() {
		return classifiedTopic;
	}

	public double getClassifiedProbability() {
		return classifiedProbability;
	}

	public boolean isClassifiedCorrectly() {
		return isClassifiedCorrectly;
	}
}
