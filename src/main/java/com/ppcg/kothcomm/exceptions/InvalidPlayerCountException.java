package com.ppcg.kothcomm.exceptions;

/**
 * This exception is thrown when the number of players goes outside the minimum or maximum bounds
 */
public class InvalidPlayerCountException extends RuntimeException{
    public InvalidPlayerCountException(String message){
        super(message);
    }
}
