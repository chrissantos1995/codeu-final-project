package com.flatironschool.javacs;

import java.io.IOException;
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

  public String rottenTomatoesScore;
  public String metaCriticScore;
  public static String  wikiBaseUrl = "https://en.wikipedia.org";
  public int [directorAwardCount] = 0;

  // Constructors

  public WikiMovie(String url) throws IOException{
    this.url = url;
    init();
  }

  // Methods

  public void init() throws IOException {

    // download and parse the document
    // Document doc = Jsoup.connect(wikiUrl).get();

    // select the main content text
    // Element content = doc.getElementById("mw-content-text");
    directorUrl = getDirectorWikiUrl(url);
    directorAwardCount = getDirectorAwardCount(url);

    /*castUrls = getCastUrls(content);
    rottenTomatoesScore = getRottenTomatoesScore(content);
    metaCriticScore = getMetaCriticScore(content);*/
  }

  /*// return a List of URLs of the WikiMovie's cast
  public List<String> getCastUrls(Element content) {

    // The list to return
    List<String> castList = new ArrayList<>();

    return castList;
  }

  // return the Rotten Tomatoes score of the movie
  public String getRottenTomatoesScore(Element content) {

    String score = null; // the Rotten Tomatoes score

    Elements paragraphs = content.select("p");

    for (Element p : paragraphs) { // iterate the paragraphs

      String textContent = p.html().toLowerCase();

      if (textContent.contains("rotten tomatoes")) {

        int targetSentenceIndex = textContent.indexOf("rotten tomatoes");
        String targetSentence = getContainingSentence(textContent, targetSentenceIndex);

        break;
      }
    }

    return score;
  }

  // returns the percent number closest to the index given in a block of text restricted to the sentence containing the index
  private String getContainingSentence(String textContent, int targetSentenceIndex) {


  }*/

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

    public static boolean releasedDuringOscarSeason(String source) throws IOException {
    // Checks to see if a movie was released during September - December. If so, return true.
    return false;
  }


}