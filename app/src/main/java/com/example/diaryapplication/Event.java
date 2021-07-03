package com.example.diaryapplication;


import com.google.firebase.database.Exclude;
import java.io.Serializable;

public class Event implements Serializable {

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }


    @Exclude
    private String key;
    private String title;
    private String date;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String birthday) {
        this.date = birthday;
    }

    public Event(String name, String birthday){
        this.title = name;
        this.date = birthday;
        return;
    }

    public Event(){
    }
}