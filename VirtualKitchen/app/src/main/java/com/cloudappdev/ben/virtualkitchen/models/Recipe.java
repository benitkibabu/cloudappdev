package com.cloudappdev.ben.virtualkitchen.models;

/**
 * Created by Ben on 21/10/2016.
 */

public class Recipe {
    String uri;
    String label;
    String imageUrl;
    String source;
    String url;
    double yield;
    String level;
    String summary;
    float caleries;
    float totalWeight;
    Ingredient[] ingredients;
    NutrientInfo[] totalNutrients;
    NutrientInfo[] totalDaily;
    String[] dietLabels;
    String[] healthLabels;

    public Recipe(String uri, String label, String imageUrl, String source, String url, double yield, String level, String summary, float caleries, float totalWeight, Ingredient[] ingredients, NutrientInfo[] totalNutrients, NutrientInfo[] totalDaily, String[] dietLabels, String[] healthLabels) {
        this.uri = uri;
        this.label = label;
        this.imageUrl = imageUrl;
        this.source = source;
        this.url = url;
        this.yield = yield;
        this.level = level;
        this.summary = summary;
        this.caleries = caleries;
        this.totalWeight = totalWeight;
        this.ingredients = ingredients;
        this.totalNutrients = totalNutrients;
        this.totalDaily = totalDaily;
        this.dietLabels = dietLabels;
        this.healthLabels = healthLabels;
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

    public double getYield() {
        return yield;
    }

    public String getLevel() {
        return level;
    }

    public String getSummary() {
        return summary;
    }

    public float getCaleries() {
        return caleries;
    }

    public float getTotalWeight() {
        return totalWeight;
    }

    public Ingredient[] getIngredients() {
        return ingredients;
    }

    public NutrientInfo[] getTotalNutrients() {
        return totalNutrients;
    }

    public NutrientInfo[] getTotalDaily() {
        return totalDaily;
    }

    public String[] getDietLabels() {
        return dietLabels;
    }

    public String[] getHealthLabels() {
        return healthLabels;
    }
}
