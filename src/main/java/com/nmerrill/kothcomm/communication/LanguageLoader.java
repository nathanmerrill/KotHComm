package com.nmerrill.kothcomm.communication;

import com.nmerrill.kothcomm.communication.languages.Language;
import com.nmerrill.kothcomm.exceptions.LanguageLoadException;
import com.nmerrill.kothcomm.game.PlayerType;
import com.nmerrill.kothcomm.game.AbstractPlayer;
import org.eclipse.collections.api.RichIterable;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.set.MutableSet;
import org.eclipse.collections.impl.factory.Lists;
import org.eclipse.collections.impl.factory.Sets;

import java.io.File;

public final class LanguageLoader<T extends AbstractPlayer<T>> {

    private final MutableSet<Language<T>> loaders;
    private final MutableSet<String> excludedLanguages;
    private File submissionsDirectory;

    public LanguageLoader(){
        this.loaders = Sets.mutable.empty();
        this.excludedLanguages = Sets.mutable.empty();
    }

    public LanguageLoader(Arguments arguments){
        this();
        setSubmissionsDirectory(arguments.submissionDirectory());
        arguments.excludedLanguages.forEach(this::excludeLoader);
    }

    public void setSubmissionsDirectory(File submissionsDirectory) {
        if (submissionsDirectory.isFile()){
            throw new LanguageLoadException("Need a directory for submissions");
        }
        this.submissionsDirectory = submissionsDirectory;
    }

    public Language<T> byName(String name){
        if (loaders.size() == 1){
            return loaders.getOnly();
        }
        RichIterable<Language<T>> iter = loaders.selectWith(this::namesMatch, name);
        if (iter.isEmpty()){
            return null;
        }
        return iter.getOnly();
    }

    private boolean namesMatch(Language loader, String name){
        return name.toLowerCase().equals(loader.name().toLowerCase());
    }

    private boolean excluded(Language loader){
        return excludedLanguages.contains(loader.name());
    }

    public File getDirectory(Language loader){
        return new File(submissionsDirectory, loader.directoryName());
    }

    public MutableList<Language<T>> getLoaders(){
        return loaders.reject(this::excluded).toList();
    }

    public void excludeLoader(String name){
        excludedLanguages.add(name);
    }

    public void addLoader(Language<T> loader){
        loaders.add(loader);
    }

    private MutableList<File> getFiles(Language loader){
        String directoryName = loader.directoryName();
        if (directoryName == null) {
            return null;
        }
        File directory = new File(submissionsDirectory, directoryName);
        //noinspection ResultOfMethodCallIgnored
        directory.mkdir();
        File[] folders = directory.listFiles();
        return Lists.mutable.of(folders);
    }

    public MutableList<PlayerType<T>> load(){
        return getLoaders().flatCollect(l -> l.loadPlayers(getFiles(l)));
    }

}
