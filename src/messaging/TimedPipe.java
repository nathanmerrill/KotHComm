package messaging;


public abstract class TimedPipe implements Pipe {

    private int defaultTimeout;

    public TimedPipe(){
        this(100);
    }

    public TimedPipe(int defaultTimeout){
        this.defaultTimeout = defaultTimeout;
    }

    public int getDefaultTimeout() {
        return defaultTimeout;
    }

    public void setDefaultTimeout(int defaultTimeout) {
        this.defaultTimeout = defaultTimeout;
    }

    @Override
    public String getMessage(){
        return getMessage(defaultTimeout);
    }

    public abstract String getMessage(int timeout);

    public String request(String message, int timeout){
        sendMessage(message);
        return getMessage(timeout);
    }

}
