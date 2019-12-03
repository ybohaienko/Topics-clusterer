package com.bohaienko.lr4.utils;

import com.uttesh.exude.ExudeData;
import com.uttesh.exude.exception.InvalidDataException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class Parser {
	public List<String> filterWords(List<String> words, String[] topics) {
		return words.stream()
				.filter(word -> word.length() > 3)
				.filter(word -> !Arrays.asList(String.join(" ", topics).split("\\s+")).contains(word))
				.collect(Collectors.toList());
	}

	public List<String> getWordListOfTextList(List<String> texts) {
		List<String> result = null;
		try {
			result = Arrays.asList(ExudeData.getInstance()
					.filterStoppingsKeepDuplicates(texts.toString())
					.split("\\W+"));
		} catch (InvalidDataException e) {
			e.printStackTrace();
		}
		return result;
	}

	public List<String> getWordListOfText(String text) {
		List<String> result = null;
		try {
			result = Arrays.asList(ExudeData.getInstance()
					.filterStoppingsKeepDuplicates(text)
					.split("\\W+"));
		} catch (InvalidDataException e) {
			e.printStackTrace();
		}
		return result;
	}
}
