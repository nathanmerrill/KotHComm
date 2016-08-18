package com.ppcg.kothcomm.messaging;

import java.io.*;

public class IOPipe {
    private final BufferedInputStream input;
    private final BufferedOutputStream output;
    private final Process process;


    public IOPipe(Process process){
        this.process = process;
        input = new BufferedInputStream(process.getInputStream());
        output = new BufferedOutputStream(process.getOutputStream());
    }

    private String readData(int timeout) throws IOException{
        long timeoutTime = System.currentTimeMillis() + timeout;
        int available = 0;
        while (available == 0 && System.currentTimeMillis() < timeoutTime) {
            available = input.available();
            if (available > 0) {
                byte[] bytes = new byte[available];
                input.read(bytes);
                return new String(bytes);
            }
            try {
                Thread.sleep(0);
            } catch (InterruptedException e){
                throw new RuntimeException(e);
            }
        }
        throw new IOException("No input received after "+timeout+" milliseconds");
    }

    public void sendMessage(String message) throws IOException{
        output.write(message.getBytes());
        output.flush();
    }

    public String getMessage(int timeout) throws IOException{
        return readData(timeout);
    }

    @Override
    protected void finalize() throws Throwable {
        process.destroyForcibly();
        super.finalize();
    }
}
