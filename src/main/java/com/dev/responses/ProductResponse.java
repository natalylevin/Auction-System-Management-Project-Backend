package com.dev.responses;

import com.dev.models.ProductModel;
import com.dev.objects.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductResponse extends BasicResponse {
    List<ProductModel> products;

    public ProductResponse(List<Product> products) {
        this.products = new ArrayList<>();
        for (Product product : products) {
            this.products.add(new ProductModel(product));
        }
    }


    public List<ProductModel> getProducts() {
        return products;
    }

    public void setProducts(List<ProductModel> products) {
        this.products = products;
    }
}
