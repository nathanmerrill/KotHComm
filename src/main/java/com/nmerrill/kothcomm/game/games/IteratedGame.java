package com.nmerrill.kothcomm.game.games;

import com.nmerrill.kothcomm.game.AbstractPlayer;

public abstract class IteratedGame<T extends AbstractPlayer<T>> extends AbstractGame<T> {
    private int iterations;
    public IteratedGame(int iterations){
        this.iterations = iterations;
    }
    @Override
    protected boolean step() {
        iterations--;
        iterate();
        return iterations >= 0;
    }
    protected abstract void iterate();
}
