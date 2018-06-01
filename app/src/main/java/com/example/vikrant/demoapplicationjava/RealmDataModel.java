package com.example.vikrant.demoapplicationjava;

import io.realm.RealmObject;


public class RealmDataModel extends RealmObject {

    private int id;
    private String title;
    private String body;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
