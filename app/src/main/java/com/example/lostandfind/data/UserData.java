package com.example.lostandfind.data;

import java.time.LocalDateTime;

public class UserData {
    private String UID;
    private String email;
    private String name;
    private String localTime;

    public UserData(String UID, String email, String name) {
        this.UID = UID;
        this.email = email;
        this.name = name;
        localTime = LocalDateTime.now().toString();
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocalTime() {
        return localTime;
    }

    public void setLocalTime(String localTime) {
        this.localTime = localTime;
    }
}
