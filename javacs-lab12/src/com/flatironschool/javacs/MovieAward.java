package com.flatironschool.javacs;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;

import redis.clients.jedis.Jedis;
import java.net.URL;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class MovieAward {

  public static String source;
  public JedisIndex index;
  private Queue<String> queue = new LinkedList<String>();
  final static WikiFetcher wf = new WikiFetcher();
  private Map<String, Integer> map;
  public static String  wikiBaseUrl = "https://en.wikipedia.org";

  // This wipes all the data off the Redis database
  public void deleteJedisIndexData(JedisIndex index) {
    index.deleteURLSets();
    index.deleteTermCounters();
    index.deleteAllKeys();
  }

  // This crawls every link starting on the source link
  public void callWikiCrawler() throws IOException {
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
    
    // This prints out the number of times a term appears on a series of URL
    /* Map<String, Integer> map = index.getCounts("fincher");
    for (Entry<String, Integer> entry: map.entrySet()) {
      System.out.println(entry);
    }*/
  }

  public static WikiSearch search(String term, JedisIndex index) {
    Map<String, Integer> map = index.getCounts(term);
    return new WikiSearch(map);
  }


  // Director logic
  public static String getDirectorWikiUrl(String source) throws IOException {
    // Grabs the Movie URL for use in JSoup
    Document doc = Jsoup.connect(source).get();
    // Table found on right hand side of movie wiki page
    Elements movieInfoTable = doc.select(".infobox.vevent");
    // All the rows inside of the table
    Elements rows = movieInfoTable.select("tr");
    // finds director url and appends to base url
    String directorWikiUrl = wikiBaseUrl + rows.get(2).select("td").select("a").first().attr("href");
    System.out.println(directorWikiUrl);
    return directorWikiUrl;
  }

  public static int getDirectorAwardCount(String source) throws IOException {
    Document doc = Jsoup.connect(getDirectorWikiUrl(source)).get();
    // Given that there is no common format for Director accolades.
    // This code counts particular words that would be expected in 'successful' director page
    // Hypothesis: Higher  frequency of these words -> higher chance of winning oscar
    Elements won = doc.getElementsContainingOwnText(" won ");
    Elements popular = doc.getElementsContainingOwnText(" popular ");
    Elements influential = doc.getElementsContainingOwnText(" influential ");
    Elements award = doc.getElementsContainingOwnText(" award ");
    Elements acclaimed = doc.getElementsContainingOwnText(" acclaimed ");
    Elements historic = doc.getElementsContainingOwnText(" historic ");
    Elements festival = doc.getElementsContainingOwnText(" festival ");
    Elements nominated = doc.getElementsContainingOwnText(" nominated ");
    int sumOfTerms = won.size() + popular.size() + influential.size() + award.size() 
      + acclaimed.size() + festival.size() + historic.size() + nominated.size();
    System.out.println(sumOfTerms);
    return sumOfTerms;
  }

  public static String countActorAwards(String source) throws IOException {
    Document doc = Jsoup.connect(getDirectorWikiUrl(source)).get();

    return "jh";

  }

  public static boolean releasedDuringOscarSeason(String source) throws IOException {
    // Checks to see if a movie was released during September - December. If so, return true.
    return false;
  }

  public static void main(String[] args) throws IOException {
    // Jedis jedis = JedisMaker.make();
    // JedisIndex index = new JedisIndex(jedis); 
    // String term1 = "academy";
    // System.out.println("Query: " + term1);
    // WikiSearch search1 = search(term1, index);
    // search1.print();
    String interstellar = "https://en.wikipedia.org/wiki/Interstellar_(film)";
    String drive = "https://en.wikipedia.org/wiki/Drive_(2011_film)";
    String birdman = "https://en.wikipedia.org/wiki/Birdman_(film)";
    String carter = "https://en.wikipedia.org/wiki/John_Carter_(film)";
    String titanic = "https://en.wikipedia.org/wiki/Titanic_(1997_film)";
    String gravity = "https://en.wikipedia.org/wiki/Gravity_(film)";
    getDirectorAwardCount(gravity);
    getDirectorAwardCount(carter);
    getDirectorAwardCount(interstellar);
    getDirectorAwardCount(drive);
    getDirectorAwardCount(birdman);
    getDirectorAwardCount(titanic);
    //countActorAwards(source);
  }
}