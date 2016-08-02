package com.flatironschool.javacs;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.util.List;
import java.util.ArrayList;

import java.io.IOException;

import redis.clients.jedis.Jedis;
import java.net.URL;

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

	public String rottenTomatoesScore;
	public String metaCriticScore;

	// Constructors

	public WikiMovie(String url) throws IOException {
		this.url = url;
		init();
	}

	// Methods

	public void init() throws IOException {

		// download and parse the document
		Document doc = Jsoup.connect(url).get();

		// select the main content text 
		Element content = doc.getElementById("mw-content-text");

		directorUrl = getDirectorUrl(content);
		//castUrls = getCastUrls(content);

		rottenTomatoesScore = getMovieRating(content, "rotten tomatoes", "(\\d+%)");

		metaCriticScore = getMovieRating(content, "metacritic", "(\\d+ out of \\d+)");
	}

  	// return the URL of the WikiMovie's director
	public String getDirectorUrl(Element content) {

	    // Table found on right hand side of movie wiki page
	    Element movieInfoTable = content.select(".infobox.vevent").first();

	    // All the rows inside of the table
	    Elements rows = movieInfoTable.select("tr");

	    // finds director url
	    String directorWikiUrl = rows.get(2).select("td").select("a").first().attr("href");

	    return directorWikiUrl;
  	}

  	// return a List of URLs of the WikiMovie's cast
  	public List<String> getCastUrls(Element content) {

  		// The list to return
  		List<String> castList = new ArrayList<>();

  		return castList;
  	}

  	// returns the movie rating of a wikipedia page, given target string and regex of numerical representation 
  	private String getMovieRating(Element content, String targetString, String ratingRegex) {

  		String score = null; // the Rotten Tomatoes score

  		Elements paragraphs = content.select("p");

  		for(Element p : paragraphs) { // iterate the paragraphs

  			String textContent = p.html().toLowerCase();

  			if(textContent.contains(targetString)) {

  				int targetSentenceIndex = textContent.indexOf(targetString);

  				String targetSentence = getContainingSentence(textContent,targetSentenceIndex);

  				score = firstRegexMatch(ratingRegex, targetSentence);

  				break;
  			}
  		}

  		return score;
  	}

  	// returns the percent number closest to the index given in a block of text restricted to the sentence containing the index
  	private String getContainingSentence(String textContent, int targetSentenceIndex) {

  		int startIndex;
  		int endIndex;
  		char c;

  		startIndex = targetSentenceIndex;
  		c = textContent.charAt(startIndex);

  		while(startIndex >= 1 && c != '.') {
  			--startIndex;
  			c = textContent.charAt(startIndex);
  		}

  		endIndex = textContent.indexOf(".",startIndex + 1);

  		endIndex = endIndex > 0 ? endIndex : textContent.length();

  		return textContent.substring(startIndex,endIndex);
  	}

  	// Returns the first match of a regex string
  	private String firstRegexMatch(String regex, String text) {

  		Pattern pat = Pattern.compile(regex);
  		Matcher mat = pat.matcher(text);

  		if(mat.find())
  			return mat.group(1);

  		return null;
  	}

  	public static void main(String[] args) throws IOException {

  		WikiMovie wm = new WikiMovie("https://en.wikipedia.org/wiki/Tropic_Thunder");

  		System.out.println("Rotten tomatoes score: " + wm.rottenTomatoesScore);

  		System.out.println("Metacritic score: " + wm.metaCriticScore);
  	}

}