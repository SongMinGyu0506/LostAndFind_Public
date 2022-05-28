package com.example.lostandfind.data;

public class Token {
    private String token;
    private String uid;

    public Token() {}
    public Token(String token, String uid) {
        this.token = token;
        this.uid = uid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
