package com.example.cinema.models;

import jakarta.persistence.*;

@Entity //Declare that this is an entity to be used in the database
@Table(name = "users") //Set the table this entity should be stored in
@Inheritance(strategy = InheritanceType.JOINED) //Tell the code to join the tables of classes that extend this class
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    protected String userName;
    protected String password;

    //--- Constructor: Start ---

    //User Constructor
    //  Needs a username and a password
    //  There are no checks being done at this level to determine password strength
    //  Sets the default type to null as we should not have a default user anywhere
    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    //Default User Constructor
    //  Only here to stop intelliJ from throwing errors
    //  Should not be used
    public User() {
    }

    //--- Constructor: End ---

    //--- Getters: Start ---

    public Long getId() {
      return id;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    //--- Getters: End ---
    //--- Setters: Start ---

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    //--- Setters: End ---
}
