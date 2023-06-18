package com.dev.wishlist.rest.dtos;

import com.dev.wishlist.models.ProductCatalog;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public class WishlistResponse {

    @Schema(
            description = "User id",
            example = "1"
    )
    private Long userId;

    @Schema(
            description = "Detailed Products"
    )
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
