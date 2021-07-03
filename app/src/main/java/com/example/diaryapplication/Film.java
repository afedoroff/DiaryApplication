package com.example.diaryapplication;


import com.google.firebase.database.Exclude;

import java.io.Serializable;

public class Film implements Serializable {
    private final float DEFAULT_RATE = 0;
    private final boolean DEFAULT_DONE = false;
    private final boolean DEFAULT_RATED = false;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Exclude
    private String key;
    private String  title, author, desc;
    private boolean done;
    private float rate;

    private boolean rated;


    public Film(String title, String author, String desc){
        this.title = title;
        this.author = author;
        this.desc = desc;
        this.rate = DEFAULT_RATE;
        this.done = DEFAULT_DONE;
        this.rated = DEFAULT_RATED;
        return;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public String getDesc() {
        return desc;
    }

    public float getRate() {
        return rate;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public boolean isRated() {
        return rated;
    }

    public void setRated(boolean rated) {
        this.rated = rated;
    }

    public Film(){
    }
}