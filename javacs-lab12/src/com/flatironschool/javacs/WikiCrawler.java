package com.flatironschool.javacs;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import redis.clients.jedis.Jedis;


public class WikiCrawler {
	// keeps track of where we started
	public static String source;
	
	// the index where the results go
	public static JedisIndex index;
	
	// queue of URLs to be indexed
	public static Queue<String> queue = new LinkedList<String>();
	
	// fetcher used to get pages from Wikipedia
	public static WikiFetcher wf = new WikiFetcher();

	/**
	 * Constructor.
	 * 
	 * @param source
	 * @param index
	 */
	public WikiCrawler(String source, JedisIndex index) {
		this.source = source;
		this.index = index;
		queue.offer(source);
	}

	/**
	 * Returns the number of URLs in the queue.
	 * 
	 * @return
	 */
	public static int queueSize() {
		return queue.size();	
	}

	/**
	 * Gets a URL from the queue and indexes it.
	 * @param b 
	 * 
	 * @return Number of pages indexed.
	 * @throws IOException
	 */
	public static String crawl(boolean testing) throws IOException {
        // FILL THIS IN!

		String url = queue.poll();
		System.out.println("Crawling " + url);

		if (testing==false && index.isIndexed(url)) {
			System.out.println("Already indexed.");
			return null;
		}
		
		Elements paragraphs;
		if (testing) {
			paragraphs = wf.readWikipedia(url);
		} else {
			paragraphs = wf.fetchWikipedia(url);
		}
		index.indexPage(url, paragraphs);
		queueInternalLinks(paragraphs);		
		return url;
	}
	
	/**
	 * Parses paragraphs and adds internal links to the queue.
	 * 
	 * @param paragraphs
	 */
	// NOTE: absence of access level modifier means package-level
	public static void queueInternalLinks(Elements paragraphs) {
        // FILL THIS IN!
		for (Element paragraph: paragraphs) {
			helperQueueInternalLink(paragraph);
		}
	}

	public static void helperQueueInternalLink(Element paragraph) {
		Elements elts = paragraph.select("a[href]");
		for (Element elt: elts) {
			String relURL = elt.attr("href");
			
			if (relURL.startsWith("/wiki/")) {
				String absURL = "https://en.wikipedia.org" + relURL;
				queue.offer(absURL);
			}
		}
	}


	public static void main(String[] args) throws IOException {
		
/*		// make a WikiCrawler
		Jedis jedis = JedisMaker.make();
		JedisIndex index = new JedisIndex(jedis); 
		String source = "https://en.wikipedia.org/wiki/The_Social_Network";
		WikiCrawler wc = new WikiCrawler(source, index);
		
		// for testing purposes, load up the queue
		Elements paragraphs = wf.fetchWikipedia(source);
		wc.queueInternalLinks(paragraphs);

		// loop until we index a new page
		String res;
		do {
			res = wc.crawl(false);

            // REMOVE THIS BREAK STATEMENT WHEN crawl() IS WORKING
		} while (res == null);
		
		Map<String, Integer> map = index.getCounts("movie");
		for (Entry<String, Integer> entry: map.entrySet()) {
			System.out.println(entry);
		}

		//System.out.println(wc.queue);*/
	}
}
