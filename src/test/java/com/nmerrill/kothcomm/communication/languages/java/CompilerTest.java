package com.nmerrill.kothcomm.communication.languages.java;

import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.impl.factory.Lists;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CompilerTest {
    private final File testFolder = new File("compiler_test");

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @BeforeClass
    public void createTestFolder(){
        testFolder.mkdir();
    }

    @Test
    public void testCompilation() throws Exception{
        String output = "Hello";
        File source = write("public class Test { public String toString(){ return \""+output+"\";}}", "Test.java");
        Compiler compiler = new Compiler();
        List<Class> classes = compiler.compile(Lists.mutable.of(source));
        Assert.assertNotNull(classes);
        Assert.assertEquals(1, classes.size());
        Assert.assertEquals(output,create(classes.get(0)).toString());
    }

    @Test
    public void testMultipleCompilation() throws Exception{
        String output = "Hello";
        String append = "Append";
        File creator = write("public class Creator { public String toString(){ return new Created().toString()+\""+append+"\";}}", "Creator.java");
        File created = write("public class Created { public String toString(){ return \""+output+"\";}}", "Created.java");
        Compiler compiler = new Compiler();
        MutableList<Class> classes = compiler.compile(Lists.mutable.of(creator, created));
        Assert.assertNotNull(classes);
        Assert.assertEquals(2, classes.size());
        Assert.assertEquals(output+append,create(classes.get(0)).toString());
        Assert.assertEquals(output,create(classes.get(1)).toString());
    }

    @AfterClass(alwaysRun = true)
    public void deleteTestFolder(){
        deleteRecursive(testFolder);
    }

    private Object create(Class<?> clazz) throws Exception{
        return clazz.getConstructor().newInstance();
    }

    private File write(String contents, String filename) throws IOException {
        File location = new File(testFolder, filename);
        FileWriter writer = new FileWriter(location);
        writer.write(contents);
        writer.close();
        return location;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void deleteRecursive(File file){
        if (file.isFile()){
            file.delete();
            return;
        }
        File[] children = testFolder.listFiles();
        if (children != null){
            for (File child: children){
                deleteRecursive(child);
            }
        }
        testFolder.delete();
    }
}
