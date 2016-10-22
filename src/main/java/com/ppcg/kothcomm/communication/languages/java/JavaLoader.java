package com.ppcg.kothcomm.communication.languages.java;

import com.ppcg.kothcomm.game.AbstractPlayer;
import com.ppcg.kothcomm.game.PlayerType;
import com.ppcg.kothcomm.communication.languages.LanguageLoader;
import org.eclipse.collections.api.list.MutableList;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;


public class JavaLoader<T extends AbstractPlayer<T>> implements LanguageLoader<T>{

    private final Class<T> playerType;
    private final Compiler compiler;
    public JavaLoader(Class<T> playerType){
        this.playerType = playerType;
        this.compiler = new Compiler();
    }

    @Override
    public String directoryName() {
        return "java";
    }

    @Override
    public String name() {
        return "Java";
    }

    @Override
    public MutableList<PlayerType<T>> loadPlayers(MutableList<File> files) {
        return files
                .select(f -> f.getName().endsWith(".java"))
                .collect(compiler::compile)
                .collect(this::cast)
                .collect(this::classToPlayerType);
    }

    @SuppressWarnings("unchecked")
    private Class<? extends T> cast(Class clazz){
        return playerType.asSubclass(clazz);
    }

    private PlayerType<T> classToPlayerType(Class<? extends T> clazz){
        try {
            Constructor<? extends T> constructor = clazz.getConstructor();
            return new PlayerType<>(clazz.getSimpleName(), () -> safeCallConstructor(constructor));
        } catch(NoSuchMethodException e){
            throw new RuntimeException(e);
        }
    }

    private T safeCallConstructor(Constructor<? extends T> constructor){
        try {
            return constructor.newInstance();
        } catch (IllegalAccessException|InvocationTargetException |InstantiationException e) {
            throw new RuntimeException(e);
        }
    }
}
