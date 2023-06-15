package com.dev.wishlist.models;

import java.util.List;

public class ProductProjection {
    List<Product> products;

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    @Override
    public String toString() {
        return "ProductProjection{" +
                "products=" + products +
                '}';
    }
}
