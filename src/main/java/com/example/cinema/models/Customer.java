package com.example.cinema.models;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "customers")
@DiscriminatorValue("CUSTOMER")
public class Customer extends User{
    protected int phoneNumber ;
    protected String billingAddress ;
    @Transient
    protected List<String> orderHistory ;

    @ManyToOne
    @JoinColumn(name = "credit_card_id")
    protected CreditCard creditCard ;

    public Customer(String userName, String password, int phoneNumber, String billingAddress) {
        super(userName, password);
        this.phoneNumber = phoneNumber;
        this.billingAddress = billingAddress;
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

    public String getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(String billingAddress) {
        this.billingAddress = billingAddress;
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

    public CreditCard getCreditCard() {return creditCard;}

    public void setCreditCard(CreditCard creditCard) {this.creditCard = creditCard;}
}