package com.dev.wishlist.dtos;

import com.dev.wishlist.models.ProductCatalog;

import java.util.List;

public class WishlistResponse {
    private Long userId;
    private List<ProductCatalog> products;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<ProductCatalog> getProducts() {
        return products;
    }

    public void setProducts(List<ProductCatalog> products) {
        this.products = products;
    }

    @Override
    public String toString() {
        return "WishlistResponse{" +
                "userId=" + userId +
                ", products=" + products +
                '}';
    }
}
