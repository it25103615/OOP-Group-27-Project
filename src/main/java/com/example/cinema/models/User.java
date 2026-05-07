package com.example.cinema.models;

public class User {
    protected String userName;
    protected String password;
    protected String type;

    //--- Constructor: Start ---

    //User Constructor
    //  Needs a username and a password
    //  There are no checks being done at this level to determine password strength
    //  Sets the default type to null as we should not have a default user anywhere
    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;
        this.type = null;
    }

    //--- Constructor: End ---

    //--- Getters: Start ---

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getType() {
        return type;
    }

    //--- Getters: End ---
    //--- Setters: Start ---

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setType(String type) {
        this.type = type;
    }

    //--- Setters: End ---
}
