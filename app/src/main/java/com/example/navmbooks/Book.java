package com.example.navmbooks;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class Book {
    private final String title;
    private final String author;
    private final ArrayList<Chapter> chapters;

    private Book(String title, String author, ArrayList<Chapter> chapters){
        this.title=title;
        this.author=author;
        this.chapters=chapters;
    }
    public static Book readBook(String fileName) throws IOException {
        String title="";
        String author="";
        ArrayList<Chapter> chapters = new ArrayList<Chapter>();
        File input = new File(fileName);
        if (!input.exists()){
            throw new IOException("file not found at: " + input);
        }
        Document doc = Jsoup.parse(input, "UTF-8");
        title=doc.getElementsByTag("h1").get(0).text();
        author = Objects.requireNonNull(doc.getElementById("pg-header-authlist")).getElementsByTag("p").get(0).text();

        Chapter chapter = null;
        Elements body = doc.body().children();
        Integer i=0;
        for (Element e:body) {
            if(e.tagName().equals("h2")){
                String chapterTitle = e.text();
                chapter = new Chapter(chapterTitle,i);
                i++;
            }
            chapters.add(chapter);
        }
        return new Book(title, author, chapters);
    }
    @Override
    public String toString(){
        return "Title: "+this.title+ ", Author: "+this.author+"\n"+chapters;
    }
}
