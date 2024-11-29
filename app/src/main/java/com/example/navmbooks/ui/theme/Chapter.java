package com.example.navmbooks.ui.theme;

import com.example.navmbooks.data.ImageItem;
import com.example.navmbooks.data.TextItem;

import java.util.ArrayList;
import java.util.List;

/**
 * This class creates chapter objects.
 */
public class Chapter {
    private final String chapTitle;
    private final Integer chapNum;
    private final List<Object> content;

    public List<Object> getContent() {
        return content;
    }

    public void addContent(Object item) {
        content.add(item);
    }

    public Chapter(String title, Integer chapNum){
        chapTitle = title;
        this.chapNum = chapNum;
        this.content=new ArrayList<>();
    }

    public String getTitle(){
        return chapTitle;
    }
    public Integer getChapNum(){
        return chapNum;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("\nChapter Title: ").append(chapTitle)
                .append(", Chapter Number: ").append(chapNum)
                .append("\n");

        for (Object item : content) {
            if (item instanceof TextItem) {
                result.append(((TextItem) item).getText()).append("\n");
            } else if (item instanceof ImageItem) {
                result.append("Image: ").append(((ImageItem) item).getImagePath()).append("\n");
            }
        }
        return result.toString();
    }
}
