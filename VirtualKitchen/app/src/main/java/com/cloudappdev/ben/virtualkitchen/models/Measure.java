package com.cloudappdev.ben.virtualkitchen.models;

/**
 * Created by Ben on 21/10/2016.
 */

public class Measure {
    String uri;
    String label;

    public Measure(String uri, String label) {
        this.uri = uri;
        this.label = label;
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
}
