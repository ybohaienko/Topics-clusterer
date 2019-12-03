package com.bohaienko.lr4;

import com.bohaienko.lr4.crawler.WebCrawler;
import com.bohaienko.lr4.model.WordAttributes;
import com.bohaienko.lr4.model.WordUsage;
import com.bohaienko.lr4.utils.Analyzer;
import com.bohaienko.lr4.utils.Parser;
import com.bohaienko.lr4.utils.Printer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import weka.clusterers.ClusterEvaluation;
import weka.clusterers.HierarchicalClusterer;
import weka.core.EuclideanDistance;
import weka.core.Instances;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;
import java.util.Objects;

import static com.bohaienko.lr4.utils.Printer.*;

@Service
public class Starter {
	@Autowired
	WebCrawler crawler;

	@Autowired
	Parser parser;

	@Autowired
	Analyzer analyzer;

	@Autowired
	Printer printer;

	@Autowired
	private Environment env;

	private List<List<String>> headers;
	private List<WordUsage> wordUsageList;
	private List<WordAttributes> wordAttributesList;
	private File file;

	@EventListener(ApplicationReadyEvent.class)
	public void init() throws Exception {
		String[] topics = {"transportation", "health", "show business"};
		if (env.getProperty("topics") != null)
			topics = Objects.requireNonNull(env.getProperty("topics")).split("\\s*,\\s*");

		headers = crawler.crawlByTopics(topics);
		wordUsageList = analyzer.buildWordUsage(headers, topics);
		wordUsageList = analyzer.filterWordsMoreThanOneUsage(wordUsageList);
		printUsage(wordUsageList, "WordsUsage");

		wordAttributesList = analyzer.buildWordAttributes(headers, wordUsageList);
		printAttributes(wordAttributesList, "WordsAttributes");

		file = new File("words.arff");
		printToArffFile(wordAttributesList, file);

		String hrModelOutput = buildHierarchicalModel();
		printToConsoleAndFile(hrModelOutput, "modelOutput");
	}

	private String buildHierarchicalModel() throws Exception {
		Instances instances = new Instances(new BufferedReader(new FileReader(file)));
		ClusterEvaluation eval = new ClusterEvaluation();

		instances.setClassIndex(instances.numAttributes() - 1);
		HierarchicalClusterer hrModel = new HierarchicalClusterer();
		hrModel.setDistanceFunction(new EuclideanDistance());
		hrModel.setNumClusters(2);
		hrModel.buildClusterer(instances);
		eval.setClusterer(hrModel);
		eval.evaluateClusterer(instances);

		return eval.clusterResultsToString();
	}
}
