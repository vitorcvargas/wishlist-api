package com.dev.wishlist.exceptions;

import static java.lang.String.format;

public class NotFoundException extends GlobalException {


    public static NotFoundException productNotFoundWithSearchInput(final String searchInput) {
        return new NotFoundException(ExceptionMessage.PRODUCT_NOT_FOUND_WITH_SEARCH_INPUT, format("%s", searchInput));
    }

    public static NotFoundException productNotFoundWithId(final Long productId) {
        return new NotFoundException(ExceptionMessage.PRODUCT_NOT_FOUND_WITH_ID, format("%s", productId));
    }

    public static NotFoundException wishlistNotFoundForUserId(Long userId) {
        return new NotFoundException(ExceptionMessage.WISHLIST_NOT_FOUND_FOR_USER, format("%s", userId));
    }

    public NotFoundException(final ExceptionMessage exceptionMessage, final String... args) {
        super(exceptionMessage, args);
    }
}
