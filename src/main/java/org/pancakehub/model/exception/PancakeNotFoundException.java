package org.pancakehub.model.exception;

public class PancakeNotFoundException extends IllegalArgumentException {
    public PancakeNotFoundException(String message) {
        super(message);
    }
}
