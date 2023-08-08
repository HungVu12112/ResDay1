package com.thinkdiffai.cloud_note.Model.GET;

public class ModelCheckList  {
    private int id;
    private String content;
    private int  status;

    public ModelCheckList() {
    }

    public ModelCheckList(int id, String content, int status) {
        this.id = id;
        this.content = content;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }


}
