package com.thinkdiffai.cloud_note.Model.GET;

import com.google.gson.annotations.SerializedName;

public class ModelUser {
    @SerializedName("id")
    private int id;
    @SerializedName("gmail")
    private String gmail;
    @SerializedName("linkAvatar")
    private String linkAvatar;
    @SerializedName("name")
    private String name;
    @SerializedName("user_name")
    private String user_name;
    @SerializedName("role")
    private String role;
    @SerializedName("createAt")
    private String createAt;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGmail() {
        return gmail;
    }

    public void setGmail(String gmail) {
        this.gmail = gmail;
    }

    public String getLinkAvatar() {
        return linkAvatar;
    }

    public void setLinkAvatar(String linkAvatar) {
        this.linkAvatar = linkAvatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }
}
