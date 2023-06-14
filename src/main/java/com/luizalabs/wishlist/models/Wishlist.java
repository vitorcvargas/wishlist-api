package com.luizalabs.wishlist.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

import static com.luizalabs.wishlist.utils.APIConstants.MAX_WISHLIST_SIZE;

@Document
public class Wishlist {

    @Id
    private String id;

    private Long userId;

    private List<Product> products = new ArrayList<>();

    public boolean addProduct(final Product product) {
        if (this.products.size() == MAX_WISHLIST_SIZE)
            return false;

        return products.add(product);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public Wishlist() {
    }

    public Wishlist(Long userId, List<Product> products) {
        this.userId = userId;
        this.products = products;
    }
}
