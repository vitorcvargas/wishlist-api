package com.dev.wishlist.repositories;

import com.dev.wishlist.models.Wishlist;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WishlistRepository extends MongoRepository<Wishlist, Long> {

    Optional<Wishlist> findByUserId(final Long userId);
}
