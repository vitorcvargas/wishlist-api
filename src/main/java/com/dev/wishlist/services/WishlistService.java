package com.dev.wishlist.services;

import com.dev.wishlist.exceptions.BadRequestException;
import com.dev.wishlist.models.Product;
import com.dev.wishlist.models.Wishlist;
import com.dev.wishlist.repositories.WishlistRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import static com.dev.wishlist.utils.APIConstants.MAX_WISHLIST_SIZE;

@Service
public class WishlistService {

    private final Logger logger = LoggerFactory.getLogger(WishlistService.class);
    private final WishlistRepository wishlistRepository;
    private final WishlistNotifier notifier;

    public WishlistService(WishlistRepository wishlistRepository, WishlistNotifier notifier) {
        this.wishlistRepository = wishlistRepository;
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
}
