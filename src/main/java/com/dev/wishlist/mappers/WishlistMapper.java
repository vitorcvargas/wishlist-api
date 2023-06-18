package com.dev.wishlist.mappers;

import com.dev.wishlist.rest.dtos.WishlistResponse;
import com.dev.wishlist.models.ProductCatalog;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface WishlistMapper {
    WishlistMapper INSTANCE = Mappers.getMapper(WishlistMapper.class);

    WishlistResponse wishlistGetRequestToWishlistResponse(final Long userId, final List<ProductCatalog> products);
}
