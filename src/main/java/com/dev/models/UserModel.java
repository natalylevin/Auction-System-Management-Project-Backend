package com.dev.models;

import com.dev.objects.User;

public class UserModel {

    private int id;
    private String username;

    private Double balance;

    private Boolean isAdmin;

    public UserModel(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.balance = user.getBalance();
        this.isAdmin = user.isAdmin();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public Boolean getAdmin() {
        return isAdmin;
    }

    public void setAdmin(Boolean admin) {
        isAdmin = admin;
    }
}
