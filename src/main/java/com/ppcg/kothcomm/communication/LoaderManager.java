package com.ppcg.kothcomm.communication;

import com.ppcg.kothcomm.communication.languages.LanguageLoader;
import com.ppcg.kothcomm.game.AbstractPlayer;
import com.ppcg.kothcomm.game.PlayerType;
import org.eclipse.collections.api.RichIterable;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.map.MutableMap;
import org.eclipse.collections.impl.factory.Lists;
import org.eclipse.collections.impl.factory.Maps;

import java.io.File;

public class LoaderManager<T extends AbstractPlayer<T>> {

    private final MutableMap<LanguageLoader<T>, MutableList<File>> loaders;
    private final LanguageLoader<T> defaultLanguageLoader;
    private final File submissionsDirectory;

    public LoaderManager(File submissionsDirectory, LanguageLoader<T> defaultLanguageLoader){
        if (submissionsDirectory.isFile()){
            throw new RuntimeException("Need a directory for submissions");
        }
        this.submissionsDirectory = submissionsDirectory;
        this.loaders = Maps.mutable.empty();
        this.defaultLanguageLoader = defaultLanguageLoader;
    }

    public LanguageLoader<T> byName(String name){
        RichIterable<LanguageLoader<T>> iter = loaders.keysView().select(i -> name.toLowerCase().equals(i.name().toLowerCase()));
        if (iter.isEmpty()){
            return defaultLanguageLoader;
        }
        return iter.getOnly();
    }

    public File getDirectory(LanguageLoader loader){
        return new File(submissionsDirectory, loader.directoryName());
    }

    public MutableList<LanguageLoader<T>> getLoaders(){
        return loaders.keysView().toList();
    }

    public void addLoader(LanguageLoader<T> loader){
        File directory = new File(submissionsDirectory, loader.directoryName());
        if (!directory.mkdir()){
            throw new RuntimeException("Cannot create directory at "+directory.toString());
        }
        File[] folders = directory.listFiles();
        loaders.put(loader, Lists.mutable.of(folders));
    }

    public MutableList<PlayerType<T>> load(){
        return loaders.keyValuesView()
                .flatCollect(p -> p.getOne().loadPlayers(p.getTwo()))
                .toList();
    }

}
