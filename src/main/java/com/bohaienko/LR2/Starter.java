package com.bohaienko.LR2;

import com.bohaienko.LR2.crawler.WebCrawler;
import com.bohaienko.LR2.model.ClassProbabilityStatistic;
import com.bohaienko.LR2.model.WordUsageStatistic;
import com.bohaienko.LR2.utils.Analizer;
import com.bohaienko.LR2.utils.Printer;
import com.uttesh.exude.exception.InvalidDataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.List;

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
		String[] topics = {
				"transportation"
				, "health"
				, "show business"
		};
		List<List<String>> topicMatrix = crawler.crawlByTopics(topics);

		try {
			List<WordUsageStatistic> statistics = analizer.countWordsUsage(topics, topicMatrix);
			List<ClassProbabilityStatistic> stat = analizer.getProbabilityOfUsage(statistics);
			List<ClassProbabilityStatistic> normStat = analizer.getClassNormalizedProbability(statistics, stat);
//			System.out.println(statistics);
			printer.printUsageStatisticTable(statistics, topics);
			printer.printUsageStatisticTable2(stat, topics);
			printer.printUsageStatisticTable2(normStat, topics);
		} catch (InvalidDataException e) {
			e.printStackTrace();
		}
	}
}
