package com.binotify.models;

import com.binotify.database.Database;

public class SubscriptionStatus extends Database {
    private Boolean status;
    private Integer id;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Integer getId() {
        try {
            this.executeQuery("");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
