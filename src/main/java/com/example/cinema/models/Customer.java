package com.example.cinema.models;

public class Customer extends User{
    protected int phoneNumber ;
    protected String billingAddresses ;
    protected String orderHistory ;
    protected CreditCard creditCard ;

    public Customer(String userName, String password, int phoneNumber, String billingAddresses, String orderHistory, CreditCard creditCard) {
        super(userName, password);
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

    public CreditCard getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(CreditCard creditCard) {
        this.creditCard = creditCard;
    }
}
