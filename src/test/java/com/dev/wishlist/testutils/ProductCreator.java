package com.dev.wishlist.testutils;

import com.dev.wishlist.models.Product;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.dev.wishlist.utils.APIConstants.MAX_WISHLIST_SIZE;

public class ProductCreator {

    public static Product createSingleProduct() {
        return new Product(1L, "Nike revolution");
    }

    public static List<Product> createProductListWithMaxCapacity() {
        List<Product> products = new ArrayList<>();

        for (int i = 0; i < MAX_WISHLIST_SIZE; i++) {
            products.add(createSingleProduct());
        }

        return products;
    }

    private ProductCreator() {
    }
}
