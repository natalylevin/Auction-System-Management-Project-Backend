package com.dev.objects;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "bid")
public class Bid {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "placement_date")
    private Date date;

    @Column(name = "new_bid")
    private Double newBid;

    public Bid() {

    }

    public Bid(User user, Product product, Date date, Double newBid) {
        this.user = user;
        this.product = product;
        this.date = date;
        this.newBid = newBid;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Double getNewBid() {
        return newBid;
    }

    public void setNewBid(Double newBid) {
        this.newBid = newBid;
    }
}
