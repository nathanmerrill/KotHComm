package com.nmerrill.kothcomm.communication;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class Arguments {

    @Parameter(names = {"-i", "--iterations"}, description = "Number of games to run")
    public int iterations = 50;

    @Parameter(names = {"-e", "--exclude"}, description = "Languages that should be excluded.  Use 'other' for languages without built-in support, or 'local' for local classes.  You can prefix a language with `non` to exclude everything but that language", variableArity = true)
    public List<String> excludedLanguages = new ArrayList<>();

    @Parameter(names = {"-d", "--directory"}, description = "Directory for submissions")
    public String directory = "submissions";

    @Parameter(names = {"-q", "--question-id"}, description = "Question id to download from")
    public int questionID = -1;

    @Parameter(names = {"-r", "--random-seed"}, description = "Seed for random")
    public int randomSeed = -1;

    @Parameter(names = {"-g", "--gui"}, description = "Whether to use a GUI")
    public boolean useGui = false;

    @Parameter(names = {"-h", "--help"}, description = "Help")
    public boolean help = false;


    public Random getRandom(){
        if (randomSeed == -1){
            return new Random();
        }
        return new Random(randomSeed);
    }

    public boolean validQuestionID(){
        return questionID > 0;
    }

    public File submissionDirectory(){
        return new File(directory);
    }

    public static <T extends Arguments> T parse(String[] args, T argClass){
        JCommander commander = new JCommander(argClass);
        commander.setCaseSensitiveOptions(false);
        commander.parse(args);
        if (argClass.help){
            commander.usage();
            System.exit(0);
        }
        return argClass;
    }

    public static Arguments parse(String[] args){
        return parse(args, new Arguments());
    }

}
