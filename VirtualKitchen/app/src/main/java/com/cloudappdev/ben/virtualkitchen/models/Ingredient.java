package com.cloudappdev.ben.virtualkitchen.models;

import java.io.Serializable;

/**
 * Created by Ben on 21/10/2016.
 */

public class Ingredient implements Serializable{
    int id;
    String uri;
    double quantity;
    String measure;
    double weight;
    String food;
    int userid;

    public Ingredient(int id, String uri, double quantity, String measure, double weight, String food, int userid) {
        this.id = id;
        this.uri = uri;
        this.quantity = quantity;
        this.measure = measure;
        this.weight = weight;
        this.food = food;
        this.userid = userid;
    }

    public Ingredient(String uri, double quantity, String measure, double weight, String food) {
        this.id = id;
        this.uri = uri;
        this.quantity = quantity;
        this.measure = measure;
        this.weight = weight;
        this.food = food;
    }
    public Ingredient(String uri, double quantity, String measure, double weight, String food, int userid) {
        this.uri = uri;
        this.quantity = quantity;
        this.measure = measure;
        this.weight = weight;
        this.food = food;
        this.userid = userid;
    }

    public int getId() {
        return id;
    }

    public String getUri() {
        return uri;
    }

    public double getQuantity() {
        return quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public double getWeight() {
        return weight;
    }

    public String getFood() {
        return food;
    }
}
