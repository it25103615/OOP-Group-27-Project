package com.example.cinema.models;

public class Customer extends User{
    protected int phoneNumber ;
    protected String billingAddresses ;
    protected String orderHistory ;
    protected int creditCard ;

    public Customer(int phoneNumber, String billingAddresses, String orderHistory, int creditCard) {
        super();
        this.phoneNumber = phoneNumber;
        this.billingAddresses = billingAddresses;
        this.orderHistory = orderHistory;
        this.creditCard = creditCard;
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

    public String getOrderHistory() {
        return orderHistory;
    }

    public void setOrderHistory(String orderHistory) {
        this.orderHistory = orderHistory;
    }

    public int getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(int creditCard) {
        this.creditCard = creditCard;
    }
}
