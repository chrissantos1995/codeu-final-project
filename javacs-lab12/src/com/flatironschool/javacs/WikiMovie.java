package com.flatironschool.javacs;

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

	public WikiMovie(String url) {
		this.url = url;
		init();
	}

	// Methods

	public void init() throws IOException {

		// download and parse the document
		Document doc = Jsoup.connect(wikiUrl).get();

		// select the main content text 
		Element content = doc.getElementById("mw-content-text");

		directorUrl = getDirectorUrl(content);
		castUrls = getCastUrls(content);

		rottenTomatoesScore = getRottenTomatoesScore(content);
		metaCriticScore = getMetaCriticScore(content);
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

  	// return the Rotten Tomatoes score of the movie
  	public String getRottenTomatoesScore(Element content) {

  		String score = null; // the Rotten Tomatoes score

  		Elements paragraphs = content.select("p");

  		for(Element p : paragraphs) { // iterate the paragraphs

  			String textContent = p.html().toLowerCase();

  			if(textContent.contains("rotten tomatoes")) {

  				int targetSentenceIndex = textContent.indexOf("rotten tomatoes");
  				String targetSentence = getContainingSentence(textContent,targetSentenceIndex);

  				break;
  			}
  		}

  		return score;
  	}

  	// returns the percent number closest to the index given in a block of text restricted to the sentence containing the index
  	private String getContainingSentence(String textContent, int targetSentenceIndex) {


  	}

}