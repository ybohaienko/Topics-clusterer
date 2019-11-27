package com.bohaienko.LR2.utils;

import com.bohaienko.LR2.model.Probability;
import com.bohaienko.LR2.model.Dictionary;
import com.uttesh.exude.exception.InvalidDataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class Analizer {

	@Autowired
	Parser parser;

	public List<Dictionary> supplyDictionary(String[] topics, List<List<String>> wordLists)
			throws InvalidDataException {
		List<Dictionary> usageStatistics = new ArrayList<>();
		for (int i = 0; i < topics.length; i++) {
			int finalI = i;
			String topic = topics[finalI];
			//INFO: Filter words that less than 3 chars and 'topic' words for classification cleanliness
			List<String> words = parser.filterWords(parser.getWordListOfTextList(wordLists.get(finalI)), topic);
			words.forEach(word -> {
				if (usageStatistics.stream().noneMatch(s -> s.getWord().equals(word))) {
					usageStatistics.add(new Dictionary(word, new HashMap<String, Integer>() {{
						put(topics[finalI], 1);
					}}));
				} else {
					usageStatistics.stream().filter(s -> s.getWord().equals(word)).forEach(e -> {
						Map<String, Integer> usage = e.getWordUsage();
						usage.merge(topics[finalI], 1, Integer::sum);
					});
				}
			});
		}
		return parser.getFilledUsageStatistics(usageStatistics, topics);
	}

	public List<Probability> getProbabilityOfUsage(List<Dictionary> wordUsages) {
		List<Probability> classProbabilities = new ArrayList<>();
		wordUsages.forEach(e -> {
			calculateProbability(e.getWordUsage());
			classProbabilities.add(new Probability(e.getWord(), calculateProbability(e.getWordUsage())));
		});
		return classProbabilities;
	}

	public List<Probability> getClassNormalizedProbability(List<Dictionary> wordUsages, List<Probability> classProbability) {
		List<Probability> classNormalizedProbability = new ArrayList<>();
		wordUsages.forEach(e -> {
			Map<String, Double> normalizedProbability = classProbability.stream().filter(p -> p.getWord().equals(e.getWord())).findFirst().get().getClassProbability();
			classNormalizedProbability.add(new Probability(e.getWord(), calculateNormalizedProbability(e.getWordUsage(), normalizedProbability)));
		});
		return classNormalizedProbability;
	}

	public Map<String, Double> calculateProbability(Map<String, Integer> topicUsage) {
		Map<String, Double> probabilities = new HashMap<>();
		double classesNum = topicUsage.values().stream().mapToInt(Integer::intValue).sum();
		topicUsage.forEach((k, v) -> probabilities.put(k, (double) v / classesNum));
		return probabilities;
	}

	public Map<String, Double> calculateNormalizedProbability(Map<String, Integer> topicUsage, Map<String, Double> classProbability) {
		Map<String, Double> normProbs = new HashMap<>();
		int usage = topicUsage.values().stream().reduce(0, Integer::sum);
		topicUsage.forEach((k, v) -> {
			double pProb = classProbability.get(k);
			double nWord = (double) usage;
			double pClass = 1 / (double) topicUsage.size();
			double pNorm = (nWord * pProb + pClass) / (nWord + 1);
			normProbs.put(k, pNorm);
		});
		return normProbs;
	}
}
