package com.example.comicvn.obj;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class Chapter implements Comparable<Chapter>{

    private String id;
    private String number;
    private String title;
    private List<Page> pages;
    private Date update;

    public Chapter() {}

    public Chapter(String number,String title, List<Page> pages){
        this.id = System.currentTimeMillis() + "";
        this.number = number;
        this.title = title;
        this.pages = pages;
        this.update = Calendar.getInstance().getTime();
    }

    public Chapter(String id, String number, String title, List<Page> pages, Date update) {
        this.id = id;
        this.number = number;
        this.title = title;
        this.pages = pages;
        this.update = update;
    }

    public boolean isChapter(String chapterId){
        return  this.id.equals(chapterId);
    }

    public String getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public String getTitle() {
        return title;
    }

    public List<Page> getPages() {
        return pages;
    }

    public Date getUpdate() {
        return update;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Chapter chapter = (Chapter) o;
        return number.equals(chapter.number) && Objects.equals(id, chapter.id) && Objects.equals(title, chapter.title) && Objects.equals(pages, chapter.pages) && Objects.equals(update, chapter.update);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, number, title, pages, update);
    }

    @Override
    public int compareTo(Chapter chapter) {
        return this.update.compareTo(chapter.update);
    }
}
