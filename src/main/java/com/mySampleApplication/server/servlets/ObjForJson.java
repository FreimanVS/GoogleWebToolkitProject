package com.mySampleApplication.server.servlets;

public class ObjForJson {
    public String name;

    public ObjForJson() {
    }

    public ObjForJson(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
