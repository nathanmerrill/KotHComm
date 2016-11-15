package com.nmerrill.kothcomm.communication.languages.java;


import com.nmerrill.kothcomm.exceptions.LanguageLoadException;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.impl.factory.Lists;

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

    public Compiler() {
        diagnostics = new DiagnosticCollector<>();
        compiler = ToolProvider.getSystemJavaCompiler();
        if (compiler == null) {
            throw new LanguageLoadException("Compiler not available. Either include tools.jar in your classpath or run from the JDK");
        }
        fileManager = compiler.getStandardFileManager(diagnostics, null, null);
    }

    public MutableList<Class> compile(MutableList<File> files) {
        if (files.size() == 0) {
            return Lists.mutable.empty();
        }
        JavaCompiler.CompilationTask task = compiler.getTask(
                null,
                null,
                diagnostics,
                Arrays.asList("-classpath", System.getProperty("java.class.path")),
                null,
                fileManager.getJavaFileObjects(files.toArray(new File[]{})));
        if (task.call()) {
            URLClassLoader classLoader;
            try {
                classLoader = new URLClassLoader(new URL[]{files.get(0).getAbsoluteFile().getParentFile().toURI().toURL()});
            } catch (MalformedURLException e) {
                throw new LanguageLoadException("Unable to load class file", e);
            }
            return files
                    .collect(f -> f.getName().split("\\.")[0])
                    .collect(f -> {
                        try {
                            return classLoader.loadClass(f);
                        } catch (ClassNotFoundException e) {
                            throw new LanguageLoadException("Unable to load the class", e);
                        }
                    });
        } else {
            String errors = diagnostics.getDiagnostics().stream()
                    .map(diagnostic ->
                            "Error at "+diagnostic.getSource().getName()
                                    + " line " + diagnostic.getLineNumber()
                                    + ": " +diagnostic.getMessage(null)
                    ).collect(Collectors.joining("\n"));
            throw new LanguageLoadException(errors);
        }
    }
}
