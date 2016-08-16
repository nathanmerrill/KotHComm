package loader;

import KoTHComm.loader.Compiler;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


public class CompilerTester {
    private final String className = "Test";
    private final File file = new File(className+".java");

    @BeforeClass
    public void createFile() throws IOException {
        String classFile = "public class "+className+"{}";
        FileOutputStream stream = new FileOutputStream(file);
        stream.write(classFile.getBytes());
    }

    @Test
    public void createClass(){
        Compiler compiler = new Compiler();
        Class clazz = compiler.compile(file);
        Assert.assertEquals(clazz.getSimpleName(), className);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @AfterClass
    public void cleanUp(){
        file.delete();
        new File(className+".class").delete();
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
