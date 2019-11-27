package com.bohaienko.lr2.utils;

import com.bohaienko.lr2.model.Dictionary;
import com.bohaienko.lr2.model.Probability;
import com.bohaienko.lr2.model.VerificationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Service
public class Analyzer {

	@Autowired
	Parser parser;

	private List<Dictionary> dictionaries;
	private List<VerificationData> data;
	private Map<String, Double> probabilityValues;

	public List<Dictionary> buildDictionary(String[] topics, List<List<String>> wordLists) {
		dictionaries = new ArrayList<>();
		for (int i = 0; i < topics.length; i++) {
			int finalI = i;
			String topic = topics[finalI];
			//INFO: Filter words that less than 3 chars and 'topic' words for classification cleanliness
			List<String> words = parser.filterWords(parser.getWordListOfTextList(wordLists.get(finalI)), topic);
			words.forEach(word -> {
				if (dictionaries.stream().noneMatch(s -> s.getWord().equals(word))) {
					dictionaries.add(new Dictionary(word, new HashMap<String, Integer>() {{
						put(topics[finalI], 1);
					}}));
				} else {
					dictionaries.stream().filter(s -> s.getWord().equals(word)).forEach(e -> {
						Map<String, Integer> usage = e.getUsage();
						usage.merge(topics[finalI], 1, Integer::sum);
					});
				}
			});
		}
		return parser.getFilledUsageStatistics(dictionaries, topics);
	}

	public List<Probability> getDenormalizedProbability(List<Dictionary> usages) {
		return usages
				.stream()
				.map(entry -> new Probability(entry.getWord(), calculateDenormProbability(entry.getUsage())))
				.collect(Collectors.toList());
	}

	public List<Probability> getNormalizedProbability(List<Dictionary> wordUsages, List<Probability> denormProbabilities) {
		return wordUsages
				.stream()
				.map(entry -> new Probability(
						entry.getWord(),
						calculateNormalizedProbability(
								entry.getUsage(),
								denormProbabilities
										.stream()
										.filter(e -> e.getWord().equals(entry.getWord()))
										.findFirst().get().getProbability()
						)
				))
				.collect(Collectors.toList());
	}

	public List<VerificationData> verifyClassification(List<List<String>> testSet, String[] topics, List<Probability> normalizedProbabilities) {
		data = new ArrayList<>();
		System.out.println(testSet);

		for (int i = 0; i < topics.length; i++) {
			int finalI = i;
			testSet.get(i).forEach(e -> {
				System.out.println(e);
				List<String> words = parser.getWordListOfText(e);
				final String[] classificatedTopic = {null};
				final double[] classificatedProbability = {0};
				AtomicBoolean isClassificatedCorrectly = new AtomicBoolean(false);

				Arrays.asList(topics).forEach(topic -> {
					List<Double> topicProb = new ArrayList<>();
					words.forEach(word -> {
						if (normalizedProbabilities.stream().anyMatch(prob -> prob.getWord().equals(word))) {
							Map<String, Double> wordProb = normalizedProbabilities
									.stream()
									.filter(prob -> prob.getWord().equals(word)).findFirst().get().getProbability();
							topicProb.add(wordProb.get(topic));
						}
					});
					double multipliedProb = topicProb.stream().reduce(0.5, (a, b) -> a * b);

					if (multipliedProb > classificatedProbability[0]) {
						classificatedProbability[0] = multipliedProb;
						classificatedTopic[0] = topic;
					}

					if (topicProb.isEmpty()) {
						classificatedProbability[0] = 0.0;
						classificatedTopic[0] = "Topic was not classified";
					}
					isClassificatedCorrectly.set(topics[finalI].equals(classificatedTopic[0]));
				});
				data.add(new VerificationData(e, topics[finalI], classificatedTopic[0], classificatedProbability[0], isClassificatedCorrectly.get()));
			});
		}
		return data;
	}

	private Map<String, Double> calculateDenormProbability(Map<String, Integer> topicUsage) {
		probabilityValues = new HashMap<>();
		double classesNum = topicUsage.values().stream().mapToInt(Integer::intValue).sum();
		topicUsage.forEach((k, v) -> probabilityValues.put(k, (double) v / classesNum));
		return probabilityValues;
	}

	private Map<String, Double> calculateNormalizedProbability(Map<String, Integer> topicUsage, Map<String, Double> classProbability) {
		probabilityValues = new HashMap<>();
		int usage = topicUsage.values().stream().reduce(0, Integer::sum);
		topicUsage.forEach((k, v) -> {
			double pProb = classProbability.get(k);
			double pClass = 1 / (double) topicUsage.size();
			double pNorm = (usage * pProb + pClass) / (usage + 1);
			probabilityValues.put(k, pNorm);
		});
		return probabilityValues;
	}
}
