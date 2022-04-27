package com.example.lostandfind.data;

import androidx.annotation.NonNull;

import com.google.firebase.Timestamp;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Post implements Serializable {
    //Post Meta Data
    private String time; //Post 작성 날짜

    //Post Information
    private String imageName; //이미지
    private String title; //Post 제목
    private String category; //카테고리
    private String getting_item_place; //장소
    private String getting_item_time; //분실,습득 날짜
    private String status; //상태
    private String text; //Post 본문

    //User Data
    /*User Data는 Post 작성시 Current User data를 이용하여 저장한다.*/
    private String user_email;
    private String user_name;
    private String user_UID;

    private String pattern = "yyyy-MM-dd HH:mm";
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);


    public Post() {}

    //Constructor
    public Post(String imageName, String title, String category, String getting_item_place, String getting_item_time, String status, String text, String user_email, String user_name, String user_UID) {
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
        this.time = simpleDateFormat.format(new Date()).toString();

        this.imageName = imageName;
        this.title = title;
        this.category = category;
        this.getting_item_place = getting_item_place;
        this.getting_item_time = getting_item_time;
        this.status = status;
        this.text = text;

        this.user_email = user_email;
        this.user_name = user_name;
        this.user_UID = user_UID;
    }

    public String getGetting_item_place() {
        return getting_item_place;
    }

    public void setGetting_item_place(String getting_item_place) {
        this.getting_item_place = getting_item_place;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getGetting_item_time() {
        return getting_item_time;
    }

    public void setGetting_item_time(String getting_item_time) {
        this.getting_item_time = getting_item_time;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_UID() {
        return user_UID;
    }

    public void setUser_UID(String user_UID) {
        this.user_UID = user_UID;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    @NonNull
    @Override
    public String toString() {
        return this.getImageName() + this.getTitle();
    }
}
