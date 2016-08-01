package messaging;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;

public class PipeCommunicator implements Communicator<String, String>{
    public static final String BOTS_FOLDER = System.getProperty("user.dir")+"/bots";
    public static final String COMMAND_FILE = "command.txt";
    private final File directory;
    private final IOPipe pipe;

    public PipeCommunicator(File directory){
        this.directory = directory;
        File commandFile = new File(directory, COMMAND_FILE);
        try {
            List<ProcessBuilder> commands =  Files.lines(commandFile.toPath())
                    .map(a -> buildProcess(a.split(" ")))
                    .collect(Collectors.toList());
            ProcessBuilder last = commands.remove(commands.size()-1);
            for (ProcessBuilder builder: commands){
                builder.start().waitFor();
            }
            pipe = new IOPipe(last.start());
        } catch (IOException | InterruptedException e){
            throw new RuntimeException(e);
        }
    }
    public PipeCommunicator(String directory){
        this(new File(directory));
    }
    public PipeCommunicator(){
        this(BOTS_FOLDER);
    }

    @Override
    public String sendMessage(String message, String method, int timeout) {
        pipe.sendMessage(method+"\n"+message+"\n");
        return pipe.getMessage(timeout);
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
