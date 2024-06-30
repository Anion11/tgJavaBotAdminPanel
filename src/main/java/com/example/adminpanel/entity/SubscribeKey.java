package com.example.adminpanel.entity;

public class SubscribeKey {
    private Long id;
    private Long subscribeId;
    private String key;
    private String type;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setSubscribeId(Long id) {
        this.subscribeId = id;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Long getSubscribeId() {
        return subscribeId;
    }

    public String getKey() {
        return key;
    }

    @Override
    public String toString() {
        return key + " : " + type;
    }
}
