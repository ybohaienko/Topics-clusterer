package com.bohaienko.LR2.model;

import java.util.Map;

public class ClassProbabilityStatistic {
	private String word;
	private Map<String, Double> classProbability;

	public ClassProbabilityStatistic(String word, Map<String, Double> topicUsage) {
		this.word = word;
		this.classProbability = topicUsage;
	}

	public String getWord() {
		return word;
	}

	public Map<String, Double> getClassProbability() {
		return classProbability;
	}

	@Override
	public String toString() {
		return word + " | " + classProbability.entrySet() + "\n";
	}
}
