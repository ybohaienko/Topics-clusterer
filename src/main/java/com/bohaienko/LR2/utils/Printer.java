package com.bohaienko.LR2.utils;

import com.bohaienko.LR2.model.Dictionary;
import com.bohaienko.LR2.model.Probability;
import dnl.utils.text.table.TextTable;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class Printer {
	public void printUsageStatisticTable(List<Dictionary> dictionaries, String[] topics) {
		List<List<String>> table = new ArrayList<>();
		for (Dictionary dict : dictionaries) {
			List<String> row = new ArrayList<>();
			row.add(dict.getWord());
			Arrays.asList(topics).forEach(topic ->
					dict.getWordUsage().forEach((key, value) -> {
						if (key.equals(topic))
							row.add(value.toString());
					}));
			table.add(row);
		}
		printTable(topics, table);
	}

	public void printUsageStatisticTable2(List<Probability> probabilities, String[] topics) {
		List<List<String>> table = new ArrayList<>();
		for (Probability prob : probabilities) {
			List<String> row = new ArrayList<>();
			row.add(prob.getWord());
			Arrays.asList(topics).forEach(topic ->
					prob.getClassProbability().forEach((key, value) -> {
						if (key.equals(topic))
							row.add(value.toString());
					}));
			table.add(row);
		}
		printTable(topics, table);
	}

	private void printTable(String[] header, List<List<String>> table) {
		String[] headers = (String[]) ArrayUtils.addAll(new String[]{"word"}, header);
		String[][] arrayTable = table.stream().map(l -> l.toArray(new String[0])).toArray(String[][]::new);
		new TextTable(headers, arrayTable).printTable();
	}
}
