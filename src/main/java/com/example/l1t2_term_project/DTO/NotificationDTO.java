package com.example.l1t2_term_project.DTO;

import java.io.Serializable;

public class NotificationDTO implements Serializable {
    private String notification;

    public NotificationDTO(String notification) {
        this.notification = notification;
    }

    public String getNotification() {
        return notification;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }
}
