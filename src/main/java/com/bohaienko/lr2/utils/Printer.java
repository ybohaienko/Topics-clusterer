package com.bohaienko.lr2.utils;

import com.bohaienko.lr2.model.Dictionary;
import com.bohaienko.lr2.model.Probability;
import com.bohaienko.lr2.model.VerificationData;
import dnl.utils.text.table.TextTable;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class Printer {
	public static void printDictionary(List<Dictionary> dictionaries, String[] topics, String tableName) {
		List<List<String>> table = new ArrayList<>();
		for (Dictionary dict : dictionaries) {
			List<String> row = new ArrayList<>();
			row.add(dict.getWord());
			Arrays.asList(topics).forEach(topic ->
					dict.getUsage().forEach((key, value) -> {
						if (key.equals(topic))
							row.add(value.toString());
					}));
			table.add(row);
		}
		String[] headers = (String[]) ArrayUtils.addAll(new String[]{"word"}, topics);
		printTable(headers, table, tableName);
	}

	public static void printProbabilities(List<Probability> probabilities, String[] topics, String tableName) {
		List<List<String>> table = new ArrayList<>();
		for (Probability prob : probabilities) {
			List<String> row = new ArrayList<>();
			row.add(prob.getWord());
			Arrays.asList(topics).forEach(topic ->
					prob.getProbability().forEach((key, value) -> {
						if (key.equals(topic))
							row.add(new DecimalFormat("####0.00").format(value));
					}));
			table.add(row);
		}
		String[] headers = (String[]) ArrayUtils.addAll(new String[]{"word"}, topics);
		printTable(headers, table, tableName);
	}

	public static void printVerification(List<VerificationData> verificationData, String tableName) {
		List<List<String>> table = new ArrayList<>();
		verificationData.forEach(e -> {
			List<String> row = new ArrayList<>();
			row.add(e.getHeader());
			row.add(e.getActualTopic());
			row.add(e.getClassificatedTopic());
			row.add(new DecimalFormat("####0.00").format(e.getClassificatedProbability()));
			row.add(String.valueOf(e.isClassificatedCorrectly()));
			table.add(row);
		});
		String[] headers = {"Header", "Actual Topic", "Classified Topic", "Classified Probability", "Is Classified Correctly"};
		printTable(headers, table, tableName);
	}

	private static void printTable(String[] headers, List<List<String>> table, String tableName) {
		String[][] arrayTable = table.stream().map(l -> l.toArray(new String[0])).toArray(String[][]::new);
		TextTable tt = new TextTable(headers, arrayTable);
		tt.setAddRowNumbering(true);
		System.out.println(String.format("\n\n%17s%s", "TABLE NAME: ", tableName));
		tt.printTable();
	}
}
