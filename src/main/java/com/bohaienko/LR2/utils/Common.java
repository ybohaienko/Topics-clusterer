package com.bohaienko.LR2.utils;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class Common {
	public static List<List<String>> getTrainingSet(List<List<String>> set) {
		List<List<String>> trainingSet = new ArrayList<>();
		set.forEach(e -> trainingSet.add(e.subList(0, (int) (set.size() * 0.7))));
		return trainingSet;
	}

	public static List<List<String>> getTestSet(List<List<String>> set) {
		List<List<String>> trainingSet = new ArrayList<>();
		set.forEach(e -> trainingSet.add(e.subList(0, (int) (set.size() * 0.3))));
		return trainingSet;
	}
}
