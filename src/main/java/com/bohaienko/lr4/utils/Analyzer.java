package com.bohaienko.lr4.utils;

import com.bohaienko.lr4.model.WordAttributes;
import com.bohaienko.lr4.model.WordUsage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class Analyzer {

	@Autowired
	Parser parser;

	private List<WordUsage> wordUsageList;
	private List<WordAttributes> wordAttributesList;

	public List<WordUsage> buildWordUsage(List<List<String>> topicsHeaders, String[] topics) {
		wordUsageList = new ArrayList<>();
		topicsHeaders.forEach(topicHeaders -> {
			topicHeaders.forEach(header -> {
				parser.filterWords(parser.getWordListOfText(header), topics).forEach(word -> {
					if (wordUsageList.stream().noneMatch(s -> s.getWord().equals(word))) {
						wordUsageList.add(new WordUsage(word, 1));
					} else {
						wordUsageList.stream().filter(s -> s.getWord().equals(word)).forEach(e -> {
							int usage = e.getWordUsage();
							e.setWordUsage(usage + 1);
						});
					}
				});
			});
		});
		return wordUsageList;
	}

	public List<WordUsage> filterWordsMoreThanOneUsage(List<WordUsage> wordUsageList) {
		return wordUsageList.stream().filter(e -> e.getWordUsage() > 1).collect(toList());
	}

	public List<WordAttributes> buildWordAttributes(List<List<String>> topicsHeaders, List<WordUsage> wordUsageList) {
		wordAttributesList = new ArrayList<>();
		topicsHeaders.forEach(topicHeaders -> {
			topicHeaders.forEach(header -> {
				List<String> headerWords = parser.getWordListOfText(header);
				headerWords.forEach(word -> {
					wordUsageList.forEach(e -> {
						if (e.getWord().equals(word)) {
							wordAttributesList.add(new WordAttributes(
									e.getWord(),
									countCharsBeforeWordInText(word, headerWords),
									word.length()
							));
						}
					});
				});
			});
		});
		return wordAttributesList;
	}

	public int countCharsBeforeWordInText(String word, List<String> text) {
		int counter = 0;
		for (String e : text) {
			if (!e.equals(word))
				counter += e.length();
			else
				break;
		}
		return counter;
	}
}
