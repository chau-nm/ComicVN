package com.example.comicvn.obj;

public class Page {
    private int numPage;
    private String image;

    public Page(){}

    public Page(int numPage, String image) {
        this.numPage = numPage;
        this.image = image;
    }

    public int getNumPage() {
        return numPage;
    }

    public String getImage() {
        return image;
    }
}
