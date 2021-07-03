package com.example.diaryapplication;


import com.google.firebase.database.Exclude;
import java.io.Serializable;

public class Person implements Serializable {

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Exclude
    private String key;
    private String  name;
    private int age;
    private String birthday;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public Person(String name, String birthday, int age){
        this.name = name;
        this.birthday = birthday;
        this.age = age;
        return;
    }

    public Person(){
    }
}