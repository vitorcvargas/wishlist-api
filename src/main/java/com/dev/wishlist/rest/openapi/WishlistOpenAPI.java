package com.dev.wishlist.rest.openapi;

import com.dev.wishlist.exceptions.ExceptionResponse;
import com.dev.wishlist.models.Product;
import com.dev.wishlist.rest.dtos.WishlistResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;

public interface WishlistOpenAPI {

    @Operation(summary = "Adds a product to a wishlist")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201"),
            @ApiResponse(responseCode = "400", description = "It was not possible to add the selected product to the wishlist as it is already full.", content = {
                    @Content(schema = @Schema(implementation = ExceptionResponse.class))
            })
    })
    ResponseEntity<String> addToWishlist(final String requestTraceId, final Long userId, final String wishlistId, final Product product);

    @Operation(summary = "Lists a sequence of filtered detailed products in a user's wishlist")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "Product not found with search input: {searchInput}; Product not found with id: {productId}; Wishlist not found for user with id: {userId}", content = {
                    @Content(schema = @Schema(implementation = ExceptionResponse.class))
            })
    })
    ResponseEntity<WishlistResponse> filterProducts(final String requestTraceId, final Long userId, final String wishlistId, final String searchInput);

    @Operation(summary = "Lists a sequence of detailed products in a user's wishlist")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema(example = "Wishlist not found for user with id: {userId}"))})
    })
    ResponseEntity<WishlistResponse> findAllProducts(final String requestTraceId, final Long userId, final String wishlistId);

    @Operation(summary = "Deletes a product from the user's wishlist")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "404", description = "Product not found with id: {productId}; Wishlist not found for user with id: {userId}", content = {
                    @Content(schema = @Schema(implementation = ExceptionResponse.class))
            })
    })
    ResponseEntity<String> deleteProduct(final String requestTraceId, final Long userId, final String wishlistId, final Long productId);
}
