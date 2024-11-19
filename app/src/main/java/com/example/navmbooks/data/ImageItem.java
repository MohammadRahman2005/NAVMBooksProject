package com.example.navmbooks.data;

/**
 * this class handles if the chapter content is an image
 */
public class ImageItem {
    private final String path;

    public ImageItem(String path){
        this.path=path;
    }

    public String getImagePath(){
        return path;
    }
}
