package com.cloudappdev.ben.virtualkitchen.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Benit Kibabu on 25/05/2017.
 */

public class Hit implements Serializable {
    @SerializedName("recipe")
    Recipe recipe;
    @SerializedName("bookmarked")
    boolean bookmarked;
    @SerializedName("bought")
    boolean bought;

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public boolean isBookmarked() {
        return bookmarked;
    }

    public void setBookmarked(boolean bookmarked) {
        this.bookmarked = bookmarked;
    }

    public boolean isBought() {
        return bought;
    }

    public void setBought(boolean bought) {
        this.bought = bought;
    }
}
