package com.thinkdiffai.cloud_note.Model;

import com.google.gson.annotations.SerializedName;

public class Color {
    @SerializedName("a")
    private float a;
    @SerializedName("b")
    private int b;
    @SerializedName("g")
    private int g;
    @SerializedName("r")
    private int r;
    public Color() {
    }

    public Color(float a, int b, int g, int r) {
        this.a = a;
        this.b = b;
        this.g = g;
        this.r = r;
    }

    public float getA() {
        return a;
    }

    public void setA(float a) {
        this.a = a;
    }

    public int getB() {
        return b;
    }

    public void setB(int b) {
        this.b = b;
    }

    public int getG() {
        return g;
    }

    public void setG(int g) {
        this.g = g;
    }

    public int getR() {
        return r;
    }

    public void setR(int r) {
        this.r = r;
    }
}
