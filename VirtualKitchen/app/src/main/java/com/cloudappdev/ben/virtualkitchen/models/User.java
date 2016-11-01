package com.cloudappdev.ben.virtualkitchen.models;

import java.io.Serializable;

/**
 * Created by Ben on 31/10/2016.
 */

public class User implements Serializable{
    int id;
    String loginType;
    String userid;
    String name;
    String email;
    String imageUrl;

    public User(int id, String loginType, String userid, String name, String email, String imageUrl) {
        this.id = id;
        this.loginType = loginType;
        this.userid = userid;
        this.name = name;
        this.email = email;
        this.imageUrl = imageUrl;
    }

    public User(String loginType, String userid, String name, String email, String imageUrl) {
        this.loginType = loginType;
        this.userid = userid;
        this.name = name;
        this.email = email;
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public int getId() {
        return id;
    }

    public String getLoginType() {
        return loginType;
    }

    public String getUserid() {
        return userid;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}
