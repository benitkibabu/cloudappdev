package com.cloudappdev.ben.virtualkitchen.models;

/**
 * Created by Ben on 21/10/2016.
 */

public class Food {
    String uri;
    String label;

    public Food(String uri, String label) {
        this.uri = uri;
        this.label = label;
    }

    public String getUri() {
        return uri;
    }

    public String getLabel() {
        return label;
    }
}
