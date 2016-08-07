package com.flatironschool.javacs;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.util.List;
import java.util.ArrayList;

import java.io.IOException;

import redis.clients.jedis.Jedis;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import java.net.URL;

import redis.clients.jedis.Jedis;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class WikiMovie {

	// Global Variables

	public String url;

	public String directorUrl;
	public List<String> castUrls;

	public Integer rottenTomatoesScore;
	public Integer metaCriticScore;

	public static String  wikiBaseUrl = "https://en.wikipedia.org";

	public int directorAwardCount = 0;

	// Constructors

	public WikiMovie(String url) throws IOException {
		this.url = url;
		init();
	}

	public void init() throws IOException {

		//download and parse the document
		Document doc = Jsoup.connect(url).get();

		//select the main content text
		Element content = doc.getElementById("mw-content-text");

		//castUrls = getCastUrls(content);

		rottenTomatoesScore = getMovieRating(content, "rotten tomatoes", "(\\d+%)");

		metaCriticScore = getMovieRating(content, "metacritic", "(score of \\d+)");

		directorUrl = getDirectorWikiUrl(url);

		directorAwardCount = getDirectorAwardCount(url);

		/*castUrls = getCastUrls(content); */
	}

	// return a List of URLs of the WikiMovie's cast
	public List<String> getCastUrls(Element content) {

		// The list to return
		List<String> castList = new ArrayList<>();

		return castList;
	}

	// returns the movie rating of a wikipedia page, given target string and regex of numerical representation
	private Integer getMovieRating(Element content, String targetString, String ratingRegex) {

		Elements paragraphs = content.select("p");

		for (Element p : paragraphs) { // iterate the paragraphs

			String textContent = p.html().toLowerCase();

			if (textContent.contains(targetString)) {

				int targetSentenceIndex = textContent.indexOf(targetString);

				String targetSentence = getContainingSentence(textContent, targetSentenceIndex);

				String score = firstRegexMatch(ratingRegex, targetSentence);

				return getInteger(score);
			}
		}

		return null;
	}

	private Integer getInteger(String str) {
		String intStr = firstRegexMatch("(\\d+)", str);
		return Integer.parseInt(intStr);
	}

	// returns the percent number closest to the index given in a block of text restricted to the sentence containing the index
	private String getContainingSentence(String textContent, int targetSentenceIndex) {

		int startIndex;
		int endIndex;
		char c;

		startIndex = targetSentenceIndex;
		c = textContent.charAt(startIndex);

		while (startIndex >= 1 && c != '.') {
			--startIndex;
			c = textContent.charAt(startIndex);
		}

		endIndex = textContent.indexOf(".", startIndex + 1);

		endIndex = endIndex > 0 ? endIndex : textContent.length();

		return textContent.substring(startIndex, endIndex);
	}

	public static String getDirectorWikiUrl(String source) throws IOException {
		// Grabs the Movie URL for use in JSoup
		Document doc = Jsoup.connect(source).get();
		// Table found on right hand side of movie wiki page
		Elements movieInfoTable = doc.select(".infobox.vevent");
		// All the rows inside of the table
		Elements rows = movieInfoTable.select("tr");
		// finds director url and appends to base url
		String directorWikiUrl = wikiBaseUrl + rows.get(2).select("td").select("a").first().attr("href");
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
		return sumOfTerms;
	}

	// Returns the first match of a regex string
	private String firstRegexMatch(String regex, String text) {

		Pattern pat = Pattern.compile(regex);
		Matcher mat = pat.matcher(text);

		if (mat.find())
			return mat.group(1);

		return null;
	}


	public static boolean releasedDuringOscarSeason(String source) throws IOException {
		// Checks to see if a movie was released during September - December. If so, return true.
    Document doc = Jsoup.connect(source).get();
    // Table found on right hand side of movie wiki page
    Elements movieInfoTable = doc.select(".infobox.vevent");
    // All the rows inside of the table
    Elements rows = movieInfoTable.select("tr");
    // finds director url and appends to base url
    int releaseDateIndex = rows.size() - 6;
    Element r = rows.get(releaseDateIndex).select("td").first().select("li").first();
    String rstring = r.text();
    System.out.println(rstring);
    
		return false;
	}

	public static void main(String[] args) throws IOException {

		WikiMovie wm = new WikiMovie("https://en.wikipedia.org/wiki/Br%C3%BCno");

		System.out.println("Rotten tomatoes score: " + wm.rottenTomatoesScore);

		System.out.println("Metacritic score: " + wm.metaCriticScore);
	}
}