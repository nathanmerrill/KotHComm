package com.ppcg.kothcomm.game;

public abstract class RepeatedGame<T extends AbstractPlayer<T>> extends AbstractGame<T> {
    private int iterations;
    public RepeatedGame(int iterations){
        this.iterations = iterations;
    }
    @Override
    protected boolean step() {
        iterations--;
        nextStep();
        return iterations <= 0;
    }
    protected abstract void nextStep();
}
