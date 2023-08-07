package com.thinkdiffai.cloud_note.Model;

import com.thinkdiffai.cloud_note.Model.GET.ModelUser;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ModelListLastUser {
    @SerializedName("data")
    private List<ModelUser> modelUser;

    public List<ModelUser> getModelUser() {
        return modelUser;
    }

    public void setModelUser(List<ModelUser> modelUser) {
        this.modelUser = modelUser;
    }
}
