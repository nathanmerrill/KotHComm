package com.nmerrill.kothcomm.exceptions;

public class LanguageLoadException extends RuntimeException {
    public LanguageLoadException() {

    }

    public LanguageLoadException(String message) {
        super(message);
    }

    public LanguageLoadException(String message, Throwable cause) {
        super(message, cause);
    }

    public LanguageLoadException(Throwable cause) {
        super(cause);
    }
}
