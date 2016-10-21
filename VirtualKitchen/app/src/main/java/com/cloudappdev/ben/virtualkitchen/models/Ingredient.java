package com.cloudappdev.ben.virtualkitchen.models;

/**
 * Created by Ben on 21/10/2016.
 */

public class Ingredient {
    String uri;
    float quantity;
    Measure measure;
    float weight;
    Food food;

    public Ingredient(String uri, float quantity, Measure measure, float weight, Food food) {
        this.uri = uri;
        this.quantity = quantity;
        this.measure = measure;
        this.weight = weight;
        this.food = food;
    }

    public String getUri() {
        return uri;
    }

    public float getQuantity() {
        return quantity;
    }

    public Measure getMeasure() {
        return measure;
    }

    public float getWeight() {
        return weight;
    }

    public Food getFood() {
        return food;
    }
}
