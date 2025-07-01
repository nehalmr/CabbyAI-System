package com.cabbyai.driver.exception;

public class LicenseAlreadyExistsException extends RuntimeException {
    public LicenseAlreadyExistsException(String message) {
        super(message);
    }
}
