package com.dev.models;

import com.dev.objects.Product;

import java.text.SimpleDateFormat;


public class ProductModel {
    private int productId;
    private String productName;
    private String productDescription;

    private String imageLink;

    private Double minPrice;

    private Boolean sold;

    private String placementDate;

    private int sellerId;

    public ProductModel(Product product) {
        this.productId = product.getId();
        this.productDescription = product.getProductDescription();
        this.productName = product.getProductName();
        this.minPrice = product.getMinPrice();
        this.imageLink = product.getImageLink();
        this.sold = product.getSold();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        this.placementDate = simpleDateFormat.format(product.getPlacementDate());
        this.sellerId = product.getSeller().getId();
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public Double getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(Double minPrice) {
        this.minPrice = minPrice;
    }

    public Boolean getSold() {
        return sold;
    }

    public void setSold(Boolean sold) {
        this.sold = sold;
    }



    public String getPlacementDate() {
        return placementDate;
    }

    public void setPlacementDate(String placementDate) {
        this.placementDate = placementDate;
    }

    public int getSellerId() {
        return sellerId;
    }

    public void setSellerId(int sellerId) {
        this.sellerId = sellerId;
    }
}
