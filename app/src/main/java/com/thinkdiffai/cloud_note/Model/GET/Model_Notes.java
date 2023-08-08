package com.thinkdiffai.cloud_note.Model.GET;

import com.thinkdiffai.cloud_note.Model.Model_List_Note;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Model_Notes {
    @SerializedName("notes")
    private List<Model_List_Note> list;

    public Model_Notes() {
    }

    public List<Model_List_Note> getList() {
        return list;
    }

    public void setList(List<Model_List_Note> list) {
        this.list = list;
    }
}
