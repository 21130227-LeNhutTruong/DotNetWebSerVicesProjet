package com.example.app2_use_firebase.Domain;

import java.util.ArrayList;

public class CategoryDomain {
    private String title;
    private int id;
    private ArrayList<String> picUrl;

    public CategoryDomain(String title, int id, ArrayList<String> picUrl) {
        this.title = title;
        this.id = id;
        this.picUrl = picUrl;
    }

    public CategoryDomain() {

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<String> getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(ArrayList<String> picUrl) {
        this.picUrl = picUrl;
    }
}
