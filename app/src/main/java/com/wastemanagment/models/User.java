package com.wastemanagment.models;

public class User {

    public String uid, username, email, phone, userType;
    public Boolean  available;

    public User(String uid, String username, String email, String phone, String userType, Boolean available) {
        this.uid = uid;
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.userType = userType;
        this.available = available;
    }

    public User(String username, String phone) {
        this.username = username;
        this.phone = phone;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }
}
