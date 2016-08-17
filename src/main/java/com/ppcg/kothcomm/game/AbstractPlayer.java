package com.ppcg.kothcomm.game;

import java.util.Random;

public abstract class AbstractPlayer<T extends AbstractPlayer<T>> {

    private PlayerType<T> type;
    private Random random;

    public final void setType(PlayerType<T> type){
        this.type = type;
    }

    public final PlayerType<T> getType(){
        return type;
    }

    public final String getName(){
        return type.getName();
    }

    public final void setRandom(Random random){
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
