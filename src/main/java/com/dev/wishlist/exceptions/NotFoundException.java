package com.dev.wishlist.exceptions;

import static java.lang.String.format;

public class NotFoundException extends GlobalException {


    public static NotFoundException productNotFound(final String searchInput) {
        return new NotFoundException(ExceptionMessage.PRODUCT_NOT_FOUND, format("%s", searchInput));
    }

    public static NotFoundException userNotFound(Long userId) {
        return new NotFoundException(ExceptionMessage.USER_NOT_FOUND, format("%s", userId));
    }

    public NotFoundException(final ExceptionMessage exceptionMessage, final String... args) {
        super(exceptionMessage, args);
    }
}
