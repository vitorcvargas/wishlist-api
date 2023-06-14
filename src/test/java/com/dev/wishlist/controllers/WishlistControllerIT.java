package com.dev.wishlist.controllers;

import com.dev.wishlist.models.Product;
import com.dev.wishlist.testutils.integration.BaseIT;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.dev.wishlist.testutils.ProductCreator.createSingleProduct;
import static com.dev.wishlist.utils.APIConstants.MAX_WISHLIST_SIZE;
import static io.restassured.RestAssured.given;
import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;

public class WishlistControllerIT extends BaseIT {

    @Test
    @DisplayName("Should add product to wishlist and return 201 status")
    void shouldAddProductToWishlist() {
        Product product = createSingleProduct();

        final String response = given()
                .contentType(ContentType.JSON).and().body(product)
                .when().post(format("/wishlist/%s", 1L))
                .then().assertThat()
                .statusCode(201)
                .extract().response().getBody().asString();

        assertThat(response).isEqualTo("Product added to wishlist");
    }

    @Test
    @DisplayName("Should not add product to wishlist and return 400 status")
    void shouldANotddProductToWishlist() {
        Product product = createSingleProduct();

        addProductsToWishlistUpToItsLimit();

        final JsonPath response = given()
                .contentType(ContentType.JSON).and().body(product)
                .when().post(format("/wishlist/%s", 1L))
                .then().assertThat()
                .statusCode(400)
                .extract().response().jsonPath();

        final Object code = response.get("code");
        final Object message = response.get("message");

        assertThat(code).isEqualTo(1);
        assertThat(message).isEqualTo("It was not possible to add the selected product to the wishlist as it is already full.");
    }

    private void addProductsToWishlistUpToItsLimit() {
        Product product = createSingleProduct();

        for(int i = 0; i < MAX_WISHLIST_SIZE; i++) {
            given()
                    .contentType(ContentType.JSON).and().body(product)
                    .when().post(format("/wishlist/%s", 1L))
                    .then().assertThat()
                    .statusCode(201);
        }
    }
}
