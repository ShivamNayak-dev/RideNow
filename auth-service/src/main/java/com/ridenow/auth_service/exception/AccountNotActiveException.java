package com.ridenow.auth_service.exception;

public class AccountNotActiveException extends RuntimeException {
    public AccountNotActiveException() {
        super("Account is not active");
    }
}
