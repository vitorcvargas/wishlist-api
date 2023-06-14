package com.dev.wishlist.controllers;

import com.dev.wishlist.integration.BaseIT;
import com.dev.wishlist.models.Product;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.dev.wishlist.testutils.ProductCreator.createSingleProduct;
import static io.restassured.RestAssured.given;
import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;

public class WishlistControllerIT extends BaseIT {

    @Test
    @DisplayName("Should add product to wishlist")
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
}
