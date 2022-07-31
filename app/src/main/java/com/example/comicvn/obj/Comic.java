package com.example.comicvn.obj;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class Comic {
    private String id;
    private String name;
    private String cover;
    private String state;
    private List<String> category;
    private long view;
    private String content;
    private List<Chapter> chapters;

    public Comic(){}

    public Comic(String id, String name, String cover, List<String> category, String content){
        this.id = id;
        this.name = name;
        this.cover = cover;
        this.state = "Đang tiến hành";
        this.category = category;
        this.view = 0;
        this.content = content;
    }

    public Comic(String id, String name, String cover, String state
            , List<String> category, long view,  String content
            , List<Chapter> chapters) {
        this.id = id;
        this.name = name;
        this.cover = cover;
        this.state = state;
        this.category = category;
        this.view = view;
        this.content = content;
        this.chapters = chapters;
    }

    public void increaseView(){view++;}

    @RequiresApi(api = Build.VERSION_CODES.N)
    public Chapter getChapter(String chapterId){
        return  chapters != null && chapters.size() > 0
                ? chapters.stream()
                .filter(c -> c.isChapter(chapterId))
                .findFirst()
                .get() : null;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public List<Chapter> getNewUpdateChapter(int number){
        return chapters != null
                ? chapters.stream()
                .sorted(Comparator.reverseOrder())
                .limit(number)
                .collect(Collectors.toList())
                : new ArrayList<>();
    }

    public Chapter getPreviousChapter(Chapter chapter){
        int index = chapters.indexOf(chapter);
        return index > 0 ? chapters.get(index - 1) : null;
    }

    public Chapter getNextChapter(Chapter chapter){
        int index = chapters.indexOf(chapter);
        return index < chapters.size() - 1 ? chapters.get(index + 1) : null;
    }

    public void addChapter(Chapter chapter){
        if (chapters == null)
            chapters = new ArrayList<>();
        chapters.add(chapter);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public Date getUpdate(){
        return chapters != null && chapters.size() > 0
                ? chapters.stream()
                .sorted(Comparator.reverseOrder())
                .findFirst()
                .get()
                .getUpdate()
                : null;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public List<Chapter> getChapters() {
        return chapters;
    }

    public String getCover() {
        return cover;
    }

    public List<String> getCategory() {
        return category;
    }

    public long getView() {
        return view;
    }

    public String getContent() {
        return content;
    }

    public String getState() {
        return state;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setCategory(List<String> category) {
        this.category = category;
    }

    public void setView(long view) {
        this.view = view;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setChapters(List<Chapter> chapters) {
        this.chapters = chapters;
    }
}
