//package com.bohaienko.LR2.model;
//
//import java.util.Map;
//
//public class Statistic {
//	private String word;
//	private Map<String, Double> classProbability;
//	private Map<String, Integer> topicUsage;
//
//	public Statistic(String word, Map<String, Integer> topicUsage) {
//		this.word = word;
//		this.topicUsage = topicUsage;
//	}
//
//	public Statistic(String word, Map<String, Double> classProbability) {
//		this.word = word;
//		this.classProbability = classProbability;
//	}
//
//	public String getWord() {
//		return word;
//	}
//
//	public void setWord(String word) {
//		this.word = word;
//	}
//
//	public Map<String, Integer> getWordUsage() {
//		return topicUsage;
//	}
//
//	public void setTopicUsage(Map<String, Integer> topicUsage) {
//		this.topicUsage = topicUsage;
//	}
//
//	public Map<String, Double> getClassProbability() {
//		return classProbability;
//	}
//
//	public void setClassProbability(Map<String, Double> classProbability) {
//		this.classProbability = classProbability;
//	}
//}
