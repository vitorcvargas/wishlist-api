package com.luizalabs.wishlist.exceptions;

public class GlobalException extends RuntimeException {

    private int code;

    GlobalException(ExceptionMessage exceptionMessage, String... args) {
        super(String.format(exceptionMessage.getMessage(), args));
        this.code = exceptionMessage.getCode();
    }

    public int getCode() {
        return code;
    }

    public void setCode(final int code) {
        this.code = code;
    }
}
