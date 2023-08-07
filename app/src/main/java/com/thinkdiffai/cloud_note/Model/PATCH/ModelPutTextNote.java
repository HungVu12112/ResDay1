package com.thinkdiffai.cloud_note.Model.PATCH;

import com.thinkdiffai.cloud_note.Model.Color;
import com.google.gson.annotations.SerializedName;

public class ModelPutTextNote {
    @SerializedName("title")
    private String title;
    @SerializedName("color")
    private Color color;
    @SerializedName("type")
    private String type;
    @SerializedName("dueAt")
    private String duaAt;
    @SerializedName("data")
    private String data;
    @SerializedName("lock")
    private String lock;
    @SerializedName("pinned")
    private int pinned;
    @SerializedName("remindAt")
    private String reminAt;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDuaAt() {
        return duaAt;
    }

    public void setDuaAt(String duaAt) {
        this.duaAt = duaAt;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getLock() {
        return lock;
    }

    public void setLock(String lock) {
        this.lock = lock;
    }

    public int getPinned() {
        return pinned;
    }

    public void setPinned(int pinned) {
        this.pinned = pinned;
    }

    public String getReminAt() {
        return reminAt;
    }

    public void setReminAt(String reminAt) {
        this.reminAt = reminAt;
    }
}
