package com.ppcg.kothcomm.messaging;

public interface Communicator<T, U> {
    int DEFAULT_TIMEOUT = 5000;
    U sendMessage(T message, String method, int timeout);
    default U sendMessage(T message, String method){
        return sendMessage(message, method, DEFAULT_TIMEOUT);
    }
}
