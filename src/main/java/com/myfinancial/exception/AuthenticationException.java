package com.myfinancial.exception;

public class AuthenticationException extends RuntimeException {

    public AuthenticationException(String mesagem) {
        super(mesagem);
    }
}
