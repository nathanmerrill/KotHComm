package com.nmerrill.kothcomm.communication.languages;

import com.nmerrill.kothcomm.game.PlayerType;
import com.nmerrill.kothcomm.game.AbstractPlayer;
import org.eclipse.collections.api.list.MutableList;

import java.io.File;

public interface Language<T extends AbstractPlayer<T>> {
    String directoryName();
    String name();
    MutableList<PlayerType<T>> loadPlayers(MutableList<File> files);
}
