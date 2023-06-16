package com.dev.wishlist.repositories;

import com.dev.wishlist.models.ProductCatalog;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductCatalogRepository extends CrudRepository<ProductCatalog, Long> {
}
