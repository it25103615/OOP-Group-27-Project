package com.example.cinema.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "credit_cards")
public class CreditCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String cardHolderName;
    private Long cardNumber;
    private String expiry;
    private int cvv;

    // Default constructor (Required by JPA)
    public CreditCard() {
    }

    // Parameterized constructor
    public CreditCard(String cardHolderName, Long cardNumber, String expiry, int cvv) {
        this.cardHolderName = cardHolderName;
        this.cardNumber = cardNumber;
        this.expiry = expiry;
        this.cvv = cvv;
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public String getCardHolderName() {
        return cardHolderName;
    }

    public void setCardHolderName(String cardHolderName) {
        this.cardHolderName = cardHolderName;
    }

    public Long getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(Long cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getExpiry() {
        return expiry;
    }

    public void setExpiry(String expiry) {
        this.expiry = expiry;
    }

    public int getCvv() {
        return cvv;
    }

    public void setCvv(int cvv) {
        this.cvv = cvv;
    }
}