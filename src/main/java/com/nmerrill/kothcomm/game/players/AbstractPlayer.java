package com.nmerrill.kothcomm.game.players;

import java.util.Random;

public abstract class AbstractPlayer<T extends AbstractPlayer<T>> {

    private Submission<T> type;
    private Random random;

    public final void setType(Submission<T> type){
        this.type = type;
    }

    public final Submission<T> getType(){
        return type;
    }

    public final String getName(){
        return type.getName();
    }

    public void setRandom(Random random){
        this.random = random;
    }

    public final Random getRandom(){
        return this.random;
    }

    @Override
    public final String toString() {
        return getName();
    }
}
