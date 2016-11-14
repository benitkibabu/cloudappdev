package com.cloudappdev.ben.virtualkitchen.models;

import java.io.Serializable;

/**
 * Created by Ben on 21/10/2016.
 */

public class Recipe implements Serializable{
    int id;
    String uri;
    String label;
    String imageUrl;
    String source;
    String url;
    String shareAs;
    double yield;
    String dietLabels;
    String healthLabels;
    String cautions;
    String ingredientLines;
    double calories;
    double totalWeight;

    public Recipe(String uri, String label, String imageUrl, String source, String url,
                  String shareAs, double yield, String dietLabels, String healthLabels,
                  String cautions, String ingredientLines,
                  double calories, double totalWeight) {
        this.uri = uri;
        this.label = label;
        this.imageUrl = imageUrl;
        this.source = source;
        this.url = url;
        this.shareAs = shareAs;
        this.yield = yield;
        this.dietLabels = dietLabels;
        this.healthLabels = healthLabels;
        this.cautions = cautions;
        this.ingredientLines = ingredientLines;
        this.calories = calories;
        this.totalWeight = totalWeight;
    }


    public String getUri() {
        return uri;
    }

    public String getLabel() {
        return label;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getSource() {
        return source;
    }

    public String getUrl() {
        return url;
    }

    public String getShareAs() {
        return shareAs;
    }

    public double getYield() {
        return yield;
    }

    public int getId() {
        return id;
    }

    public String getDietLabels() {
        return dietLabels;
    }

    public String getHealthLabels() {
        return healthLabels;
    }

    public String getCautions() {
        return cautions;
    }

    public String getIngredientLines() {
        return ingredientLines;
    }

    public double getCalories() {
        return calories;
    }

    public double getTotalWeight() {
        return totalWeight;
    }
}
