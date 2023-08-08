package com.thinkdiffai.cloud_note.Model;

import com.google.gson.annotations.SerializedName;

public class LoginModel {
    @SerializedName("jwt")
    private String jwt;
    @SerializedName("message")
    private String message;
    @SerializedName("user")
    private UserModel user;
    @SerializedName("status")
    private int status;

    public LoginModel() {
    }

    public LoginModel(String jwt, String message, UserModel user, int status) {
        this.jwt = jwt;
        this.message = message;
        this.user = user;
        this.status = status;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
