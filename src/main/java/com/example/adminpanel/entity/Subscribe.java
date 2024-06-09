package com.example.adminpanel.entity;

public class Subscribe {
    private int subscribe_id;
    private String subscribe_type;

    public void setSubscribeKey(String subscribe_key) {
        this.subscribe_key = subscribe_key;
    }
    public String getSubscribeKey() {
        return this.subscribe_key;
    }
    private String subscribe_key;
    public int getSubscribeId() {
        return subscribe_id;
    }

    public void setSubscribeId(int subscribe_id) {
        this.subscribe_id = subscribe_id;
    }

    public void setSubscribeType(String subscribe_type) {
        this.subscribe_type = subscribe_type;
    }

    public String getSubscribeType() {
        return subscribe_type;
    }

    @Override
    public String toString() {
        return "Подписка на данный момент активна : " + subscribe_type;
    }
}
