package com.example.cinema.models;

import jakarta.persistence.*;

@Entity
@Table(name = "admins")
@DiscriminatorValue("ADMIN")
public class Admin extends User{
    public Admin(String userName, String password) {
        super(userName, password);
    }

    //Default Admin Constructor
    //  Only here to stop intellij from throwing errors
    //  Should not be used
    public Admin() {
        super();
    }
}
