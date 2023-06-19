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

    private String name;

    private Long userId;

    private Set<Product> products = new HashSet<>();
    private boolean isPublic = false;

    public Wishlist(String id, String name, Long userId, Set<Product> products) {
        this.id = id;
        this.name = name;
        this.userId = userId;
        this.products = products;
    }

    public Wishlist(String name, Long userId, Set<Product> products, boolean isPublic) {
        this.name = name;
        this.userId = userId;
        this.products = products;
        this.isPublic = isPublic;
    }

    public Wishlist(String id, String name, Long userId, Set<Product> products, boolean isPublic) {
        this.id = id;
        this.name = name;
        this.userId = userId;
        this.products = products;
        this.isPublic = isPublic;
    }

    public void addProduct(final Product product) {
        products.add(product);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    @Override
    public String toString() {
        return "Wishlist{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", userId=" + userId +
                ", products=" + products +
                ", isPublic=" + isPublic +
                '}';
    }
}
