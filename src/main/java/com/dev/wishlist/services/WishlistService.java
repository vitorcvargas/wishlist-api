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

    public WishlistService(WishlistRepository wishlistRepository) {
        this.wishlistRepository = wishlistRepository;
    }

    public void addToWishlist(final Product product, final Long userId) {

        final Wishlist wishlist = wishlistRepository.findByUserId(userId).orElse(new Wishlist());
        int wishlistSize = wishlist.getProducts().size();

        if(wishlistSize == MAX_WISHLIST_SIZE)
            throw BadRequestException.wishlistLimitReached();

        if(wishlist.getProducts().contains(product))
            throw BadRequestException.productAlreadyAddedToWishlist();

        wishlist.setUserId(userId);
        wishlist.addProduct(product);

        wishlistRepository.save(wishlist);
    }
}
