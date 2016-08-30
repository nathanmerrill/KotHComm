package com.ppcg.kothcomm.loader;


import javax.tools.*;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.stream.Collectors;

public final class Compiler {
    private final DiagnosticCollector<JavaFileObject> diagnostics;
    private final JavaCompiler compiler;
    private final StandardJavaFileManager fileManager;
    public Compiler(){
        diagnostics = new DiagnosticCollector<>();
        compiler = ToolProvider.getSystemJavaCompiler();
        if (compiler == null){
            throw new RuntimeException("Compiler not available.  Either include tools.jar in your classpath or run from the JDK");
        }
        fileManager = compiler.getStandardFileManager(diagnostics, null, null);
    }

    public Class compile(File file){
        JavaCompiler.CompilationTask task = compiler.getTask(
                null,
                null,
                diagnostics,
                Arrays.asList("-classpath", System.getProperty("java.class.path")),
                null,
                fileManager.getJavaFileObjects(file));
        if (task.call()) {
            URLClassLoader classLoader;
            try {
                classLoader = new URLClassLoader(new URL[]{new File(file, "../").toURI().toURL()});
            } catch (MalformedURLException e){
                throw new RuntimeException("Unable to load class file", e);
            }
            try {
                return classLoader.loadClass(file.getName().split("\\.")[0]);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("Unable to load the class", e);
            }
        } else {
            String errors = diagnostics.getDiagnostics().stream()
                    .map(diagnostic ->
                            String.format("Error on line %d: %s%n",
                                    diagnostic.getLineNumber(),
                                    diagnostic.getMessage(null))
                    ).collect(Collectors.joining("\n"));
            throw new RuntimeException("Error when compiling "+file.getName()+"\n"+errors);
        }
    }
}
