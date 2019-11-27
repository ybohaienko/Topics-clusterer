package com.bohaienko.LR2.crawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
public class WebCrawler {
	public List<List<String>> crawlByTopics(String[] topics) {
		List<List<String>> topicsHeaders = new ArrayList<>();
		Arrays.asList(topics).forEach(topic -> topicsHeaders.add(crawlByTopic(topic)));
		return topicsHeaders;
	}

	private List<String> crawlByTopic(String topic) {

		if (topic.contains(" "))
			topic = topic.replaceAll(" ", "%20");

		String blogUrl = "https://www.nytimes.com/search?query=" + topic + "&sort=best";
		Document doc = null;
		try {
			doc = Jsoup.connect(blogUrl).get();
		} catch (IOException e) {
			e.printStackTrace();
		}

		List<String> headers = new ArrayList<>();
		Objects.requireNonNull(doc).select("h4.css-2fgx4k").forEach(e -> headers.add(e.text()));
		return headers;
	}
}
