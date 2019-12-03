package com.bohaienko.lr4.crawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class WebCrawler {
	public List<List<String>> crawlByTopics(String[] topics) {
		return Arrays.stream(topics)
				.map(this::crawlByTopic)
				.collect(Collectors.toList());
	}

	private List<String> crawlByTopic(String topic) {
		if (topic.contains(" ")) topic = topic.replaceAll(" ", "%20");
		String url = "https://www.nytimes.com/search?query=" + topic + "&sort=best";
		Document doc = null;
		try {
			doc = Jsoup.connect(url).get();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Objects.requireNonNull(doc).select("h4.css-2fgx4k")
				.stream()
				.map(Element::text)
				.collect(Collectors.toList());
	}
}
