package KoTHComm.loader;

import java.io.File;

public class SubmissionFileManager {
    public final static String JAVA_SUBDIRECTORY = "java";
    public final static String ALTERNATE_SUBDIRECTORY = "other";

    private final File javaDirectory, alternateDirectory;

    public SubmissionFileManager(File submissionsDirectory){
        if (submissionsDirectory.isFile()){
            throw new RuntimeException("Need a directory for submissions");
        }
        this.javaDirectory = new File(submissionsDirectory, JAVA_SUBDIRECTORY);
        this.alternateDirectory = new File(submissionsDirectory, ALTERNATE_SUBDIRECTORY);
        javaDirectory.mkdir();
        alternateDirectory.mkdir();
    }

    public File getJavaDirectory() {
        return javaDirectory;
    }

    public File getAlternateDirectory() {
        return alternateDirectory;
    }
}
