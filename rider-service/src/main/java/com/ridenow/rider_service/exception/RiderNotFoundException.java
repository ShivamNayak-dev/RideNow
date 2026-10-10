package com.ridenow.rider_service.exception;

public class RiderNotFoundException extends RuntimeException {

    public RiderNotFoundException(String message) {
        super(message);
    }
}