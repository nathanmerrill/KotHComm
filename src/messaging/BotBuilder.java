package messaging;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

public class BotBuilder<T> {
    private final File directory;
    private final Function<Communicator, T> playerConstructor;

    public BotBuilder(File directory, Function<Communicator, T> playerConstructor){
        this.directory = directory;
        this.playerConstructor = playerConstructor;
    }
    public BotBuilder(String directory, Function<Communicator, T> playerConstructor){
        this(new File(directory), playerConstructor);
    }
    public BotBuilder(Function<Communicator, T> playerConstructor){
        this(System.getProperty("user.dir")+"/bots", playerConstructor);
    }

    @SuppressWarnings("ConstantConditions")
    public Map<String, Supplier<T>> readBots(){
        Map<String, Supplier<T>> constructors = new HashMap<>();
        for (File botFolder: directory.listFiles()){
            List<String> commands = readCommandFile(botFolder);
            if (commands == null){
                continue;
            }
            ProcessBuilder builder = buildProcess(botFolder, commands.toArray(new String[]{}));
            Supplier<T> constructor = () -> playerConstructor.apply(start(builder));
            constructors.put(botFolder.getName(), constructor);
        }
        return constructors;
    }



    private Communicator start(ProcessBuilder builder){
        try {
            return new Communicator(new IOPipe(builder.start()));
        } catch (IOException e){
            throw new RuntimeException("Invalid commands passed to process", e);
        }
    }

    private ProcessBuilder buildProcess(File directory, String...commands){
        ProcessBuilder builder = new ProcessBuilder(commands).directory(directory);
        builder.redirectInput(ProcessBuilder.Redirect.PIPE);
        builder.redirectOutput(ProcessBuilder.Redirect.PIPE);
        return builder;
    }

    private List<String> readCommandFile(File containingFolder){
        File commandFile = new File(containingFolder, "commands.txt");
        Scanner scanner;
        try {
            scanner = new Scanner(commandFile);
        } catch (FileNotFoundException exception){
            return null;
        }
        List<String> lines = new ArrayList<>();
        while (scanner.hasNext()){
            lines.add(scanner.nextLine());
        }
        return lines;
    }

}
