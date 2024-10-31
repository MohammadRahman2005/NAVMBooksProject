package com.example.navmbooks;

public class Chapter {
    private final String chapTitle;
    private final Integer chapNum;
    private String text=null;

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
        if (this.text==null){
            this.text = text;
        }else {
            this.text += text;
        }
    }

    @Override
    public String toString(){
        return "\nChapter Title: " + chapTitle + ", Chapter Number: " + chapNum + "\n" + text;
    }
}
