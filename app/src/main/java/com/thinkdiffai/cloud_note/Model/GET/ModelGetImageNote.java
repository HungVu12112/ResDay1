package com.thinkdiffai.cloud_note.Model.GET;

import com.google.gson.annotations.SerializedName;

public class ModelGetImageNote {
    @SerializedName("note")
    private ModelImageNote note;

    public ModelImageNote getNote() {
        return note;
    }

    public void setNote(ModelImageNote note) {
        this.note = note;
    }
}
