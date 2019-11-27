package com.bohaienko.LR2.utils;

import com.bohaienko.LR2.model.Dictionary;
import com.uttesh.exude.ExudeData;
import com.uttesh.exude.exception.InvalidDataException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class Parser {
	public List<String> filterWords(List<String> words, String topic) {
		String[] topicWords = topic.split("\\s+");
		return words.stream()
				.filter(word -> word.length() > 3)
				.filter(word -> !Arrays.asList(topicWords).contains(word))
				.collect(Collectors.toList());
	}

	public List<String> getWordListOfTextList(List<String> texts)  {
		List<String> result = null;
		try {
			result = Arrays.asList(ExudeData.getInstance().filterStoppingsKeepDuplicates(texts.toString()).split("\\W+"));
		} catch (InvalidDataException e) {
			e.printStackTrace();
		}
		return result;
	}

	public List<String> getWordListOfText(String text) {
		List<String> result = null;
		try {
			result = Arrays.asList(ExudeData.getInstance().filterStoppingsKeepDuplicates(text).split("\\W+"));
		} catch (InvalidDataException e) {
			e.printStackTrace();
		}
		return result;
	}

	public List<Dictionary> getFilledUsageStatistics(List<Dictionary> statistics, String[] topics) {
		statistics
				.forEach(entry -> Arrays.asList(topics)
						.forEach(topic -> entry.getUsage().putIfAbsent(topic, 0)));
		return statistics;
	}

	public List<List<String>> getTrainingSet(List<List<String>> set) {
		List<List<String>> trainingSet = new ArrayList<>();
		set.forEach(e -> trainingSet.add(e.subList(0, (int) (e.size() * 0.7))));
		return trainingSet;
	}

	public List<List<String>> getTestSet(List<List<String>> set) {
		List<List<String>> trainingSet = new ArrayList<>();
		set.forEach(e -> trainingSet.add(e.subList((int) (e.size() - e.size() * 0.3), e.size())));
		return trainingSet;
	}
}
