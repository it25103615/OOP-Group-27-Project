package com.example.cinema.models;

import jakarta.persistence.Embeddable;

/**
 * One line on a snack order (embeddable value object).
 */
@Embeddable
public class OrderLineItem {

    private String snackId;
    private String snackName;
    private int quantity;
    private double unitPrice;
    private double lineTotal;

    public OrderLineItem() {
    }

    public OrderLineItem(String snackId, String snackName, int quantity, double unitPrice) {
        this.snackId = snackId;
        this.snackName = snackName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.lineTotal = unitPrice * quantity;
    }

    public String getSnackId() {
        return snackId;
    }

    public void setSnackId(String snackId) {
        this.snackId = snackId;
    }

    public String getSnackName() {
        return snackName;
    }

    public void setSnackName(String snackName) {
        this.snackName = snackName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public double getLineTotal() {
        return lineTotal;
    }

    public void setLineTotal(double lineTotal) {
        this.lineTotal = lineTotal;
    }
}
