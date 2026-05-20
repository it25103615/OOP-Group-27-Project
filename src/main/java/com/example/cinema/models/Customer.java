package com.example.cinema.models;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "customers")
@DiscriminatorValue("CUSTOMER")
public class Customer extends User{
    protected int phoneNumber ;
    protected String billingAddresses ;
    @Transient
    protected List<String> orderHistory ;
    //protected CreditCard creditCard ;

    public Customer(String userName, String password, int phoneNumber, String billingAddresses) {
        super(userName, password);
        this.phoneNumber = phoneNumber;
        this.billingAddresses = billingAddresses;
    }

    public Customer(String userName, String password) {
        super(userName, password);
    }

    public Customer() {
    }

    public int getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getBillingAddresses() {
        return billingAddresses;
    }

    public void setBillingAddresses(String billingAddresses) {
        this.billingAddresses = billingAddresses;
    }

    public List<String> getOrderHistory() {
        return orderHistory;
    }

    public void addToOrderHistory(String orderID) {
        if (this.orderHistory == null) {
            this.orderHistory = new ArrayList<>();
        }
        this.orderHistory.add(orderID);
    }

    //public CreditCard getCreditCard() {return creditCard;}

    //public void setCreditCard(CreditCard creditCard) {this.creditCard = creditCard;}
}
