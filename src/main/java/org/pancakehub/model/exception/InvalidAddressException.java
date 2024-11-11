package org.pancakehub.model.exception;

public class InvalidAddressException extends IllegalArgumentException {
    public InvalidAddressException(String message) {
        super(message);
    }
}
