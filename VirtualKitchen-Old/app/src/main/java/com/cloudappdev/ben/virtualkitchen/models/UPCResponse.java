package com.cloudappdev.ben.virtualkitchen.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Benit Kibabu on 25/05/2017.
 */

public class UPCResponse implements Serializable {
    @SerializedName("code")
    String code;
    @SerializedName("total")
    int total;

    @SerializedName("items")
    List<UPCItem> items;

    public List<UPCItem> getItems() {
        return items;
    }

    public void setItems(List<UPCItem> items) {
        this.items = items;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
