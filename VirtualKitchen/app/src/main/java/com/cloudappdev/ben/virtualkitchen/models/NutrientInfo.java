package com.cloudappdev.ben.virtualkitchen.models;

/**
 * Created by Ben on 21/10/2016.
 */

public class NutrientInfo {
    String uri;
    String label;
    float quantity;
    String unit;

    public NutrientInfo(String uri, String label, float quantity, String unit) {
        this.uri = uri;
        this.label = label;
        this.quantity = quantity;
        this.unit = unit;
    }

    public String getUri() {
        return uri;
    }

    public String getLabel() {
        return label;
    }

    public float getQuantity() {
        return quantity;
    }

    public String getUnit() {
        return unit;
    }
}
