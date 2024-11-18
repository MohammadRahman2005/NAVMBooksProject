package com.example.navmbooks.data;

public class ImageItem {
    private final String path;

    public ImageItem(String path){
        this.path=path;
    }

    public String getText(){
        return path;
    }
}
