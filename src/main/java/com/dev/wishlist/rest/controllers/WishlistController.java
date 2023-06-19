package com.dev.wishlist.rest.controllers;

import com.dev.wishlist.models.Product;
import com.dev.wishlist.rest.dtos.WishlistDTO;
import com.dev.wishlist.rest.dtos.WishlistResponse;
import com.dev.wishlist.rest.openapi.WishlistOpenAPI;
import com.dev.wishlist.services.WishlistService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.dev.wishlist.utils.APIConstants.*;
import static java.lang.String.format;
import static org.springframework.http.HttpMethod.*;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/wishlists")
public class WishlistController implements WishlistOpenAPI {

    private final Logger logger = LoggerFactory.getLogger(WishlistController.class);
    private final WishlistService wishlistService;

    public WishlistController(WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }

    @PostMapping("/{userId}")
    public ResponseEntity<WishlistResponse> createWishlist(@RequestHeader("x-request-trace-id") final String requestTraceId, @PathVariable final Long userId, @RequestBody final WishlistDTO wishlistDTO) {
        final var params = format("userId=%s, wishlistDTO=%s", userId, wishlistDTO);
        logger.info(REQUEST_RECEIVED, "createWishlist", POST, params);

        WishlistResponse response = wishlistService.createWishlist(userId, wishlistDTO);

        logger.info(REQUEST_RESPONSE_WITHOUT_BODY, "createWishlist", CREATED.value());

        return ResponseEntity.status(CREATED).body(response);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<WishlistResponse>> getAllWishlists(@RequestHeader("x-request-trace-id") final String requestTraceId, @PathVariable final Long userId) {
        final var params = format("userId=%s", userId);
        logger.info(REQUEST_RECEIVED, "getAllWishlists", GET, params);

        List<WishlistResponse> wishlists = wishlistService.getAllWishlists(userId);

        logger.info(REQUEST_RESPONSE_WITHOUT_BODY, "getAllWishlists", OK.value());

        return ResponseEntity.status(OK).body(wishlists);
    }

    @DeleteMapping("/{userId}/{wishlistId}")
    public ResponseEntity<String> deleteWishlist(@RequestHeader("x-request-trace-id") final String requestTraceId, @PathVariable final Long userId, @PathVariable final String wishlistId) {
        final var params = format("userId=%s, wishlistId=%s", userId, wishlistId);
        logger.info(REQUEST_RECEIVED, "deleteWishlist", DELETE, params);

        wishlistService.deleteWishlist(userId, wishlistId);

        logger.info(REQUEST_RESPONSE_WITHOUT_BODY, "deleteWishlist", OK.value());

        return ResponseEntity.status(OK).body("Wishlist deleted.");
    }

    @PutMapping("/{userId}/{wishlistId}")
    public ResponseEntity<WishlistResponse> updateWishlist(@RequestHeader("x-request-trace-id") final String requestTraceId, @PathVariable final Long userId, @PathVariable final String wishlistId, @RequestBody final WishlistDTO wishlistDTO) {
        final var params = format("userId=%s, wishlistId=%s, body=%s", userId, wishlistId, wishlistDTO);
        logger.info(REQUEST_RECEIVED, "updateWishlist", PUT, params);

        WishlistResponse wishlist = wishlistService.updateWishlist(userId, wishlistId, wishlistDTO);

        logger.info(REQUEST_RESPONSE_WITHOUT_BODY, "updateWishlist", OK.value());

        return ResponseEntity.status(OK).body(wishlist);
    }

    @PostMapping("/products/{userId}/{wishlistId}")
    public ResponseEntity<String> addToWishlist(@RequestHeader("x-request-trace-id") final String requestTraceId, @PathVariable final Long userId, @PathVariable final String wishlistId, @RequestBody final Product product) {
        final var params = format("userId=%s, wishlistId=%s, product=%s", userId, wishlistId, product);
        logger.info(REQUEST_RECEIVED, "addToWishlist", POST, params);

        wishlistService.addToWishlist(product, userId, wishlistId);

        logger.info(REQUEST_RESPONSE_WITHOUT_BODY, "addToWishlist", CREATED.value());

        return ResponseEntity.status(CREATED).body("Product added to wishlist");
    }

    @GetMapping("/products/filter/{userId}/{wishlistId}")
    public ResponseEntity<WishlistResponse> filterProducts(@RequestHeader("x-request-trace-id") final String requestTraceId, @PathVariable final Long userId, @PathVariable final String wishlistId, @RequestParam final String searchInput) {
        final var params = format("userId=%s, wishlistId=%s, searchInput=%s", userId, wishlistId, searchInput);
        logger.info(REQUEST_RECEIVED, "findProducts", GET, params);

        final var products = wishlistService.filterProducts(userId, wishlistId, searchInput);

        logger.info(REQUEST_RESPONSE_WITH_BODY, "findProducts", OK.value(), products);

        return ResponseEntity.status(OK).body(products);
    }

    @GetMapping("/products/{userId}/{wishlistId}")
    public ResponseEntity<WishlistResponse> findAllProducts(@RequestHeader("x-request-trace-id") final String requestTraceId, @PathVariable final Long userId, @PathVariable final String wishlistId) {
        final var params = format("userId=%s, wishlistId=%s", userId, wishlistId);
        logger.info(REQUEST_RECEIVED, "findAllProducts", GET, params);

        final var products = wishlistService.findAllProducts(userId, wishlistId);

        logger.info(REQUEST_RESPONSE_WITH_BODY, "findAllProducts", OK.value(), products);

        return ResponseEntity.status(OK).body(products);
    }

    @DeleteMapping("/products/{userId}/{wishlistId}")
    public ResponseEntity<String> deleteProduct(@RequestHeader("x-request-trace-id") final String requestTraceId, @PathVariable final Long userId, @PathVariable final String wishlistId, @RequestParam final Long productId) {
        final var params = format("userId=%s, wishlistId=%s, productId=%s", userId, wishlistId, productId);
        logger.info(REQUEST_RECEIVED, "deleteProduct", DELETE, params);

        wishlistService.deleteProduct(userId, wishlistId, productId);

        logger.info(REQUEST_RESPONSE_WITHOUT_BODY, "deleteProduct", OK.value());

        return ResponseEntity.status(OK).body("Product deleted.");
    }
}
