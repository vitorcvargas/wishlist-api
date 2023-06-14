package com.luizalabs.wishlist.repositories;

import com.luizalabs.wishlist.models.Wishlist;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WishlistRepository extends MongoRepository<Wishlist, Long> {
}
