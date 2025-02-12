package com.example.navmbooks.ui.theme;

import com.example.navmbooks.data.ImageItem;
import com.example.navmbooks.data.TableItem;
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
    private final String coverImage;
    public String getTitle(){
        return title;
    }
    public String getAuthor() { return author; }
    public ArrayList<Chapter> getChapters(){
        return chapters;
    }
    public String getCoverImage() {return coverImage; }

    public Book(String title, String author, ArrayList<Chapter> chapters, StringBuilder allContent, String coverImage){
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
                            if (chapter != null) chapter.addContent(new ImageItem(imgSrc));
                        }
                    }
                    if (e.child(0).tagName().equals("table")) {
                        StringBuilder tableContent = new StringBuilder();
                        if (e.child(0).tagName().equals("tbody")){
                            for (Element row : e.child(0).children()){
                                StringBuilder rowContent = new StringBuilder();
                                for (Element cell : row.children()) {
                                    if (rowContent.length() > 0) {
                                        rowContent.append(" | ");
                                    }
                                    rowContent.append(cell.text());
                                }
                                tableContent.append("| ").append(rowContent).append(" |\n");
                                tableContent.append("-".repeat(rowContent.length() + 4)).append("\n");
                            }
                        }
                        TableItem tableItem = new TableItem(tableContent.toString());
                        if (chapter != null) chapter.addContent(tableItem);
                    }
                }
            }
            if(e.tagName().equals("table")) {
                StringBuilder tableContent = new StringBuilder();
                if (e.child(0).tagName().equals("tbody")){
                    for (Element row : e.child(0).children()){
                        StringBuilder rowContent = new StringBuilder();
                        for (Element cell : row.children()) {
                            if (rowContent.length() > 0) {
                                rowContent.append(" | ");
                            }
                            rowContent.append(cell.text());
                        }
                        tableContent.append("| ").append(rowContent).append(" |\n");
                        tableContent.append("-".repeat(rowContent.length() + 4)).append("\n");
                    }
                }
                TableItem tableItem = new TableItem(tableContent.toString());
                if (chapter != null) chapter.addContent(tableItem);
            }
            if(e.tagName().equals("div") && e.className().equals("chapter")){
                for (Element child : e.children()){
                    if (child.tagName().equals("h2")){
                        String chapterTitle = e.text();
                        chapter = new Chapter(chapterTitle,i);
                        i++;
                        chapters.add(chapter);
                    }else if(child.tagName().equals("p") && chapter!=null){
                        if (e.children().isEmpty() || e.child(0).equals("i")){
                            chapter.addContent(e.text().trim()+" ");
                        }
                        chapter.addContent(new TextItem(e.text()));
                    }
                }
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
        return new Book(title, author, chapters, allContent, cover.getAbsolutePath());
    }
    @Override
    public String toString(){
        return "Title: "+this.title+ ", Author: "+this.author+"\n"+allContent;
    }
}
