package com.dev.models;

import com.dev.objects.Bid;

import java.util.Date;

public class BidModel {
    private Long bidId;

    private int userId;

    private int productId;

    private Double bidValue;

    private Date placementDate;

    public BidModel () {

    }


    public BidModel(Bid bid) {
        this.bidId = bid.getId();
        this.userId = bid.getUser().getId();
        this.productId = bid.getProduct().getId();
        this.bidValue = bid.getNewBid();
        this.placementDate = bid.getDate();
    }

    public Date getPlacementDate() {
        return placementDate;
    }

    public void setPlacementDate(Date placementDate) {
        this.placementDate = placementDate;
    }

    public Long getBidId() {
        return bidId;
    }

    public void setBidId(Long bidId) {
        this.bidId = bidId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public Double getBidValue() {
        return bidValue;
    }

    public void setBidValue(Double bidValue) {
        this.bidValue = bidValue;
    }
}
