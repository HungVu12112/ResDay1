package com.thinkdiffai.cloud_note.Model.POST;

import com.google.gson.annotations.SerializedName;

public class ModelPostImage {
    @SerializedName("data")
    private Data data;
    @SerializedName("status")
    private int status;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data{
        @SerializedName("url_viewer")
        private String  Url_viewer;

        public String getUrl_viewer() {
            return Url_viewer;
        }

        public void setUrl_viewer(String url_viewer) {
            Url_viewer = url_viewer;
        }
    }
}
