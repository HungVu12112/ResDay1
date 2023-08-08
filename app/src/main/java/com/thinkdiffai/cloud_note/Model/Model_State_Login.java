package com.thinkdiffai.cloud_note.Model;

public class Model_State_Login {
    private int idUer;
    private String jwt;
    private int state;

    public Model_State_Login() {
    }

    public Model_State_Login(int idUer, String jwt, int state) {
        this.idUer = idUer;
        this.jwt = jwt;
        this.state = state;
    }

    public int getIdUer() {
        return idUer;
    }

    public void setIdUer(int idUer) {
        this.idUer = idUer;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
