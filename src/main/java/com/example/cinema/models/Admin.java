package com.example.cinema.models;

public class Admin extends User{
    public Admin(String userName, String password) {
        super(userName, password);
        this.type = "ADMIN";
    }
}
