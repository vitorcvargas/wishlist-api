package com.dev.wishlist.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Document
public class Wishlist {

    @Id
    private String id;

    private Long userId;

    private Set<Product> products = new HashSet<>();

    public void addProduct(final Product product) {
        products.add(product);
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

    public Set<Product> getProducts() {
        return products;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }

    public Wishlist() {
    }

    public Wishlist(Long userId, Set<Product> products) {
        this.userId = userId;
        this.products = products;
    }

    @Override
    public String toString() {
        return "Wishlist{" +
                "id='" + id + '\'' +
                ", userId=" + userId +
                ", products=" + products +
                '}';
    }
}
