package com.cloudappdev.ben.virtualkitchen.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Benit Kibabu on 25/05/2017.
 */

public class ItemOffer implements Serializable {
    @SerializedName("merchant")
    String merchant;
    @SerializedName("domain")
    String domain;
    @SerializedName("title")
    String title;
    @SerializedName("currency")
    String currency;
    @SerializedName("list_price")
    double list_price;
    @SerializedName("price")
    double price;
    @SerializedName("shipping")
    String shipping;
    @SerializedName("condition")
    String condition;
    @SerializedName("availability")
    String availability;
    @SerializedName("link")
    String link;
    @SerializedName("updated_t")
    double updated_t;

    public String getMerchant() {
        return merchant;
    }

    public void setMerchant(String merchant) {
        this.merchant = merchant;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public double getList_price() {
        return list_price;
    }

    public void setList_price(double list_price) {
        this.list_price = list_price;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getShipping() {
        return shipping;
    }

    public void setShipping(String shipping) {
        this.shipping = shipping;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public double getUpdated_t() {
        return updated_t;
    }

    public void setUpdated_t(double updated_t) {
        this.updated_t = updated_t;
    }
}
