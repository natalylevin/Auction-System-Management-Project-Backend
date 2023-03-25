package com.dev.objects;


import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int id;

    @Column
    private String productName;
    @Column
    private String productDescription;

    @Column
    private String imageLink;

    @Column
    private Double minPrice;
    @Column
    private Boolean sold;



    @Column
    private Date placementDate;

    @ManyToOne
    @JoinColumn(name = "seller_id")
    private User seller;



    public Product() {
    }

    public Product(String productName, String productDescription,
                   String imageLink, Double minPrice, User seller) {
        this.productName = productName;
        this.productDescription = productDescription;
        this.imageLink = imageLink;
        this.minPrice = minPrice;
        this.sold = false;

        this.seller = seller;
        this.placementDate = new Date();
    }



    public Date getPlacementDate() {
        return placementDate;
    }

    public void setPlacementDate(Date placementDate) {
        this.placementDate = placementDate;
    }

    public User getSeller() {
        return seller;
    }

    public void setSeller(User seller) {
        this.seller = seller;
    }

    public Boolean getSold() {
        return sold;
    }

    public void setSold(Boolean sold) {
        this.sold = sold;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
}
