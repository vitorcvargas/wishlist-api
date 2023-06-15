package com.dev.wishlist.testutils;

import com.dev.wishlist.models.Product;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.dev.wishlist.utils.APIConstants.MAX_WISHLIST_SIZE;

public class ProductCreator {

    public static Product createSingleProduct() {
        return new Product(1L, "Nike revolution");
    }

    public static Set<Product> createProductListWithMaxCapacity() {
        Set<Product> products = new HashSet<>();

        products.add(new Product(1L, "Nike air"));
        products.add(new Product(2L, "Nike Max plus"));
        products.add(new Product(3L, "Adidas Grand Court"));
        products.add(new Product(4L, "Adidas Forum"));
        products.add(new Product(15L, "Gabinete de cozinha"));
        products.add(new Product(6L, "gabinete de banheiro"));
        products.add(new Product(7L, "Sofa"));
        products.add(new Product(8L, "Xiaomi Pocophone"));
        products.add(new Product(9L, "Xiaomi Redmi"));
        products.add(new Product(10L, "JBL Ear bug"));
        products.add(new Product(11L, "Fortrek Headset"));
        products.add(new Product(12L, "Conjunto de panelas"));
        products.add(new Product(13L, "Batedeira Masterchef"));
        products.add(new Product(14L, "Balança"));
        products.add(new Product(15L, "Bola de basket"));
        products.add(new Product(16L, "Maquina de espresso Nescafe"));
        products.add(new Product(17L, "Impressora"));
        products.add(new Product(18L, "Smart TV LG"));
        products.add(new Product(19L, "Cadeira gamer"));
        products.add(new Product(20L, "Tapete"));

        return products;
    }

    private ProductCreator() {
    }
}
