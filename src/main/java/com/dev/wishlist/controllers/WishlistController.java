package com.dev.wishlist.controllers;

import com.dev.wishlist.dtos.WishlistResponse;
import com.dev.wishlist.models.Product;
import com.dev.wishlist.services.WishlistService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.dev.wishlist.utils.APIConstants.*;
import static java.lang.String.format;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/wishlist")
public class WishlistController {

    private final Logger logger = LoggerFactory.getLogger(WishlistController.class);
    private final WishlistService wishlistService;

    public WishlistController(WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }

    @PostMapping("/{userId}")
    public ResponseEntity<String> addToWishlist(@RequestHeader("x-request-trace-id") String requestTraceId, @PathVariable final Long userId, @RequestBody final Product product) {
        final String params = format("userId: %s, product: %s", userId, product);
        logger.info(REQUEST_RECEIVED, "addToWishlist", POST, params);

        wishlistService.addToWishlist(product, userId);

        logger.info(REQUEST_RESPONSE_WITHOUT_BODY, "addToWishlist", CREATED.value());

        return ResponseEntity.status(CREATED).body("Product added to wishlist");
    }

    @GetMapping("/{userId}")
    public ResponseEntity<WishlistResponse> findProducts(@RequestHeader("x-request-trace-id") String requestTraceId, @PathVariable final Long userId, @RequestParam final String searchInput) {
        final String params = format("userId: %s, searchInput: %s", userId, searchInput);
        logger.info(REQUEST_RECEIVED, "findProducts", GET, params);

        WishlistResponse products = wishlistService.findProducts(userId, searchInput);

        logger.info(REQUEST_RESPONSE_WITH_BODY, "findProducts", OK.value(), products);

        return ResponseEntity.status(OK).body(products);
    }
}
