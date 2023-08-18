package com.metanoiasystem.go4lunchxoc.data.models;

public class User {


    private String uid;
    private String username;
    private String userMail;
    private String urlPictureUser;


    public User() { }

    public User(String uid, String username,String userMail, String urlPictureUser) {
        this.uid = uid;
        this.username = username;
        this.userMail = userMail;
        this.urlPictureUser = urlPictureUser;

    }

    // --- GETTERS ---
    public String getUid() { return uid; }
    public String getUsername() { return username; }
    public String getUserMail() { return userMail; }
    public String getUrlPictureUser() { return urlPictureUser;}


    // --- SETTERS ---
    public void setUsername(String username) { this.username = username; }
    public void setUid(String uid) { this.uid = uid; }
    public void setUserMail(String userMail) { this.userMail = userMail; }
    public void setUrlPictureUser ( String urlPictureUser) { this.urlPictureUser = urlPictureUser;}
}
