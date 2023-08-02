package com.cloudappdev.ben.virtualkitchen.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Benit Kibabu on 25/05/2017.
 */

public class UPCItem implements Serializable {
    @SerializedName("ean")
    String ean;
    @SerializedName("title")
    String title;
    @SerializedName("description")
    String description;
    @SerializedName("elid")
    String elid;
    @SerializedName("brand")
    String brand;
    @SerializedName("model")
    String model;
    @SerializedName("color")
    String color;
    @SerializedName("size")
    String size;
    @SerializedName("weight")
    String weight;
    @SerializedName("lowest_recoded_price")
    double lowest_recoded_price;

    @SerializedName("images")
    List<String> images;

    @SerializedName("offers")
    List<ItemOffer> offers;

    public List<ItemOffer> getOffers() {
        return offers;
    }

    public void setOffers(List<ItemOffer> offers) {
        this.offers = offers;
    }

    public String getEan() {
        return ean;
    }

    public void setEan(String ean) {
        this.ean = ean;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getElid() {
        return elid;
    }

    public void setElid(String elid) {
        this.elid = elid;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public double getLowest_recoded_price() {
        return lowest_recoded_price;
    }

    public void setLowest_recoded_price(double lowest_recoded_price) {
        this.lowest_recoded_price = lowest_recoded_price;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }
}
