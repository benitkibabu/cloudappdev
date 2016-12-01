package com.cloudappdev.ben.virtualkitchen.models;

import java.io.Serializable;

/**
 * Created by Ben on 21/10/2016.
 */

public class Ingredient implements Serializable{
    int id;
    String text;
    double quantity;
    int userid;

    public Ingredient(int id, String text, double quantity, int userid) {
        this.id = id;
        this.text = text;
        this.quantity = quantity;
        this.userid = userid;
    }

    public Ingredient(String text, double quantity, int userid) {
        this.text = text;
        this.quantity = quantity;
        this.userid = userid;
    }

    public int getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public double getQuantity() {
        return quantity;
    }

    public int getUserid() {
        return userid;
    }
}
