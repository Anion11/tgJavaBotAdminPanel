package com.example.adminpanel.entity;

public class Subscribe {
    private Long subscribe_id;
    private String subscribe_type;
    private String subscribe_descr;

    public String getSubscribeDescr() {
        return subscribe_descr;
    }

    public void setSubscribeDescr(String subscribe_descr) {
        this.subscribe_descr = subscribe_descr;
    }

    public Long getSubscribeId() {
        return subscribe_id;
    }
    public String getStringSubscribeId() {
        return String.valueOf(subscribe_id);
    }

    public void setSubscribeId(Long subscribe_id) {
        this.subscribe_id = subscribe_id;
    }

    public void setSubscribeType(String subscribe_type) {
        this.subscribe_type = subscribe_type;
    }

    public String getSubscribeType() {
        return subscribe_type;
    }
}
