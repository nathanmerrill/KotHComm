package com.nmerrill.kothcomm.exceptions;

public class DownloadingException extends RuntimeException {
    public DownloadingException() {

    }

    public DownloadingException(String message) {
        super(message);
    }

    public DownloadingException(String message, Throwable cause) {
        super(message, cause);
    }

    public DownloadingException(Throwable cause) {
        super(cause);
    }
}
