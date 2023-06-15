package com.dev.wishlist.repositories;

import com.dev.wishlist.models.ProductDetails;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductDetailsRepository extends CrudRepository<ProductDetails, Long> {
}
