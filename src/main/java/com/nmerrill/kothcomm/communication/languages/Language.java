package com.nmerrill.kothcomm.communication.languages;

import com.nmerrill.kothcomm.game.PlayerType;
import com.nmerrill.kothcomm.game.AbstractPlayer;
import org.eclipse.collections.api.list.MutableList;

import java.io.File;

/**
 * A Language represents a language that can be loaded.
 * If a language is file based, then it should return its preferred directory name
 * @param <T>
 */
public interface Language<T extends AbstractPlayer<T>> {
    /**
     * If the language is not file based, the return value is undefined
     * @return the preferred directory for the language.
     */
    String directoryName();

    /**
     * @return A human friendly name for the language
     */
    String name();

    /**
     * @return True if the language is file based, False otherwise
     */
    boolean fileBased();

    /**
     * @param files Files that should be loaded.  Needs to be passed if the language is file based
     * @return A list of players
     */
    MutableList<PlayerType<T>> loadPlayers(MutableList<File> files);
}
