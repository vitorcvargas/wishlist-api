package com.dev.wishlist.services;

import com.dev.wishlist.repositories.WishlistRepository;
import com.dev.wishlist.exceptions.BadRequestException;
import com.dev.wishlist.models.Product;
import com.dev.wishlist.models.Wishlist;
import org.springframework.stereotype.Service;

@Service
public class WishlistService {

    private final WishlistRepository wishlistRepository;

    public WishlistService(WishlistRepository wishlistRepository) {
        this.wishlistRepository = wishlistRepository;
    }

    public void addToWishlist(final Product product, final Long userId) {

        final Wishlist wishlist = wishlistRepository.findByUserId(userId).orElse(new Wishlist());

        wishlist.setUserId(userId);
        boolean wasProductAdded = wishlist.addProduct(product);

        if (!wasProductAdded) {
            throw BadRequestException.wishlistLimitReached();
        }

        wishlistRepository.save(wishlist);
    }
}
