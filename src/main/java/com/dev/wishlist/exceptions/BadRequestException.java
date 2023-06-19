package com.dev.wishlist.exceptions;

public class BadRequestException extends GlobalException {


    public static BadRequestException wishlistLimitReached() {
        return new BadRequestException(ExceptionMessage.WISHLIST_LIMIT_REACHED);
    }

    public static BadRequestException productAlreadyAddedToWishlist() {
        return new BadRequestException(ExceptionMessage.PRODUCT_ALREADY_ADDED_TO_WISHLIST);
    }

    public static BadRequestException wishlistAlreadyCreatedWithName(String name, Long userId) {
        return new BadRequestException(ExceptionMessage.WISHLIST_ALREADY_CREATED, name, userId.toString());
    }

    public BadRequestException(final ExceptionMessage exceptionMessage, final String... args) {
        super(exceptionMessage, args);
    }
}
