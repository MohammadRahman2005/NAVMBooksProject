package com.example.navmbooks;

import android.util.Log;

import com.example.navmbooks.data.ImageItem;
import com.example.navmbooks.data.TextItem;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Book object
 */
public class Book {
    private final String title;
    private final String author;
    private final ArrayList<Chapter> chapters;
    private final String allContent;
    private final File coverImage;
    public String getTitle(){
        return title;
    }
    public ArrayList<Chapter> getChapters(){
        return chapters;
    }
    public File getCoverImage() {return coverImage; }

    private Book(String title, String author, ArrayList<Chapter> chapters, StringBuilder allContent, File coverImage){
        this.title=title;
        this.author=author;
        this.chapters=chapters;
        this.allContent = String.valueOf(allContent);
        this.coverImage = coverImage;
    }

    /**
     * Read book from localstorage and turn it into a book object
     * @param file the file of the book
     * @param cover the file which represents the image
     * @param bookDirectory the path to the current book
     * @return a new book object
     * @throws IOException
     */
    public static Book readBookFromFile(File file, File cover, String bookDirectory) throws IOException {
        StringBuilder allContent= new StringBuilder();
        ArrayList<Chapter> chapters = new ArrayList<Chapter>();
        Document doc = Jsoup.parse(file, "UTF-8", "");
        String title=doc.getElementsByTag("h1").get(0).text();
        String author = Objects.requireNonNull(doc.getElementById("pg-header-authlist")).getElementsByTag("p").get(0).text();

        Chapter chapter = null;
        Elements body = doc.body().children();
        Integer i=1;
        for (Element e:body) {
            allContent.append(e.text()).append('\n');
            if(e.tagName().equals("div")) {
                if (e.children().isEmpty()){
                    allContent.append(e.text()).append('\n');
                    if (chapter!=null) chapter.addContent(new TextItem(e.text()));
                }else {
                    for (Element child : e.children()) {
                        if (child.tagName().equals("img")) {
                            String imgSrc = bookDirectory + File.separator + child.attr("src");
                            allContent.append("image here").append("\n");
                            if (chapter != null) chapter.addContent(new ImageItem(imgSrc));
                        }
                    }
                    if (e.child(0).tagName().equals("table")) {
                        allContent.append("table here").append("\n");
                        if (chapter != null) chapter.addContent(new TextItem("Table here"));
                    }
                }
            }
            if(e.tagName().equals("table")) {
                allContent.append("table here").append("\n");
            }
            if(e.tagName().equals("h2") || e.tagName().equals("h3")){
                String chapterTitle = e.text();
                chapter = new Chapter(chapterTitle,i);
                i++;
                chapters.add(chapter);
            }else if(e.tagName().equals("p") && chapter!=null){
                if (e.children().isEmpty() || e.child(0).equals("i")){
                    chapter.addContent(e.text().trim()+" ");
                }
                chapter.addContent(new TextItem(e.text()));
            }
        }
        return new Book(title, author, chapters, allContent, cover);
    }
    @Override
    public String toString(){
        return "Title: "+this.title+ ", Author: "+this.author+"\n"+allContent;
    }
}
