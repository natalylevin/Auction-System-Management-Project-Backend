package com.dev.models;

public class NotificationModel {

    private int eventCode;
    private String productName;
    private Double newPrice;

    private int userId;



    public NotificationModel(int eventCode) {
        this.eventCode = eventCode;
    }


    public NotificationModel(int eventCode, Double newPrice, int userId) {
        this.eventCode = eventCode;
        this.newPrice = newPrice;
        this.userId = userId;
    }

    public NotificationModel(String productName, Double newPrice, int eventCode, int sellerId) {
        this.productName = productName;
        this.newPrice = newPrice;
        this.eventCode = eventCode;
        this.userId = sellerId;

    }



    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getEventCode() {
        return eventCode;
    }

    public void setEventCode(int eventCode) {
        this.eventCode = eventCode;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Double getNewPrice() {
        return newPrice;
    }

    public void setNewPrice(Double newPrice) {
        this.newPrice = newPrice;
    }
}
