package com.ppcg.kothcomm.communication.languages;

import com.ppcg.kothcomm.game.AbstractPlayer;
import com.ppcg.kothcomm.game.PlayerType;
import org.eclipse.collections.api.list.MutableList;

import java.io.File;

public interface LanguageLoader<T extends AbstractPlayer<T>> {
    String directoryName();
    String name();
    MutableList<PlayerType<T>> loadPlayers(MutableList<File> files);
}
