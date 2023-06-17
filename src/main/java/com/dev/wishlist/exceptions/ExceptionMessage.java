package com.dev.wishlist.exceptions;

public enum ExceptionMessage {

    WISHLIST_LIMIT_REACHED(1, "It was not possible to add the selected product to the wishlist as it is already full."),
    PRODUCT_ALREADY_ADDED_TO_WISHLIST(2, "Product already added to wishlist."),
    PRODUCT_NOT_FOUND_WITH_SEARCH_INPUT(3, "Product not found with search input: %s"),
    WISHLIST_NOT_FOUND_FOR_USER(4, "Wishlist not found for user with id: %s"),
    PRODUCT_NOT_FOUND_WITH_ID(5, "Product not found with id: %s");


    private int code;
    private String message;

    ExceptionMessage(final int code, final String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(final int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }
}
