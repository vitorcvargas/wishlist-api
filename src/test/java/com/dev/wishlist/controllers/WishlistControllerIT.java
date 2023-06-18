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
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import static com.dev.wishlist.testutils.creators.ProductCatalogCreator.createProductCatalogSet;
import static com.dev.wishlist.testutils.creators.ProductCreator.createProductSetWithMaxCapacity;
import static com.dev.wishlist.testutils.creators.ProductCreator.createSingleProduct;
import static io.restassured.RestAssured.given;
import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;

public class WishlistControllerIT extends BaseIT {

    @Autowired
    ProductCatalogRepository catalogRepository;

    @Autowired
    WishlistRepository wishlistRepository;

    static KafkaTestConsumer consumer;

    @BeforeAll
    static void subscribeToTopics() {
        consumer = new KafkaTestConsumer(kafkaContainer.getBootstrapServers(), "test_group");
        consumer.subscribe(List.of("wishlist.product.added", "wishlist.product.deleted"));
    }

    @BeforeEach
    void setUp() {
        catalogRepository.saveAll(createProductCatalogSet());
    }

    @AfterEach
    void cleanUp() {
        catalogRepository.deleteAll();
        wishlistRepository.deleteAll();
    }

    @Test
    @DisplayName("Should add product to wishlist and return 201 status")
    void shouldAddProductToWishlist() {
        Product product = createSingleProduct();

        final String response = given()
                .contentType(ContentType.JSON).and().body(product)
                .and().header("x-request-trace-id", UUID.randomUUID())
                .when().post(format("/wishlist/%s", 1L))
                .then().assertThat()
                .statusCode(201)
                .extract().response().getBody().asString();

        ConsumerRecords<Long, Long> records = consumer.poll();
        assertThat(records.count()).isGreaterThanOrEqualTo(1);
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
        wishlistRepository.save(createWishlistWithFullSize(1L));

        final JsonPath response = given()
                .contentType(ContentType.JSON).and().param("searchInput", "nike")
                .and().header("x-request-trace-id", UUID.randomUUID())
                .when().get(format("/wishlist/filter/%s", 1L))
                .then().assertThat()
                .statusCode(200)
                .extract().response().jsonPath();

        final Object userId = response.get("userId");
        final List<ProductCatalog> products = response.get("products");

        assertThat(userId).isEqualTo(1);
        assertThat(products.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("Should not find products matching an search input and throw an exception")
    void shouldNotFindProductsMatchingSearchInput() {
        wishlistRepository.save(createWishlistWithFullSize(1L));

        final JsonPath response = given()
                .contentType(ContentType.JSON).and().param("searchInput", "mock")
                .and().header("x-request-trace-id", UUID.randomUUID())
                .when().get(format("/wishlist/filter/%s", 1L))
                .then().assertThat()
                .statusCode(404)
                .extract().response().jsonPath();

        final Object code = response.get("code");
        final Object message = response.get("message");

        assertThat(code).isEqualTo(3);
        assertThat(message).isEqualTo("Product not found with search input: mock");
    }

    @Test
    @DisplayName("Should not find wishlist when userId is invalid and throw an exception")
    void shouldNotFindWishlistWhenUserIdIsInvalid() {

        final JsonPath response = given()
                .contentType(ContentType.JSON).and().param("searchInput", "mock")
                .and().header("x-request-trace-id", UUID.randomUUID())
                .when().get(format("/wishlist/filter/%s", 1L))
                .then().assertThat()
                .statusCode(404)
                .extract().response().jsonPath();

        final Object code = response.get("code");
        final Object message = response.get("message");

        assertThat(code).isEqualTo(4);
        assertThat(message).isEqualTo("Wishlist not found for user with id: 1");
    }

    @Test
    @DisplayName("Should find all products")
    void shouldFindAllProducts() {
        wishlistRepository.save(createWishlistWithFullSize(1L));

        final JsonPath response = given()
                .contentType(ContentType.JSON)
                .and().header("x-request-trace-id", UUID.randomUUID())
                .when().get(format("/wishlist/%s", 1L))
                .then().assertThat()
                .statusCode(200)
                .extract().response().jsonPath();

        final Object userId = response.get("userId");
        final List<ProductCatalog> products = response.get("products");

        assertThat(userId).isEqualTo(1);
        assertThat(products.size()).isEqualTo(20);
    }

    @Test
    @DisplayName("Should delete a single product")
    void shouldDeleteASingleProduct() {
        wishlistRepository.save(createWishlistWithFullSize(1L));

        final String response = given()
                .contentType(ContentType.JSON)
                .and().header("x-request-trace-id", UUID.randomUUID())
                .when().delete(format("/wishlist/%s/%s", 1L, 1L))
                .then().assertThat()
                .statusCode(200)
                .extract().response().getBody().asString();

        Wishlist wishlist = wishlistRepository.findByUserId(1L).orElse(new Wishlist());

        assertThat(response).isEqualTo("Product deleted.");
        assertThat(wishlist.getProducts().size()).isEqualTo(19);

        ConsumerRecords<Long, Long> records = consumer.poll();
        assertThat(records.count()).isGreaterThanOrEqualTo(1);
    }

    private Wishlist createWishlistWithFullSize(long userId) {
        return new Wishlist(userId, createProductSetWithMaxCapacity());
    }

    private void addProductsToWishlistUpToItsLimit() {
        Set<Product> productListWithMaxCapacity = createProductSetWithMaxCapacity();

        for (Product product : productListWithMaxCapacity) {
            given()
                    .contentType(ContentType.JSON).and().body(product)
                    .and().header("x-request-trace-id", UUID.randomUUID())
                    .when().post(format("/wishlist/%s", 1L))
                    .then().assertThat()
                    .statusCode(201);
        }
    }
}
