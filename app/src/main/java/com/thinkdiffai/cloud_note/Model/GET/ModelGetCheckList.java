package com.thinkdiffai.cloud_note.Model.GET;

import com.thinkdiffai.cloud_note.Model.ModelTextNoteCheckList;
import com.google.gson.annotations.SerializedName;

public class ModelGetCheckList {
    @SerializedName("note")
    private ModelTextNoteCheckList modelTextNoteCheckList;

    public ModelTextNoteCheckList getModelTextNoteCheckList() {
        return modelTextNoteCheckList;
    }

    public void setModelTextNoteCheckList(ModelTextNoteCheckList modelTextNoteCheckList) {
        this.modelTextNoteCheckList = modelTextNoteCheckList;
    }
}
