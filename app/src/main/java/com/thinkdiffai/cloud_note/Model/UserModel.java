package com.thinkdiffai.cloud_note.Model;

import com.google.gson.annotations.SerializedName;

public class UserModel {
    @SerializedName("id")
    private int id;
    @SerializedName("name")
   private String name;
    @SerializedName("df_screen")
   private String df_screen;
    @SerializedName("gmail")
   private String email;
    @SerializedName("df_color")
   private  Color df_color;

    public UserModel() {
    }

    public UserModel(int id, String name, String df_screen, String email, Color df_color) {
        this.id = id;
        this.name = name;
        this.df_screen = df_screen;
        this.email = email;
        this.df_color = df_color;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDf_screen() {
        return df_screen;
    }

    public void setDf_screen(String df_screen) {
        this.df_screen = df_screen;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Color getDf_color() {
        return df_color;
    }

    public void setDf_color(Color df_color) {
        this.df_color = df_color;
    }
}
