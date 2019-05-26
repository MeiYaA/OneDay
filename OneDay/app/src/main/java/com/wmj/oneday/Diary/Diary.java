package com.wmj.oneday.Diary;

public class Diary {

    private String date;
    private String title;
    private String content;
    private String tag;

    public Diary(String date, String title, String content, String tag) {
        this.date = date;
        this.title = title;
        this.content = content;
        this.tag = tag;
    }
    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
