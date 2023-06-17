package com.dev.wishlist.testutils.creators;

import com.dev.wishlist.models.ProductCatalog;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

public class ProductCatalogCreator {

    public static Set<ProductCatalog> createProductCatalogSet() {
        Set<ProductCatalog> products = new HashSet<>();

        products.add(new ProductCatalog(1L, "Nike air", "some description", new BigDecimal("300.00"), "https://somelink"));
        products.add(new ProductCatalog(2L, "Nike Max plus", "some description", new BigDecimal("300.00"), "https://somelink"));
        products.add(new ProductCatalog(3L, "Adidas Grand Court", "some description", new BigDecimal("300.00"), "https://somelink"));
        products.add(new ProductCatalog(4L, "Adidas Forum", "some description", new BigDecimal("300.00"), "https://somelink"));
        products.add(new ProductCatalog(5L, "Gabinete de cozinha", "some description", new BigDecimal("300.00"), "https://somelink"));
        products.add(new ProductCatalog(6L, "gabinete de banheiro", "some description", new BigDecimal("300.00"), "https://somelink"));
        products.add(new ProductCatalog(7L, "Sofa", "some description", new BigDecimal("300.00"), "https://somelink"));
        products.add(new ProductCatalog(8L, "Xiaomi Pocophone", "some description", new BigDecimal("300.00"), "https://somelink"));
        products.add(new ProductCatalog(9L, "Xiaomi Redmi", "some description", new BigDecimal("300.00"), "https://somelink"));
        products.add(new ProductCatalog(10L, "JBL Ear bug", "some description", new BigDecimal("300.00"), "https://somelink"));
        products.add(new ProductCatalog(11L, "Fortrek Headset", "some description", new BigDecimal("300.00"), "https://somelink"));
        products.add(new ProductCatalog(12L, "Conjunto de panelas", "some description", new BigDecimal("300.00"), "https://somelink"));
        products.add(new ProductCatalog(13L, "Batedeira Masterchef", "some description", new BigDecimal("300.00"), "https://somelink"));
        products.add(new ProductCatalog(14L, "Balança", "some description", new BigDecimal("300.00"), "https://somelink"));
        products.add(new ProductCatalog(15L, "Bola de basket", "some description", new BigDecimal("300.00"), "https://somelink"));
        products.add(new ProductCatalog(16L, "Maquina de espresso Nescafe", "some description", new BigDecimal("300.00"), "https://somelink"));
        products.add(new ProductCatalog(17L, "Impressora", "some description", new BigDecimal("300.00"), "https://somelink"));
        products.add(new ProductCatalog(18L, "Smart TV LG", "some description", new BigDecimal("300.00"), "https://somelink"));
        products.add(new ProductCatalog(19L, "Cadeira gamer", "some description", new BigDecimal("300.00"), "https://somelink"));
        products.add(new ProductCatalog(20L, "Tapete", "some description", new BigDecimal("300.00"), "https://somelink"));

        return products;
    }

    private ProductCatalogCreator() {
    }
}
