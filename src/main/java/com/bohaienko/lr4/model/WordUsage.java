package com.bohaienko.lr4.model;

public class WordUsage {
	private String word;
	private int wordUsage;

	public WordUsage(String word, int topicUsage) {
		this.word = word;
		this.wordUsage = topicUsage;
	}

	public String getWord() {
		return word;
	}

	public int getWordUsage() {
		return wordUsage;
	}

	public void setWordUsage(int wordUsage) {
		this.wordUsage = wordUsage;
	}
}
