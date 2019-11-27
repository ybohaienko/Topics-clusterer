package com.bohaienko.LR2;

import com.bohaienko.LR2.crawler.WebCrawler;
import com.bohaienko.LR2.model.Probability;
import com.bohaienko.LR2.model.Dictionary;
import com.bohaienko.LR2.model.VerificationData;
import com.bohaienko.LR2.utils.Analizer;
import com.bohaienko.LR2.utils.Parser;
import com.bohaienko.LR2.utils.Printer;
import com.uttesh.exude.exception.InvalidDataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.bohaienko.LR2.utils.Printer.*;

@Service
public class Starter {
	@Autowired
	WebCrawler crawler;

	@Autowired
	Parser parser;

	@Autowired
	Analizer analizer;

	@Autowired
	Printer printer;

	private List<List<String>> topicsSet,trainingSet,testSet;
	private List<Dictionary> dictionary;
	private List<Probability> denormProb, normProb;
	private List<VerificationData> verifData;

	@EventListener(ApplicationReadyEvent.class)
	public void init() throws InvalidDataException {
		String[] topics = {"transportation", "health", "show business"};
		topicsSet 	= crawler.crawlByTopics(topics);
		trainingSet = parser.getTrainingSet(topicsSet);
		testSet 	= parser.getTestSet(topicsSet);

		dictionary 	= analizer.buildDictionary(topics, trainingSet);
		denormProb 	= analizer.getDenormalizedProbability(dictionary);
		normProb 	= analizer.getNormalizedProbability(dictionary, denormProb);

		verifData 	= analizer.varifyClassification(testSet, topics, normProb);

		printDictionary(dictionary, topics,"Dictionary");
		printProbabilities(denormProb, topics,"Denormalized Probabilities");
		printProbabilities(normProb, topics,"Normalized Probabilities");
		printVerification(verifData, "Classification Verification Using Test Set");
	}
}
