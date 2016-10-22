package com.ppcg.kothcomm.communication.languages.javascript;

import com.ppcg.kothcomm.game.AbstractPlayer;
import com.ppcg.kothcomm.game.PlayerType;
import com.ppcg.kothcomm.communication.languages.LanguageLoader;
import org.eclipse.collections.api.list.MutableList;

import java.io.File;

public class JavascriptLoader<T extends AbstractPlayer<T>> implements LanguageLoader<T>{

    public JavascriptLoader(){

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
