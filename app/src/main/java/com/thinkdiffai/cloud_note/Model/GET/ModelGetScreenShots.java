package com.thinkdiffai.cloud_note.Model.GET;

import com.google.gson.annotations.SerializedName;

public class ModelGetScreenShots {
    @SerializedName("note")
    private ModelScreenShots notes;

    public ModelScreenShots getNotes() {
        return notes;
    }

    public void setNotes(ModelScreenShots notes) {
        this.notes = notes;
    }
}
