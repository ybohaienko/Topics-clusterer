package com.bohaienko.lr4.model;

public class WordAttributes {
	private String word;
	private int numOfLettersBeforeWord;
	private int numOfLettersInWord;

	public WordAttributes(String word, int numOfLettersBeforeWord, int numOfLettersInWord) {
		this.word = word;
		this.numOfLettersBeforeWord = numOfLettersBeforeWord;
		this.numOfLettersInWord = numOfLettersInWord;
	}

	public String getWord() {
		return word;
	}

	public int getNumOfLettersBeforeWord() {
		return numOfLettersBeforeWord;
	}

	public int getNumOfLettersInWord() {
		return numOfLettersInWord;
	}
}
