package com.nmerrill.kothcomm.communication.languages.other;

import com.nmerrill.kothcomm.game.players.Submission;
import com.nmerrill.kothcomm.game.players.AbstractPlayer;
import com.nmerrill.kothcomm.communication.languages.Language;
import org.eclipse.collections.api.list.MutableList;

import java.io.File;
import java.util.function.Function;

public final class OtherLoader<T extends AbstractPlayer<T>> implements Language<T> {
    private final Function<PipeCommunicator, T> pipeBot;
    public OtherLoader(Function<PipeCommunicator, T> pipeBot){
        this.pipeBot = pipeBot;
    }

    @Override
    public boolean fileBased() {
        return true;
    }

    @Override
    public String directoryName() {
        return "other";
    }

    @Override
    public String name() {
        return "Other language";
    }

    @Override
    public MutableList<Submission<T>> loadPlayers(MutableList<File> files) {
        return files.collect(f -> new Submission<>(f.getName(), () -> pipeBot.apply(new PipeCommunicator(f))));
    }
}
