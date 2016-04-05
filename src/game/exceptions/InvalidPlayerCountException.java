package game.exceptions;

public class InvalidPlayerCountException extends RuntimeException{
    public InvalidPlayerCountException(String message){
        super(message);
    }
}
