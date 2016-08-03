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


public class MovieRanker {

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
    WikiMovie movie = new WikiMovie(drive);
    System.out.println(movie.directorAwardCount);
    //countActorAwards(source);
  }
}