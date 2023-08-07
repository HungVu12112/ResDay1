package com.thinkdiffai.cloud_note.Model;

public class Setting_Sort {
    private int id;
    private String sortName;

    public Setting_Sort() {
    }

    public Setting_Sort(int id, String sortName) {
        this.id = id;
        this.sortName = sortName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSortName() {
        return sortName;
    }

    public void setSortName(String sortName) {
        this.sortName = sortName;
    }
}
