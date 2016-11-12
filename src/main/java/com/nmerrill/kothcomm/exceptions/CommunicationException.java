package com.nmerrill.kothcomm.exceptions;

public class CommunicationException extends RuntimeException {
    public CommunicationException() {

    }

    public CommunicationException(String message) {
        super(message);
    }

    public CommunicationException(String message, Throwable cause) {
        super(message, cause);
    }

    public CommunicationException(Throwable cause) {
        super(cause);
    }
}
