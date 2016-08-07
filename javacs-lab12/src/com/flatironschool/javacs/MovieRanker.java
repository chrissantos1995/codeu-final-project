package com.flatironschool.javacs;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;
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


public class MovieRanker {

  public static void main(String[] args) throws IOException {

    String interstellar = "https://en.wikipedia.org/wiki/Interstellar_(film)";
    String drive = "https://en.wikipedia.org/wiki/Drive_(2011_film)";
    String birdman = "https://en.wikipedia.org/wiki/Birdman_(film)";
    String carter = "https://en.wikipedia.org/wiki/John_Carter_(film)";
    String titanic = "https://en.wikipedia.org/wiki/Titanic_(1997_film)";
    String gravity = "https://en.wikipedia.org/wiki/Gravity_(film)";
    String bruno = "https://en.wikipedia.org/wiki/Br%C3%BCno";

    WikiMovie movie = new WikiMovie(bruno);
    movie.releasedDuringOscarSeason(bruno);

    List<WikiMovie> wikiMovieList = new ArrayList<>();

    wikiMovieList.add(new WikiMovie(interstellar));
    wikiMovieList.add(new WikiMovie(drive));
    wikiMovieList.add(new WikiMovie(birdman));
    wikiMovieList.add(new WikiMovie(carter));
    wikiMovieList.add(new WikiMovie(titanic));
    wikiMovieList.add(new WikiMovie(gravity));
    wikiMovieList.add(new WikiMovie(bruno));

    Collections.sort(wikiMovieList, Collections.reverseOrder());

    for(WikiMovie wm : wikiMovieList) {
        System.out.println(wm.url);
    }
  }
}