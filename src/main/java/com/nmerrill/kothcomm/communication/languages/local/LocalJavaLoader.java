package com.nmerrill.kothcomm.communication.languages.local;

import com.nmerrill.kothcomm.communication.languages.Language;
import com.nmerrill.kothcomm.game.players.AbstractPlayer;
import com.nmerrill.kothcomm.game.players.PlayerType;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.set.MutableSet;
import org.eclipse.collections.impl.factory.Sets;

import java.io.File;
import java.util.function.Supplier;


public final class LocalJavaLoader<T extends AbstractPlayer<T>> implements Language<T> {
    private final MutableSet<PlayerType<T>> suppliers;
    public LocalJavaLoader(){
        suppliers = Sets.mutable.empty();
    }

    public void register(String name, Supplier<T> supplier){
        suppliers.add(new PlayerType<>(name, supplier));
    }

    @Override
    public boolean fileBased() {
        return false;
    }

    @Override
    public String directoryName() {
        return null;
    }

    @Override
    public String name() {
        return "local";
    }

    @Override
    public MutableList<PlayerType<T>> loadPlayers(MutableList<File> files) {
        return suppliers.toList();
    }
}
