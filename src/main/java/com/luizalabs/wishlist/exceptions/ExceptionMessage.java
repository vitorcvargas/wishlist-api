package com.luizalabs.wishlist.exceptions;

public enum ExceptionMessage {

    WISHLIST_LIMIT_REACHED(1, "It was not possible to add the selected product to the wishlist as it is already full.");


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
