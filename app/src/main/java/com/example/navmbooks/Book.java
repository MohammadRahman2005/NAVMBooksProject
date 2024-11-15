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
    private final String allContent;
    public String getTitle(){
        return title;
    }
    public ArrayList<Chapter> getChapters(){
        return chapters;
    }

    private Book(String title, String author, ArrayList<Chapter> chapters, StringBuilder allContent){
        this.title=title;
        this.author=author;
        this.chapters=chapters;
        this.allContent = String.valueOf(allContent);
    }
    public static Book readBookFromFile(File file) throws IOException {
        String title="";
        String author="";
        StringBuilder allContent= new StringBuilder();
        ArrayList<Chapter> chapters = new ArrayList<Chapter>();
        Document doc = Jsoup.parse(file, "UTF-8", "");
        title=doc.getElementsByTag("h1").get(0).text();
        author = Objects.requireNonNull(doc.getElementById("pg-header-authlist")).getElementsByTag("p").get(0).text();

        Chapter chapter = null;
        Elements body = doc.body().children();
        Integer i=1;
        for (Element e:body) {
            allContent.append(e.text()).append('\n');
            if(e.tagName().equals("div")) {
                if (e.children().isEmpty()){
                    allContent.append(e.text()).append('\n');
                }else if(e.child(0).tagName().equals("img")){
                    allContent.append("image here").append("\n");
                }else if(e.child(0).tagName().equals("table")) {
                    allContent.append("table here").append("\n");
                }
            }
            if (e.tagName().equals("img")){
                allContent.append("image here").append("\n");
            }if(e.tagName().equals("table")) {
                allContent.append("table here").append("\n");
            }
            if(e.tagName().equals("h2") || e.tagName().equals("h3")){
                String chapterTitle = e.text();
                chapter = new Chapter(chapterTitle,i);
                i++;
                chapters.add(chapter);
            }else if(e.tagName().equals("p") && chapter!=null){
                chapter.setText(e.text());
                if (e.children().isEmpty() || e.child(0).equals("i")){
                    chapter.setText(e.text().trim()+" ");
                }
//                else if(chapter.getText() == null){
//                    chapters.remove(chapter);
//                    i = chapters.size()+1;
//                }
            }
        }
        return new Book(title, author, chapters, allContent );
    }

    public static Book readBookURL(String URL) throws IOException {
        String title="";
        String author="";
        StringBuilder allContent= new StringBuilder();
        ArrayList<Chapter> chapters = new ArrayList<Chapter>();
        Document doc = Jsoup.connect(URL).get();
        title=doc.getElementsByTag("h1").get(0).text();
        author = Objects.requireNonNull(doc.getElementById("pg-header-authlist")).getElementsByTag("p").get(0).text();

        Chapter chapter = null;
        Elements body = doc.body().children();
        Integer i=1;
        for (Element e:body) {
            allContent.append(e.text()).append('\n');
            if(e.tagName().equals("div")) {
                if (e.children().isEmpty()){
                    allContent.append(e.text()).append('\n');
                }else if(e.child(0).tagName().equals("img")){
                    allContent.append("image here").append("\n");
                }else if(e.child(0).tagName().equals("table")) {
                    allContent.append("table here").append("\n");
                }
            }
            if (e.tagName().equals("img")){
                allContent.append("image here").append("\n");
            }if(e.tagName().equals("table")) {
                allContent.append("table here").append("\n");
            }
            if(e.tagName().equals("h2") || e.tagName().equals("h3")){
                String chapterTitle = e.text();
                chapter = new Chapter(chapterTitle,i);
                i++;
                chapters.add(chapter);
            }else if(e.tagName().equals("p") && chapter!=null){
                chapter.setText(e.text());
                if (e.children().isEmpty() || e.child(0).equals("i")){
                    chapter.setText(e.text().trim()+" ");
                }
//                else if(chapter.getText() == null){
//                    chapters.remove(chapter);
//                    i = chapters.size()+1;
//                }
            }
        }
        return new Book(title, author, chapters, allContent );
    }
    @Override
    public String toString(){
        return "Title: "+this.title+ ", Author: "+this.author+"\n"+allContent;
    }
}
