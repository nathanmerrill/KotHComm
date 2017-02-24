package com.nmerrill.kothcomm.game.games;

import com.nmerrill.kothcomm.game.players.AbstractPlayer;

public abstract class IteratedGame<T extends AbstractPlayer<T>> extends AbstractGame<T> {
    private int iterations;

    public abstract int getNumIterations();

    public int getRemainingIterations() {
        return this.iterations;
    }

    @Override
    protected void setup() {
        this.iterations = getNumIterations();
    }

    @Override
    protected void step() {
        iterations--;
    }

    @Override
    public boolean finished() {
        return iterations <= 0;
    }
}
