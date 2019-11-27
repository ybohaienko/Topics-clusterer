package com.bohaienko.LR2;

import com.bohaienko.LR2.crawler.WebCrawler;
import com.bohaienko.LR2.model.Probability;
import com.bohaienko.LR2.model.Dictionary;
import com.bohaienko.LR2.utils.Analizer;
import com.bohaienko.LR2.utils.Printer;
import com.uttesh.exude.exception.InvalidDataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.bohaienko.LR2.utils.Common.getTestSet;
import static com.bohaienko.LR2.utils.Common.getTrainingSet;

@Service
public class Starter {
	@Autowired
	WebCrawler crawler;

	@Autowired
	Analizer analizer;

	@Autowired
	Printer printer;

	@EventListener(ApplicationReadyEvent.class)
	public void some() {
		String[] topics = {"transportation", "health", "show business"};
		List<List<String>> topicsSet = crawler.crawlByTopics(topics);
		List<List<String>> trainingSet = getTrainingSet(topicsSet);
		List<List<String>> testSet = getTestSet(topicsSet);

		try {
			List<Dictionary> dictionary = analizer.supplyDictionary(topics, trainingSet);
			List<Probability> denormProb = analizer.getProbabilityOfUsage(dictionary);
			List<Probability> normProb = analizer.getClassNormalizedProbability(dictionary, denormProb);

			printer.printUsageStatisticTable(dictionary, topics);
			printer.printUsageStatisticTable2(denormProb, topics);
			printer.printUsageStatisticTable2(normProb, topics);
		} catch (InvalidDataException e) {
			e.printStackTrace();
		}
	}
}
