package com.example.adminpanel.entity;

public class SubscribePost {
    private String descr;
    private String src;
    private String type;

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public void setSrc(String rsc) {
        this.src = rsc;
    }

    public String getDescr() {
        return descr;
    }

    public String getSrc() {
        return src;
    }
}
