package com.dev.wishlist.repositories;

import com.dev.wishlist.models.Wishlist;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WishlistRepository extends MongoRepository<Wishlist, Long> {

    Optional<List<Wishlist>> findByUserId(final Long userId);

    @Query(
            value = "{userId: ?0, _id: ?1}",
            fields = "{products: 1, userId: 1, name: 1, isPublic: 1}"
    )
    Optional<Wishlist> findByUserIdAndWishlistId(final Long userId, final String wishlistId);
}
