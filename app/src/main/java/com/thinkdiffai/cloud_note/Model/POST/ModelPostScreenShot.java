package com.thinkdiffai.cloud_note.Model.POST;

import com.thinkdiffai.cloud_note.Model.Color;
import com.google.gson.annotations.SerializedName;

public class ModelPostScreenShot {
    @SerializedName("data")
    private String data;

    @SerializedName("color")
    private Color color;

    @SerializedName("title")
    private String title;
    @SerializedName("type")
    private String type;
    @SerializedName("metaData")
    private String images;

    @SerializedName("dueAt")
    private String duaAt;
    @SerializedName("lock")
    private String lock;
    @SerializedName("pinned")
    private boolean pinned;
    @SerializedName("remindAt")
    private String reminAt;

    @SerializedName("share")
    private String share;

    @SerializedName("notePublic")
    private int notePublic;

    public String getData() {
        return data;
    }

    public int getNotePublic() {
        return notePublic;
    }

    public void setNotePublic(int notePublic) {
        this.notePublic = notePublic;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public ModelPostScreenShot() {
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

    public String getLock() {
        return lock;
    }

    public void setLock(String lock) {
        this.lock = lock;
    }

    public boolean isPinned() {
        return pinned;
    }

    public void setPinned(boolean pinned) {
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
