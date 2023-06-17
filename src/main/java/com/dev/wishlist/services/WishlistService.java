package com.dev.wishlist.services;

import com.dev.wishlist.dtos.WishlistResponse;
import com.dev.wishlist.exceptions.BadRequestException;
import com.dev.wishlist.exceptions.NotFoundException;
import com.dev.wishlist.mappers.WishlistMapper;
import com.dev.wishlist.models.Product;
import com.dev.wishlist.models.ProductCatalog;
import com.dev.wishlist.models.Wishlist;
import com.dev.wishlist.repositories.ProductCatalogRepository;
import com.dev.wishlist.repositories.WishlistRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.StreamSupport;

import static com.dev.wishlist.utils.APIConstants.MAX_WISHLIST_SIZE;
import static java.lang.String.format;

@Service
public class WishlistService {

    private final Logger logger = LoggerFactory.getLogger(WishlistService.class);
    private final WishlistRepository wishlistRepository;
    private final ProductCatalogRepository productCatalogRepository;
    private final WishlistNotifier notifier;

    public WishlistService(WishlistRepository wishlistRepository, ProductCatalogRepository productCatalogRepository, WishlistNotifier notifier) {
        this.wishlistRepository = wishlistRepository;
        this.productCatalogRepository = productCatalogRepository;
        this.notifier = notifier;
    }

    public void addToWishlist(final Product product, final Long userId) {
        logger.info("action=started_adding_product_to_wishlist product={}", product);

        final Wishlist wishlist = wishlistRepository.findByUserId(userId).orElse(new Wishlist());
        int wishlistSize = wishlist.getProducts().size();

        if (wishlistSize == MAX_WISHLIST_SIZE)
            throw BadRequestException.wishlistLimitReached();

        if (wishlist.getProducts().contains(product))
            throw BadRequestException.productAlreadyAddedToWishlist();

        wishlist.setUserId(userId);
        wishlist.addProduct(product);

        wishlistRepository.save(wishlist);
        notifier.notify(userId, product.getProductId());

        logger.info("action=finished_adding_product_to_wishlist product={}", product);
    }

    public WishlistResponse findProducts(final Long userId, final String searchInput) {
        logger.info("action=started_finding_products searchInput={}", searchInput);

        List<Long> productIds = wishlistRepository.findByUserId(userId)
                .orElseThrow(() -> NotFoundException.userNotFound(userId))
                .getProducts()
                .stream()
                .filter(product -> product.getName().toLowerCase().matches(format(".*%s.*", searchInput.toLowerCase())))
                .map(Product::getProductId)
                .toList();

        if (productIds.isEmpty())
            throw NotFoundException.productNotFound(searchInput);

        List<ProductCatalog> products =
                StreamSupport.stream(productCatalogRepository.findAllById(productIds).spliterator(), false)
                        .toList();

        logger.info("action=finished_finding_products searchInput={}", searchInput);

        return WishlistMapper.INSTANCE.wishlistGetRequestToWishlistResponse(userId, products);
    }
}
