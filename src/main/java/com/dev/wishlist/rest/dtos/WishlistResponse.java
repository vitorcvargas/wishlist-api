package com.dev.wishlist.rest.dtos;

import com.dev.wishlist.models.ProductCatalog;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public class WishlistResponse {

    @Schema(
            description = "Wishlist id",
            example = "djshd72whs3123sdd"
    )
    private String id;

    @Schema(
            description = "Wishlist's name",
            example = "Birthday wishlist"
    )
    private String name;

    @Schema(
            description = "User id",
            example = "1"
    )
    private Long userId;

    @Schema(
            description = "Detailed Products"
    )
    private List<ProductCatalog> products;

    @Schema(
            description = "Indicates whether the wishlist is visible publicly"
    )
    private boolean isPublic;

    public WishlistResponse() {
    }

    public WishlistResponse(String id, String name, Long userId, List<ProductCatalog> products, boolean isPublic) {
        this.id = id;
        this.name = name;
        this.userId = userId;
        this.products = products;
        this.isPublic = isPublic;
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

    public List<ProductCatalog> getProducts() {
        return products;
    }

    public void setProducts(List<ProductCatalog> products) {
        this.products = products;
    }

    @Override
    public String toString() {
        return "WishlistResponse{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", userId=" + userId +
                ", products=" + products +
                '}';
    }
}
