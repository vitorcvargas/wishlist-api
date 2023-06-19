package com.dev.wishlist.exceptions;

import static java.lang.String.format;

public class NotFoundException extends GlobalException {


    public static NotFoundException productNotFoundWithSearchInput(final String searchInput, final Long userId, final String wishlistId) {
        return new NotFoundException(ExceptionMessage.PRODUCT_NOT_FOUND_WITH_SEARCH_INPUT, searchInput, userId.toString(), wishlistId);
    }

    public static NotFoundException productNotFound(final Long userId, final String wishlistId) {
        return new NotFoundException(ExceptionMessage.PRODUCT_NOT_FOUND, wishlistId, userId.toString());
    }

    public static NotFoundException productNotFoundWithId(final Long productId, final Long userId, final String wishlistId) {
        return new NotFoundException(ExceptionMessage.PRODUCT_NOT_FOUND_WITH_ID, productId.toString(), userId.toString(), wishlistId);
    }

    public static NotFoundException wishlistNotFound(Long userId, String wishlistId) {
        return new NotFoundException(ExceptionMessage.WISHLIST_NOT_FOUND, userId.toString(), wishlistId);
    }

    public NotFoundException(final ExceptionMessage exceptionMessage, final String... args) {
        super(exceptionMessage, args);
    }


}
