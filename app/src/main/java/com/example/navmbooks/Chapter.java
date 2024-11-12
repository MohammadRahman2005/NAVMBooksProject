package com.example.navmbooks;

import java.util.Objects;

public class Chapter {
    private final String chapTitle;
    private final Integer chapNum;
    private String text="No Text";

    public Chapter(String title, Integer chapNum){
        chapTitle = title;
        this.chapNum = chapNum;
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
