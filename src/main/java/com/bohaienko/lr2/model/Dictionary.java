package com.bohaienko.lr2.model;

import java.util.Map;

public class Dictionary {
	private String word;
	private Map<String, Integer> wordUsage;

	public Dictionary(String word, Map<String, Integer> topicUsage) {
		this.word = word;
		this.wordUsage = topicUsage;
	}

	public String getWord() {
		return word;
	}

	public Map<String, Integer> getUsage() {
		return wordUsage;
	}
}
