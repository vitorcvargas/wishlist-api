package com.dev.wishlist.exceptions;

public class BadRequestException extends GlobalException {


    public static BadRequestException wishlistLimitReached() {
        return new BadRequestException(ExceptionMessage.WISHLIST_LIMIT_REACHED);
    }

    public BadRequestException(final ExceptionMessage exceptionMessage, final String... args) {
        super(exceptionMessage, args);
    }
}
