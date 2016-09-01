package com.ppcg.kothcomm.messaging;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class PipeCommunicator implements Communicator<String, String>{
    public static final String COMMAND_FILE = "command.txt";
    private final File directory;
    private final IOPipe pipe;

    public PipeCommunicator(File directory){
        this.directory = directory;
        File commandFile = new File(directory, COMMAND_FILE);
        try {
            List<ProcessBuilder> commands =  Files.lines(commandFile.toPath())
                    .map(this::buildCommands)
                    .map(this::buildProcess)
                    .collect(Collectors.toList());
            ProcessBuilder last = commands.remove(commands.size()-1);
            for (ProcessBuilder builder: commands){
                builder.start().waitFor(10, TimeUnit.SECONDS);
            }
            pipe = start(last);
        } catch (IOException | InterruptedException e){
            throw new RuntimeException(e);
        }
    }

    private String[] buildCommands(String command){
        return command.trim().split(" ");
    }

    public PipeCommunicator(String directory){
        this(new File(directory));
    }

    @Override
    public String sendMessage(String message, String method, int timeout) {
        try {
            pipe.sendMessage(method + "\n" + message + "\n");
            return pipe.getMessage(timeout);
        } catch (IOException e){
            throw new RuntimeException("Unable to communicate with "+directory.getName()+". Method: "+method+" Message: "+message, e);
        }
    }

    private IOPipe start(ProcessBuilder builder){
        try {
            return new IOPipe(builder.start());
        } catch (IOException e){
            throw new RuntimeException("Invalid commands passed to process", e);
        }
    }

    private ProcessBuilder buildProcess(String...commands){
        ProcessBuilder builder = new ProcessBuilder(commands).directory(directory);
        builder.redirectInput(ProcessBuilder.Redirect.PIPE);
        builder.redirectOutput(ProcessBuilder.Redirect.PIPE);
        return builder;
    }

}
