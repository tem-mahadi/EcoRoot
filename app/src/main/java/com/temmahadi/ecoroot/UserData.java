package com.temmahadi.ecoroot;

import android.content.Context;

import androidx.appcompat.app.AlertDialog;

public class UserData {
    private
    String title,desc,img;
    float weight;
    public UserData(){}

    public UserData(String title, String desc, float weight, String img) {
        this.title = title;
        this.desc = desc;
        this.weight = weight;
        this.img = img;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
