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
    String password;

    public User(){}

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

    public User(String loginType, String userid, String name, String email, String imageUrl, String password) {
        this.loginType = loginType;
        this.userid = userid;
        this.name = name;
        this.email = email;
        this.imageUrl = imageUrl;
        this.password = password;
    }

    public User(int id, String loginType, String userid, String name, String email, String imageUrl, String password) {
        this.id = id;
        this.loginType = loginType;
        this.userid = userid;
        this.name = name;
        this.email = email;
        this.imageUrl = imageUrl;
        this.password = password;
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

    public String getPassword() {
        return password;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
