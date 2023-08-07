package com.thinkdiffai.cloud_note.Model.POST;

import com.google.gson.annotations.SerializedName;

public class LoginReq {
    @SerializedName("user_name")
    private String user_name;
    @SerializedName("password")
    private String passwd;

    public LoginReq(String user_name, String passwd) {
        this.user_name = user_name;
        this.passwd = passwd;
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
}
