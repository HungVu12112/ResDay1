package com.thinkdiffai.cloud_note.Model.GET;

import com.thinkdiffai.cloud_note.Model.Color;
import com.google.gson.annotations.SerializedName;

public class ModelTextNote {

    @SerializedName("idNote")
    private int id;
    @SerializedName("title")

    private String title;
    @SerializedName("color")

    private Color color;
    @SerializedName("type")
    private String type;
    @SerializedName("dueAt")
    private String duaAt;
    @SerializedName("createAt")
    private String createAt;
    @SerializedName("data")

    private String data;
    @SerializedName("doneNote")
    private int doneNote;
    @SerializedName("idUser")
    private int idUser;
    @SerializedName("lock")
    private String lock;
    @SerializedName("pinned")
    private int pinned;
    @SerializedName("remindAt")
    private String reminAt;
    @SerializedName("share")
    private String share;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getDoneNote() {
        return doneNote;
    }

    public void setDoneNote(int doneNote) {
        this.doneNote = doneNote;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
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

    public String getShare() {
        return share;
    }

    public void setShare(String share) {
        this.share = share;
    }
}
