package com.thinkdiffai.cloud_note.Model.POST;

import com.thinkdiffai.cloud_note.Model.Color;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ModelTextNoteCheckListPost {


    @SerializedName("title")

    private String title;
    @SerializedName("color")

    private Color color;
    @SerializedName("type")

    private String type;
    @SerializedName("dueAt")

    private String duaAt;

    @SerializedName("data")

    private List<ModelCheckListPost> data;

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

    public int getNotePublic() {
        return notePublic;
    }

    public void setNotePublic(int notePublic) {
        this.notePublic = notePublic;
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


    public List<ModelCheckListPost> getData() {
        return data;
    }

    public void setData(List<ModelCheckListPost> data) {
        this.data = data;
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

    public String getShare() {
        return share;
    }

    public void setShare(String share) {
        this.share = share;
    }

    public String getReminAt() {
        return reminAt;
    }

    public void setReminAt(String reminAt) {
        this.reminAt = reminAt;
    }
}
