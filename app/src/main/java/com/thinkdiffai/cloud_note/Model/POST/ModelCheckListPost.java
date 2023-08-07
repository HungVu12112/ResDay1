package com.thinkdiffai.cloud_note.Model.POST;

import com.google.gson.annotations.SerializedName;

public class ModelCheckListPost {
    @SerializedName("content")
    private String content;
    @SerializedName("status")
    private boolean  status;

    public ModelCheckListPost() {
    }



    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
