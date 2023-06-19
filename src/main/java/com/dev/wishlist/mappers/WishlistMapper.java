package com.dev.wishlist.mappers;

import com.dev.wishlist.models.ProductCatalog;
import com.dev.wishlist.models.Wishlist;
import com.dev.wishlist.rest.dtos.WishlistResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface WishlistMapper {
    WishlistMapper INSTANCE = Mappers.getMapper(WishlistMapper.class);

    default WishlistResponse wishlistGetRequestToWishlistResponse(final Wishlist wishlist, final List<ProductCatalog> products) {
        WishlistResponse response = new WishlistResponse();
        response.setId(wishlist.getId());
        response.setName(wishlist.getName());
        response.setUserId(wishlist.getUserId());
        response.setProducts(products);

        return response;
    }
}
