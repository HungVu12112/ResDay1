package com.thinkdiffai.cloud_note.Model.POST;

import com.google.gson.annotations.SerializedName;

public class RegiterReq {
    @SerializedName("user_name")
    private String user_name;
    @SerializedName("password")
    private String passwd;
    @SerializedName("name")
    private String name;
    @SerializedName("gmail")
    private String email;

    public RegiterReq(String user_name, String passwd, String name, String email) {
        this.user_name = user_name;
        this.passwd = passwd;
        this.name = name;
        this.email = email;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
