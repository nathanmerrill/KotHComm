package messaging;

import java.io.*;
import java.util.Arrays;

public class IOPipe extends TimedPipe {
    private final BufferedInputStream input;
    private final BufferedOutputStream output;


    public IOPipe(String...commands){
        ProcessBuilder builder = new ProcessBuilder(commands);
        builder.redirectInput(ProcessBuilder.Redirect.PIPE);
        builder.redirectOutput(ProcessBuilder.Redirect.PIPE);
        Process process;
        try {
            process = builder.start();
        } catch (IOException e){
            throw new RuntimeException("Invalid commands passed to process:\n"+ Arrays.toString(commands), e);
        }

        input = new BufferedInputStream(process.getInputStream());
        output = new BufferedOutputStream(process.getOutputStream());
    }

    private String readData(int timeout) {
        long timeoutTime = System.currentTimeMillis() + timeout;
        int available = 0;
        while (available == 0 && System.currentTimeMillis() < timeoutTime) {
            try {
                available = input.available();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if (available > 0) {
                byte[] bytes = new byte[available];
                return new String(bytes);
            }
            try {
                Thread.sleep(0);
            } catch (InterruptedException e){
                throw new RuntimeException(e);
            }
        }
        return "";
    }

    @Override
    public void sendMessage(String message){
        try {
            output.write(message.getBytes());
        } catch (IOException e) {
            throw new RuntimeException("Unable to write message: "+message, e);
        }
    }

    @Override
    public String getMessage(int timeout) {
        return readData(timeout);
    }

}
