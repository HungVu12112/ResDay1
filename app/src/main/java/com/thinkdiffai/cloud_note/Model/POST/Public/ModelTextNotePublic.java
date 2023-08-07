package com.thinkdiffai.cloud_note.Model.POST.Public;

import com.thinkdiffai.cloud_note.Model.Color;
import com.google.gson.annotations.SerializedName;

public class ModelTextNotePublic {
    @SerializedName("title")
    private String titile;
    @SerializedName("data")
    private String data;
    @SerializedName("color")
    private Color color;
    @SerializedName("type")
    private String type;
    @SerializedName("notePublic")
    private int notePublic;
    @SerializedName("dueAt")
    private String dueAt;
    @SerializedName("lock")
    private String lock ;
    @SerializedName("remindAt")
    private String remindAt;
    @SerializedName("pinned")
    private boolean pinned;
    @SerializedName("share")
    private String share;

    public String getTitile() {
        return titile;
    }

    public void setTitile(String titile) {
        this.titile = titile;
    }

    public String getData() {
        return data;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getNotePublic() {
        return notePublic;
    }

    public void setNotePublic(int notePublic) {
        this.notePublic = notePublic;
    }

    public String getDueAt() {
        return dueAt;
    }

    public void setDueAt(String dueAt) {
        this.dueAt = dueAt;
    }

    public String getLock() {
        return lock;
    }

    public void setLock(String lock) {
        this.lock = lock;
    }

    public String getRemindAt() {
        return remindAt;
    }

    public void setRemindAt(String remindAt) {
        this.remindAt = remindAt;
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
}
