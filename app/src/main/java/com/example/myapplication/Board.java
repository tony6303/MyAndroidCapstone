package com.example.myapplication;

public class Board {
    private String id;
    private String title;
    private String contents;
    private String name;
    private String area;
    private String work;
    private String day;

    public Board(String id, String title, String contents, String name, String area, String work, String day) {
        this.id = id;
        this.title = title;
        this.contents = contents;
        this.name = name;
        this.area = area;
        this.work = work;
        this.day = day;
    }

    public Board(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getWork() {
        return work;
    }

    public void setWork(String work) {
        this.work = work;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    @Override
    public String toString() {
        return "Board{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", contents='" + contents + '\'' +
                ", name='" + name + '\'' +
                ", area=" + area +
                ", work=" + work +
                ", day=" + day +
                '}';
    }
}
