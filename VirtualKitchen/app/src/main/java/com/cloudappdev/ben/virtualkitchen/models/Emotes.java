package com.cloudappdev.ben.virtualkitchen.models;

import java.io.Serializable;

/**
 * Created by ben on 30/11/16.
 */

public class Emotes implements Serializable {
    String label;
    int imageUrl;

    public Emotes(String label, int imageUrl) {
        this.label = label;
        this.imageUrl = imageUrl;
    }

    public String getLabel() {
        return label;
    }

    public int getImageUrl() {
        return imageUrl;
    }
}
