package com.example.navmbooks;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Chapter {
    private final String chapTitle;
    private final Integer chapNum;
    private String text="No Text";
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
    public String getText(){
        return text;
    }
    public void setText(String text){
        if (Objects.equals(this.text, "No Text")){
            this.text = text;
        }else{
            this.text+=text;
        }
    }

    @Override
    public String toString(){
        return "\nChapter Title: " + chapTitle + ", Chapter Number: " + chapNum + "\n" + text;
    }
}
