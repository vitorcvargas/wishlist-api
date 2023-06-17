package com.dev.wishlist.controllers;

import com.dev.wishlist.models.Product;
import com.dev.wishlist.models.ProductCatalog;
import com.dev.wishlist.models.Wishlist;
import com.dev.wishlist.repositories.ProductCatalogRepository;
import com.dev.wishlist.repositories.WishlistRepository;
import com.dev.wishlist.testutils.integration.BaseIT;
import com.dev.wishlist.testutils.integration.consumers.KafkaTestConsumer;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.junit.Before;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static com.dev.wishlist.testutils.creators.ProductCatalogCreator.createProductCatalogSet;
import static com.dev.wishlist.testutils.creators.ProductCreator.createProductSetWithMaxCapacity;
import static com.dev.wishlist.testutils.creators.ProductCreator.createSingleProduct;
import static io.restassured.RestAssured.given;
import static java.lang.String.format;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;

public class WishlistControllerIT extends BaseIT {

    List<ConsumerRecord<Long, Long>> consumerRecords = new ArrayList<>();

    @Autowired
    ProductCatalogRepository catalogRepository;

    @Autowired
    WishlistRepository wishlistRepository;

    @Before
    void setUp() {
        catalogRepository.saveAll(createProductCatalogSet());
    }

    @Test
    @DisplayName("Should add product to wishlist and return 201 status")
    void shouldAddProductToWishlist() {

        KafkaTestConsumer kafkaTestConsumer = new KafkaTestConsumer(kafkaContainer.getBootstrapServers(), "test_group");

        kafkaTestConsumer.subscribe(singletonList("wishlist.product.added"));

        Product product = createSingleProduct();

        final String response = given()
                .contentType(ContentType.JSON).and().body(product)
                .and().header("x-request-trace-id", UUID.randomUUID())
                .when().post(format("/wishlist/%s", 1L))
                .then().assertThat()
                .statusCode(201)
                .extract().response().getBody().asString();

        ConsumerRecords<Long, Long> records = kafkaTestConsumer.poll();
        assertThat(records.count()).isEqualTo(1);
        assertThat(response).isEqualTo("Product added to wishlist");
    }

    @Test
    @DisplayName("Should not add product to wishlist and return 400 status when wishlist is full")
    void shouldANotddProductToWishlistWhenItIsFull() {
        Product product = createSingleProduct();

        addProductsToWishlistUpToItsLimit();

        final JsonPath response = given()
                .contentType(ContentType.JSON).and().body(product)
                .and().header("x-request-trace-id", UUID.randomUUID())
                .when().post(format("/wishlist/%s", 1L))
                .then().assertThat()
                .statusCode(400)
                .extract().response().jsonPath();

        final Object code = response.get("code");
        final Object message = response.get("message");

        assertThat(code).isEqualTo(1);
        assertThat(message).isEqualTo("It was not possible to add the selected product to the wishlist as it is already full.");
    }

    @Test
    @DisplayName("Should not add product to wishlist and return 400 status when product was already added")
    void shouldANotddProductToWishlistWhenProductAlreadyAdded() {
        Product product = createSingleProduct();

        given()
                .contentType(ContentType.JSON).and().body(product)
                .and().header("x-request-trace-id", UUID.randomUUID())
                .when().post(format("/wishlist/%s", 1L))
                .then().assertThat()
                .statusCode(201);

        final JsonPath response = given()
                .contentType(ContentType.JSON).and().body(product)
                .and().header("x-request-trace-id", UUID.randomUUID())
                .when().post(format("/wishlist/%s", 1L))
                .then().assertThat()
                .statusCode(400)
                .extract().response().jsonPath();

        final Object code = response.get("code");
        final Object message = response.get("message");

        assertThat(code).isEqualTo(2);
        assertThat(message).isEqualTo("Product already added to wishlist.");
    }

    @Test
    @DisplayName("Should find products matching an search input, case insensitive")
    void shouldFindProductsMatchingSearchInput() {
        populateProductCatalogCache();
        wishlistRepository.save(createWishlistWithFullSize(1L));


        final JsonPath response = given()
                .contentType(ContentType.JSON).and().param("searchInput", "ni")
                .and().header("x-request-trace-id", UUID.randomUUID())
                .when().get(format("/wishlist/%s", 1L))
                .then().assertThat()
                .statusCode(200)
                .extract().response().jsonPath();

        final Object userId = response.get("userId");
        final List<ProductCatalog> products = response.get("products");

        assertThat(userId).isEqualTo(1);
        assertThat(products.size()).isEqualTo(2);
    }

    private Wishlist createWishlistWithFullSize(long userId) {
        return new Wishlist(userId, createProductSetWithMaxCapacity());
    }

    private void populateProductCatalogCache() {
        catalogRepository.saveAll(createProductCatalogSet());
    }

    private void addProductsToWishlistUpToItsLimit() {
        Set<Product> productListWithMaxCapacity = createProductSetWithMaxCapacity();

        for (Product product : productListWithMaxCapacity) {
            String string = given()
                    .contentType(ContentType.JSON).and().body(product)
                    .and().header("x-request-trace-id", UUID.randomUUID())
                    .when().post(format("/wishlist/%s", 1L))
                    .then().assertThat()
                    .statusCode(201).extract().response().toString();

            System.out.println(string);
        }
    }
}
