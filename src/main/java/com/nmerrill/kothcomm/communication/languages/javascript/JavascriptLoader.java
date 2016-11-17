package com.nmerrill.kothcomm.communication.languages.javascript;

import com.nmerrill.kothcomm.game.AbstractPlayer;
import com.nmerrill.kothcomm.game.PlayerType;
import com.nmerrill.kothcomm.communication.languages.Language;
import org.eclipse.collections.api.list.MutableList;

import java.io.File;

public final class JavascriptLoader<T extends AbstractPlayer<T>> implements Language<T> {

    public JavascriptLoader(){

    }

    @Override
    public boolean fileBased() {
        return true;
    }

    @Override
    public String directoryName() {
        return "js";
    }

    @Override
    public String name() {
        return "Javascript";
    }

    @Override
    public MutableList<PlayerType<T>> loadPlayers(MutableList<File> files) {
        return null;
    }
}
