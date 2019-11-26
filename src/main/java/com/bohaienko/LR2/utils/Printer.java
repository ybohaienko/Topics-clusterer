package com.bohaienko.LR2.utils;

import com.bohaienko.LR2.model.ClassProbabilityStatistic;
import com.bohaienko.LR2.model.WordUsageStatistic;
import dnl.utils.text.table.TextTable;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

@Service
public class Printer {
	public void printUsageStatisticTable(List<WordUsageStatistic> stats, String[] topics) {
		String[] headers = (String[]) ArrayUtils.addAll(new String[]{"word"}, topics);
		List<List<String>> table = new ArrayList<>();
		for (WordUsageStatistic s : stats) {
			List<String> row = new ArrayList<>();
			row.add(s.getWord());
			for (String topic : topics) {
				s.getWordUsage().forEach((key, value) -> {
					if (key.equals(topic))
						row.add(value.toString());
				});
			}
			table.add(row);
		}
		String[][] arrayTable = table.stream().map(l -> l.toArray(new String[0])).toArray(String[][]::new);
		new TextTable(headers, arrayTable).printTable();
	}

	public void printUsageStatisticTable2(List<ClassProbabilityStatistic> stats, String[] topics) {
		String[] headers = (String[]) ArrayUtils.addAll(new String[]{"word"}, topics);
		List<List<String>> table = new ArrayList<>();
		for (ClassProbabilityStatistic s : stats) {
			List<String> row = new ArrayList<>();
			row.add(s.getWord());
			for (String topic : topics) {
				s.getClassProbability().forEach((key, value) -> {
					if (key.equals(topic))
						row.add(new DecimalFormat("####0.00").format(value));
				});
			}
			table.add(row);
		}
		String[][] arrayTable = table.stream().map(l -> l.toArray(new String[0])).toArray(String[][]::new);
		new TextTable(headers, arrayTable).printTable();
	}
}
