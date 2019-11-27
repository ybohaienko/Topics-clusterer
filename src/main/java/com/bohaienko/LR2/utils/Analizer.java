package com.bohaienko.LR2.utils;

import com.bohaienko.LR2.model.Dictionary;
import com.bohaienko.LR2.model.Probability;
import com.bohaienko.LR2.model.VerificationData;
import com.uttesh.exude.exception.InvalidDataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class Analizer {

	@Autowired
	Parser parser;

	private List<Dictionary> dictionaries;
	private List<Probability> probabilities;
	private List<VerificationData> data;
	private Map<String, Double> probabilityValues;

	public List<Dictionary> buildDictionary(String[] topics, List<List<String>> wordLists)
			throws InvalidDataException {
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
		probabilities = new ArrayList<>();
		usages.forEach(entry -> {
			calculateDenormProbability(entry.getUsage());
			probabilities.add(new Probability(
					entry.getWord(),
					calculateDenormProbability(entry.getUsage()))
			);
		});
		return probabilities;
	}

	public List<Probability> getNormalizedProbability(List<Dictionary> wordUsages, List<Probability> denormProbablities) {
		probabilities = new ArrayList<>();
		wordUsages.forEach(entry -> {
			Map<String, Double> denormProbabilityOfTopics = denormProbablities.stream()
					.filter(e -> e.getWord().equals(entry.getWord()))
					.findFirst().get().getProbability();
			probabilities.add(new Probability(
					entry.getWord(),
					calculateNormalizedProbability(
							entry.getUsage(),
							denormProbabilityOfTopics
					))
			);
		});
		return probabilities;
	}

	public List<VerificationData> varifyClassification(List<List<String>> testSet, String[] topics, List<Probability> normalizedProbabilities) {
		data = new ArrayList<>();
		System.out.println(testSet);

		for (int i = 0; i < topics.length; i++) {
			int finalI = i;
			testSet.get(i).forEach(e -> {
				System.out.println(e);
				List<String> words = parser.getWordListOfText(e);
				final String[] clasiffiedTopic = {null};
				final double[] classifiedProbability = {0};
				AtomicBoolean isClassifiedCorrectly = new AtomicBoolean(false);

				Arrays.asList(topics).forEach(topic -> {
					List<Double> topicProb = new ArrayList<>();
					words.forEach(word -> {
						if (normalizedProbabilities.stream().anyMatch(prob -> prob.getWord().equals(word))) {
							Map<String, Double> wordProb = normalizedProbabilities.stream().filter(prob -> prob.getWord().equals(word)).findFirst().get().getProbability();
							topicProb.add(wordProb.get(topic));
						}
					});
					double multProb = topicProb.stream().reduce(0.5, (a, b) -> a * b);
					System.out.println(topicProb);
					System.out.println(topicProb.isEmpty());

					if (multProb > classifiedProbability[0]) {
						classifiedProbability[0] = multProb;
						clasiffiedTopic[0] = topic;
					}

					if (topicProb.isEmpty()) {
						classifiedProbability[0] = 0.0;
						clasiffiedTopic[0] = "Topic was not classified";
					}
					isClassifiedCorrectly.set(topics[finalI].equals(clasiffiedTopic[0]));
				});
				data.add(new VerificationData(e, topics[finalI], clasiffiedTopic[0], classifiedProbability[0], isClassifiedCorrectly.get()));
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
			double nWord = (double) usage;
			double pClass = 1 / (double) topicUsage.size();
			double pNorm = (nWord * pProb + pClass) / (nWord + 1);
			probabilityValues.put(k, pNorm);
		});
		return probabilityValues;
	}
}
