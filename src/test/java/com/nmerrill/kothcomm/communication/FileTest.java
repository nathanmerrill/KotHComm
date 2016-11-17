package com.nmerrill.kothcomm.communication;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public interface FileTest {


    @SuppressWarnings("ResultOfMethodCallIgnored")
    default void createTestFolder(){
        testDirectory().mkdirs();
    }

    default File write(String contents, String filename) throws IOException {
        File location = new File(testDirectory(), filename);
        FileWriter writer = new FileWriter(location);
        writer.write(contents);
        writer.close();
        return location;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    default void deleteRecursive(File file){
        if (file.isFile()){
            file.delete();
            return;
        }
        File[] children = file.listFiles();
        if (children != null){
            for (File child: children){
                deleteRecursive(child);
            }
        }
        file.delete();
    }

    default void deleteTestFolder(){
        deleteRecursive(testDirectory());
    }

    File testDirectory();
}
