package com.cloudappdev.ben.virtualkitchen.models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Ben on 21/10/2016.
 */

public class Recipe implements Serializable{
    String uri;
    String label;
    String imageUrl;
    String source;
    String url;
    String shareAs;
    double yield;
    ArrayList<String> dietLabels;
    ArrayList<String> healthLabels;
    ArrayList<String> cautions;
    ArrayList<String> ingredientLines;
    ArrayList<Ingredient> ingredients;
    double caleries;
    double totalWeight;

    public Recipe(String uri, String label, String imageUrl, String source,
                  String url, String shareAs, double yield, ArrayList<String> dietLabels,
                  ArrayList<String> healthLabels, ArrayList<String> cautions, ArrayList<String> ingredientLines,
                  ArrayList<Ingredient> ingredients, double caleries, double totalWeight) {
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
        this.ingredients = ingredients;
        this.caleries = caleries;
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

    public ArrayList<String> getDietLabels() {
        return dietLabels;
    }

    public ArrayList<String> getHealthLabels() {
        return healthLabels;
    }

    public ArrayList<String> getIngredientLines() {
        return ingredientLines;
    }

    public ArrayList<Ingredient> getIngredients() {
        return ingredients;
    }

    public double getCaleries() {
        return caleries;
    }

    public double getTotalWeight() {
        return totalWeight;
    }

    public ArrayList<String> getCautions() {
        return cautions;
    }
}
