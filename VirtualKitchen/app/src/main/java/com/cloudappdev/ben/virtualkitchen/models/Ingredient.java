package com.cloudappdev.ben.virtualkitchen.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Ben on 21/10/2016.
 */

public class Ingredient implements Serializable{
    @SerializedName("id")
    int id;
    @SerializedName("text")
    String text;
    @SerializedName("weight")
    double weight;
    @SerializedName("app_user_id")
    int userid;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }
}
