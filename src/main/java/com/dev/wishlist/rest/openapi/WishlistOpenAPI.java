package com.dev.wishlist.rest.openapi;

import com.dev.wishlist.exceptions.ExceptionResponse;
import com.dev.wishlist.models.Product;
import com.dev.wishlist.rest.dtos.WishlistDTO;
import com.dev.wishlist.rest.dtos.WishlistResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface WishlistOpenAPI {

    @Operation(summary = "Create wishlist")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201"),
            @ApiResponse(responseCode = "400", description = "Wishlist already created with name={name}, userId={userId}", content = {
                    @Content(schema = @Schema(implementation = ExceptionResponse.class))
            })
    })
    ResponseEntity<WishlistResponse> createWishlist(final String requestTraceId, final Long userId, final WishlistDTO wishlistDTO);

    @Operation(summary = "Get all wishlists")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "404", description = "Wishlist not found with userId={userId}, wishlistId={wishlistId}", content = {
                    @Content(schema = @Schema(implementation = ExceptionResponse.class))
            })
    })
    ResponseEntity<List<WishlistResponse>> getAllWishlists(final String requestTraceId, final Long userId);

    @Operation(summary = "Delete wishlist")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "404", description = "Wishlist not found with userId={userId}, wishlistId={wishlistId}", content = {
                    @Content(schema = @Schema(implementation = ExceptionResponse.class))
            })
    })
    ResponseEntity<String> deleteWishlist(final String requestTraceId, final Long userId, final String wishlistId);

    @Operation(summary = "Delete wishlist")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "404", description = "Wishlist not found with userId={userId}, wishlistId={wishlistId}", content = {
                    @Content(schema = @Schema(implementation = ExceptionResponse.class))
            })
    })

    ResponseEntity<WishlistResponse> updateWishlist(final String requestTraceId, final Long userId, final String wishlistId, final WishlistDTO wishlistDTO);

    @Operation(summary = "Adds a product to a wishlist")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201"),
            @ApiResponse(responseCode = "400", description = "It was not possible to add the selected product to the wishlist as it is already full.", content = {
                    @Content(schema = @Schema(implementation = ExceptionResponse.class))
            }),
            @ApiResponse(responseCode = "404", description = "Wishlist not found with userId={userId}, wishlistId={wishlistId}", content = {
                    @Content(schema = @Schema(implementation = ExceptionResponse.class))
            })
    })
    ResponseEntity<String> addToWishlist(final String requestTraceId, final Long userId, final String wishlistId, final Product product);

    @Operation(summary = "Lists a sequence of filtered detailed products in a user's wishlist")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "Wishlist not found with userId={userId}, wishlistId={wishlistId}; Product not found with searchInput=%s, userId={userId}, wishlistId={wishlistId}", content = {
                    @Content(schema = @Schema(implementation = ExceptionResponse.class))
            })
    })
    ResponseEntity<WishlistResponse> filterProducts(final String requestTraceId, final Long userId, final String wishlistId, final String searchInput);

    @Operation(summary = "Lists a sequence of detailed products in a user's wishlist")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema(example = "Wishlist not found with userId={userId}, wishlistId={wishlistId}"))})
    })
    ResponseEntity<WishlistResponse> findAllProducts(final String requestTraceId, final Long userId, final String wishlistId);

    @Operation(summary = "Deletes a product from the user's wishlist")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "404", description = "PWishlist not found with userId={userId}, wishlistId={wishlistId}; Product not found with productId={productId}, userId={userId}, wishlistId={wishlistId}", content = {
                    @Content(schema = @Schema(implementation = ExceptionResponse.class))
            })
    })
    ResponseEntity<String> deleteProduct(final String requestTraceId, final Long userId, final String wishlistId, final Long productId);
}
