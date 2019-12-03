package com.bohaienko.lr4.utils;

import com.bohaienko.lr4.model.WordAttributes;
import com.bohaienko.lr4.model.WordUsage;
import dnl.utils.text.table.TextTable;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class Printer {
	public static void printUsage(List<WordUsage> wordUsageList, String tableName) {
		List<List<String>> table = new ArrayList<>();
		for (WordUsage dict : wordUsageList) {
			List<String> row = new ArrayList<>();
			row.add(dict.getWord());
			row.add(String.valueOf(dict.getWordUsage()));
			table.add(row);
		}
		String[] headers = {"word", "usage"};
		printTable(headers, table, tableName);
	}

	public static void printAttributes(List<WordAttributes> wordAttributesList, String tableName) {
		List<List<String>> table = new ArrayList<>();
		for (WordAttributes dict : wordAttributesList) {
			List<String> row = new ArrayList<>();
			row.add(dict.getWord());
			row.add(String.valueOf(dict.getNumOfLettersBeforeWord()));
			row.add(String.valueOf(dict.getNumOfLettersInWord()));
			table.add(row);
		}
		String[] headers = {"word", "letters before word", "letters in word"};
		printTable(headers, table, tableName);
	}

	public static void printToArffFile(List<WordAttributes> wordAttributesList, File file) {
		try {
			file.delete();
			BufferedWriter bw = new BufferedWriter(new FileWriter(file, true));
			bw.write("@relation words\n\n@attribute x\tnumeric\n@attribute y\tnumeric\n@attribute word\tstring\n\n@data\n");
			for (WordAttributes a : wordAttributesList) {
				bw.write(String.format("%s\t%s\t%s\n", a.getNumOfLettersBeforeWord(), a.getNumOfLettersInWord(), a.getWord()));
			}
			bw.close();
			System.out.println("INFO: .arff file is created");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void printToConsoleAndFile(String output, String filename) throws FileNotFoundException {
		System.out.println(output);
		PrintStream fileStream = new PrintStream(filename);
		System.setOut(fileStream);
		System.out.println(output);
	}

	private static void printTable(String[] headers, List<List<String>> table, String tableName) {
		String[][] arrayTable = table.stream().map(l -> l.toArray(new String[0])).toArray(String[][]::new);
		TextTable tt = new TextTable(headers, arrayTable);
		tt.setAddRowNumbering(true);
		System.out.println(String.format("\n\n%17s%s", "TABLE NAME: ", tableName));
		tt.printTable();
		try {
			tt.toCsv(new FileOutputStream(tableName + ".csv"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
