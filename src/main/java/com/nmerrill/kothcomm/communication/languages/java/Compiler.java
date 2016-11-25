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

/**
 * This class can be used to take a list of files and compile them into a list of classes
 *
 * Use of this class requires the JDK, not the JRE.
 */
public final class Compiler {
    private final DiagnosticCollector<JavaFileObject> diagnostics;
    private final JavaCompiler compiler;
    private final StandardJavaFileManager fileManager;

    /**
     *  Creates your compiler.  Uses the system java compiler, and requires the JDK
     *  If the JDK is not found, a LanguageLoadException will be thrown
     */
    public Compiler() {
        diagnostics = new DiagnosticCollector<>();
        compiler = ToolProvider.getSystemJavaCompiler();
        if (compiler == null) {
            throw new LanguageLoadException("Compiler not available. Either include tools.jar in your classpath or run from the JDK");
        }
        fileManager = compiler.getStandardFileManager(diagnostics, null, null);
    }

    /**
     * Compiles a list of files as a collection.  The files should have no package declaration.
     * If compilation is not successful, a LanguageLoadException is thrown
     *
     * @param files Files that you want to compile
     * @return List of classes for each file.
     */

    public MutableList<Class> compile(MutableList<File> files) {
        if (files.size() == 0) {
            return Lists.mutable.empty();
        }
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
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
                            (diagnostic.getSource()==null)?"":
                            "Error at "+diagnostic.getSource().getName()
                                    + " line " + diagnostic.getLineNumber()
                                    + ": " +diagnostic.getMessage(null)
                    ).collect(Collectors.joining("\n"));
            throw new LanguageLoadException(errors);
        }
    }


}
