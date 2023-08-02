package com.cloudappdev.ben.virtualkitchen.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Benit Kibabu on 25/05/2017.
 */

public class MyRecipes implements Serializable {
    @SerializedName("id")
    int id;
    @SerializedName("uri")
    String uri;
    @SerializedName("label")
    String label;
    @SerializedName("image")
    String image;
    @SerializedName("source")
    String source;
    @SerializedName("url")
    String url;
    @SerializedName("shareAs")
    String shareAs;
    @SerializedName("yield")
    double yield;
    @SerializedName("dietLabels")
    String dietLabels;
    @SerializedName("healthLabels")
    String healthLabels;
    @SerializedName("cautions")
    String cautions;
    @SerializedName("ingredientLines")
    String ingredientLines;
    @SerializedName("calories")
    double calories;
    @SerializedName("totalWeight")
    double totalWeight;
    @SerializedName("app_user_id")
    int user_id;
    @SerializedName("ingredientCount")
    int ingredientCount;

    public int getIngredientCount() {
        return ingredientCount;
    }

    public void setIngredientCount(int ingredientCount) {
        this.ingredientCount = ingredientCount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getShareAs() {
        return shareAs;
    }

    public void setShareAs(String shareAs) {
        this.shareAs = shareAs;
    }

    public double getYield() {
        return yield;
    }

    public void setYield(double yield) {
        this.yield = yield;
    }

    public String getDietLabels() {
        return dietLabels;
    }

    public void setDietLabels(String dietLabels) {
        this.dietLabels = dietLabels;
    }

    public String getHealthLabels() {
        return healthLabels;
    }

    public void setHealthLabels(String healthLabels) {
        this.healthLabels = healthLabels;
    }

    public String getCautions() {
        return cautions;
    }

    public void setCautions(String cautions) {
        this.cautions = cautions;
    }

    public String getIngredientLines() {
        return ingredientLines;
    }

    public void setIngredientLines(String ingredientLines) {
        this.ingredientLines = ingredientLines;
    }

    public double getCalories() {
        return calories;
    }

    public void setCalories(double calories) {
        this.calories = calories;
    }

    public double getTotalWeight() {
        return totalWeight;
    }

    public void setTotalWeight(double totalWeight) {
        this.totalWeight = totalWeight;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
}
