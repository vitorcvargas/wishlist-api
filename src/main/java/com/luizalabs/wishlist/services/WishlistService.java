package com.luizalabs.wishlist.services;

import com.luizalabs.wishlist.exceptions.BadRequestException;
import com.luizalabs.wishlist.models.Product;
import com.luizalabs.wishlist.models.Wishlist;
import com.luizalabs.wishlist.repositories.WishlistRepository;
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
