package com.dev.wishlist.repositories;

import com.dev.wishlist.models.ProductProjection;
import com.dev.wishlist.models.Wishlist;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WishlistRepository extends MongoRepository<Wishlist, Long> {

    Optional<Wishlist> findByUserId(final Long userId);

        @Query(value = "{userId : ?0, products:{$elemMatch:{name: {$regex: /?1/, $options: i}}}}",
            fields = "{products:1, _id:0}")
    Optional<ProductProjection> findAllProductsByUserIdAndSearchInput(final Long userId, final String search);
}
