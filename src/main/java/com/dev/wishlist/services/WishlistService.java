package com.dev.wishlist.services;

import com.dev.wishlist.rest.dtos.WishlistResponse;
import com.dev.wishlist.exceptions.BadRequestException;
import com.dev.wishlist.exceptions.NotFoundException;
import com.dev.wishlist.mappers.WishlistMapper;
import com.dev.wishlist.models.Product;
import com.dev.wishlist.models.Wishlist;
import com.dev.wishlist.repositories.ProductCatalogRepository;
import com.dev.wishlist.repositories.WishlistRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.stream.StreamSupport;

import static com.dev.wishlist.utils.APIConstants.*;
import static java.lang.String.format;

@Service
public class WishlistService {

    private final Logger logger = LoggerFactory.getLogger(WishlistService.class);
    private final WishlistRepository wishlistRepository;
    private final ProductCatalogRepository productCatalogRepository;
    private final WishlistNotifierService notifier;

    public WishlistService(final WishlistRepository wishlistRepository, final ProductCatalogRepository productCatalogRepository, final WishlistNotifierService notifier) {
        this.wishlistRepository = wishlistRepository;
        this.productCatalogRepository = productCatalogRepository;
        this.notifier = notifier;
    }

    public void addToWishlist(final Product product, final Long userId) {
        logger.info("action=started_adding_product_to_wishlist product={}", product);

        final var wishlist = wishlistRepository.findByUserId(userId).orElse(new Wishlist());
        final var wishlistSize = wishlist.getProducts().size();

        if (wishlistSize == MAX_WISHLIST_SIZE)
            throw BadRequestException.wishlistLimitReached();

        if (wishlist.getProducts().contains(product))
            throw BadRequestException.productAlreadyAddedToWishlist();

        wishlist.setUserId(userId);
        wishlist.addProduct(product);

        wishlistRepository.save(wishlist);
        notifier.notify(PRODUCT_ADDED_TOPIC, userId, product.getProductId());

        logger.info("action=finished_adding_product_to_wishlist product={}", product);
    }

    public WishlistResponse findProducts(final Long userId, final String searchInput) {
        logger.info("action=started_finding_products, userId={}, searchInput={}", userId, searchInput);

        final var productIds = getWishlistOrElseThrow(userId)
                .getProducts()
                .stream()
                .filter(product -> product.getName().toLowerCase().matches(format(".*%s.*", searchInput.toLowerCase())))
                .map(Product::getProductId)
                .toList();

        if (productIds.isEmpty())
            throw NotFoundException.productNotFoundWithSearchInput(searchInput);

       final var products =
                StreamSupport.stream(productCatalogRepository.findAllById(productIds).spliterator(), false)
                        .toList();

        logger.info("action=finished_finding_products, userId={}, searchInput={}", userId, searchInput);

        return WishlistMapper.INSTANCE.wishlistGetRequestToWishlistResponse(userId, products);
    }

    public WishlistResponse findAllProducts(final Long userId) {
        logger.info("action=started_finding_all_products, userId={}", userId);

        final var productIds = getWishlistOrElseThrow(userId)
                .getProducts()
                .stream()
                .map(Product::getProductId)
                .toList();

        final var products =
                StreamSupport.stream(productCatalogRepository.findAllById(productIds).spliterator(), false)
                        .toList();

        logger.info("action=finished_finding_all_products, userId={}", userId);

        return WishlistMapper.INSTANCE.wishlistGetRequestToWishlistResponse(userId, products);
    }

    public void deleteProduct(final Long userId, final Long productId) {
        logger.info("action=started_deleting_product, userId={}, productId={}", userId, productId);

        final var wishlist = getWishlistOrElseThrow(userId);

        final var productOptional = wishlist.getProducts()
                .stream()
                .filter(product -> Objects.equals(product.getProductId(), productId))
                .findFirst();

        if (productOptional.isEmpty())
            throw NotFoundException.productNotFoundWithId(productId);

        wishlist.getProducts().remove(productOptional.get());

        wishlistRepository.save(wishlist);
        notifier.notify(PRODUCT_DELETED_TOPIC, userId, productId);

        logger.info("action=finished_deleting_product, userId={}, productId={}", userId, productId);
    }

    private Wishlist getWishlistOrElseThrow(final Long userId) {
        return wishlistRepository.findByUserId(userId)
                .orElseThrow(() -> NotFoundException.wishlistNotFoundForUserId(userId));
    }
}
