package messaging;

public interface Pipe {

    void sendMessage(String message);
    String getMessage();

    default String request(String message) {
        sendMessage(message);
        return getMessage();
    }
}
