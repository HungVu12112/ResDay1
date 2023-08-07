package com.thinkdiffai.cloud_note.Model.GET;

import com.google.gson.annotations.SerializedName;

public class ModelGetNoteText {
    @SerializedName("note")
    private ModelTextNote modelTextNote;

    public ModelGetNoteText() {
    }

    public ModelTextNote getModelTextNote() {
        return modelTextNote;
    }

    public void setModelTextNote(ModelTextNote modelTextNote) {
        this.modelTextNote = modelTextNote;
    }
}
