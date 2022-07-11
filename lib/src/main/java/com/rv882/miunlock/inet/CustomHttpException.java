package com.rv882.miunlock.inet;

public class CustomHttpException extends Exception {
    public CustomHttpException(String message) {
        super(message);
    }

    public CustomHttpException(String message, Throwable cause) {
        super(message, cause);
    }
}
