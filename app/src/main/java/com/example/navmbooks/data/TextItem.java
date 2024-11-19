package com.example.navmbooks.data;

/**
 * Handles if chapter content is text
 */
public class TextItem {
    private final String text;

    public TextItem(String text){
        this.text=text;
    }

    public String getText(){
        return text;
    }
}
