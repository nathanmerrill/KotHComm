package com.nmerrill.kothcomm.game.games;

import com.nmerrill.kothcomm.game.players.AbstractPlayer;

public abstract class IteratedGame<T extends AbstractPlayer<T>> extends AbstractGame<T> {
    private int iterations;
    public IteratedGame(int iterations){
        this.iterations = iterations;
    }
    @Override
    protected void step() {
        iterations--;
    }

    @Override
    public boolean finished() {
        return iterations >= 0;
    }
}
