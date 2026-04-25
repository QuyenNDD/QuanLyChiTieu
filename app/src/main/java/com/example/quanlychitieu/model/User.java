package com.example.quanlychitieu.model;

public class User {
    private int userID;
    private String userName;
    private String passWord;
    private String Email;

    public User() {
    }

    public User(int userID, String userName, String email) {
        this.userID = userID;
        this.userName = userName;
        Email = email;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }
}
