package com.bohaienko.LR2.utils;

import com.bohaienko.LR2.model.WordUsageStatistic;
import com.uttesh.exude.ExudeData;
import com.uttesh.exude.exception.InvalidDataException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class Parser {
	public List<String> filterWords(List<String> words, String topic) {
		String[] topicWords = topic.split("\\s+");
		return words.stream()
				.filter(w -> w.length() > 3)
				.filter(w -> !Arrays.asList(topicWords).contains(w))
				.collect(Collectors.toList());
	}

	public List<String> getWordListOfTextList(List<String> texts) throws InvalidDataException {
		return Arrays.asList(ExudeData.getInstance().filterStoppingsKeepDuplicates(texts.toString()).split("\\W+"));
	}

	public List<WordUsageStatistic> getFilledUsageStatistics(List<WordUsageStatistic> statistics, String[] topics) {
		statistics.forEach(e -> {
			for (String topic : topics) {
				e.getWordUsage().putIfAbsent(topic, 0);
			}
		});
		return statistics;
	}
}
