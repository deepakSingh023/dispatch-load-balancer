package com.deepaksingh023.dispatch.error.exceptions;

public class NoOrdersToProcess extends RuntimeException {
    public NoOrdersToProcess(String message) {
        super(message);
    }
}
