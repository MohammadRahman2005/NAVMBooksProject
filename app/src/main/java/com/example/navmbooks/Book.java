package com.example.navmbooks;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;

public class Book {
    private final String title;
    private final String author;
//    private final ArrayList<Chapter> chapters;

    private Book(String title, String author){
        this.title=title;
        this.author=author;
    }
    public static Book readBook(String url) throws IOException {
        String title="";
        String author="";
        Document doc = Jsoup.connect(url).get();
        title=doc.getElementsByTag("h1").get(0).text();
        author = doc.getElementsByClass("ph1").get(0).text();
        return new Book(title, author);
    }
    public static void print(){
        System.out.println("test");
    }
}
