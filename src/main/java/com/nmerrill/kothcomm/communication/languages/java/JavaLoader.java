package com.nmerrill.kothcomm.communication.languages.java;

import com.nmerrill.kothcomm.communication.languages.Language;
import com.nmerrill.kothcomm.exceptions.LanguageLoadException;
import com.nmerrill.kothcomm.game.players.AbstractPlayer;
import com.nmerrill.kothcomm.game.players.PlayerType;
import org.eclipse.collections.api.list.MutableList;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

/**
 * Loads players contained in Java files.
 * Players should be located in the /java folder in the submissions directory
 * @param <T> the type of player you are loading
 */
public final class JavaLoader<T extends AbstractPlayer<T>> implements Language<T> {

    private final Class<T> playerType;
    private final Compiler compiler;

    /**
     * Create a Java loader
     * @param playerType The class of the player you want to create.
     */
    public JavaLoader(Class<T> playerType){
        this.playerType = playerType;
        this.compiler = new Compiler();
    }

    /**
     * @inheritDoc
     */
    @Override
    public boolean fileBased() {
        return true;
    }

    /**
     * @inheritDoc
     */
    @Override
    public String directoryName() {
        return "java";
    }

    /**
     * @inheritDoc
     */
    @Override
    public String name() {
        return "Java";
    }

    /**
     * @inheritDoc
     */
    @Override
    public MutableList<PlayerType<T>> loadPlayers(MutableList<File> files) {
        return compiler.compile(files.select(f -> f.getName().endsWith(".java")))
                .collectIf(this::isConstructable, this::classToPlayerType);
    }

    private boolean isConstructable(Class clazz){
        return playerType.isAssignableFrom(clazz) &&
                !Modifier.isAbstract(clazz.getModifiers()) &&
                !Modifier.isInterface(clazz.getModifiers());
    }

    @SuppressWarnings("unchecked")
    private PlayerType<T> classToPlayerType(Class clazz){
        try {
            Constructor<? extends T> constructor = clazz.asSubclass(playerType).getConstructor();
            return new PlayerType<>(clazz.getSimpleName(), () -> safeCallConstructor(constructor));
        } catch(NoSuchMethodException e){
            throw new LanguageLoadException(e);
        }
    }

    private T safeCallConstructor(Constructor<? extends T> constructor){
        try {
            return constructor.newInstance();
        } catch (IllegalAccessException|InvocationTargetException |InstantiationException e) {
            throw new LanguageLoadException(e);
        }
    }
}
