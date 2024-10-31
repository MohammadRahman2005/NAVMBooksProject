package com.example.navmbooks;

public class Chapter {
    private final String chapTitle;
    private final Integer chapNum;
//    private final String text;

    public Chapter(String title, Integer chapNum){
        chapTitle = title;
        this.chapNum = chapNum;
//        this.text=text;
    }

    public String getTitle(){
        return chapTitle;
    }
    public Integer getChapNum(){
        return chapNum;
    }

    @Override
    public String toString(){
        return "Chapter Title: " + chapTitle + "Chapter Number: " + chapNum;
    }
}
