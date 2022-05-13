package com.example.lostandfind.chatDB;


import com.google.firebase.firestore.Exclude;

import java.io.Serializable;

public class ChatRooms implements Serializable{
    @Exclude private String id;

    String receiverUID;
    String receiverName;
    String senderUID;
    String senderName;

    public ChatRooms(){ }

    public ChatRooms(String receiverUID, String receiverName, String senderUID, String senderName){
        this.receiverUID = receiverUID;
        this.receiverName = receiverName;
        this.senderUID = senderUID;
        this.senderName = senderName;
    }

    public String getSenderUID() {
        return senderUID;
    }
    public void setSenderUID(String senderUID) {
        this.senderUID = senderUID;
    }
    public String getSenderName() {
        return senderName;
    }
    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getReceiverName() {
        return receiverName;
    }
    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }
    public String getReceiverUID() {
        return receiverUID;
    }
    public void setReceiverUID(String receiverUID) {
        this.receiverUID = receiverUID;
    }
}
