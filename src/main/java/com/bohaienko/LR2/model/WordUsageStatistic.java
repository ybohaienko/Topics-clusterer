package com.bohaienko.LR2.model;

import java.util.Map;

public class WordUsageStatistic {
	private String word;
	private Map<String, Integer> wordUsage;

	public WordUsageStatistic(String word, Map<String, Integer> topicUsage) {
		this.word = word;
		this.wordUsage = topicUsage;
	}

	public String getWord() {
		return word;
	}

	public Map<String, Integer> getWordUsage() {
		return wordUsage;
	}
}
