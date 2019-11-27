package com.bohaienko.lr2;

import com.bohaienko.lr2.crawler.WebCrawler;
import com.bohaienko.lr2.model.Dictionary;
import com.bohaienko.lr2.model.Probability;
import com.bohaienko.lr2.model.VerificationData;
import com.bohaienko.lr2.utils.Analyzer;
import com.bohaienko.lr2.utils.Parser;
import com.bohaienko.lr2.utils.Printer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.bohaienko.lr2.utils.Printer.*;

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

	private List<List<String>> topicsSet, trainingSet, testSet;
	private List<Dictionary> dictionary;
	private List<Probability> denormProb, normProb;
	private List<VerificationData> verificationData;

	@EventListener(ApplicationReadyEvent.class)
	public void init() {
		String[] topics = {"transportation", "health", "show business"};
		topicsSet = crawler.crawlByTopics(topics);
		trainingSet = parser.getTrainingSet(topicsSet);
		testSet = parser.getTestSet(topicsSet);

		dictionary = analyzer.buildDictionary(topics, trainingSet);
		denormProb = analyzer.getDenormalizedProbability(dictionary);
		normProb = analyzer.getNormalizedProbability(dictionary, denormProb);

		verificationData = analyzer.verifyClassification(testSet, topics, normProb);

		printDictionary(dictionary, topics, "Dictionary");
		printProbabilities(denormProb, topics, "Denormalized Probabilities");
		printProbabilities(normProb, topics, "Normalized Probabilities");
		printVerification(verificationData, "Classification Verification Using Test Set");
	}
}
