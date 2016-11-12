package com.nmerrill.kothcomm.game.games;

import com.nmerrill.kothcomm.game.AbstractPlayer;
import com.nmerrill.kothcomm.utils.ActionQueue;

public abstract class MaxActionQueueGame<T extends AbstractPlayer<T>> extends ActionQueueGame<T> {
    private int stepsRemaining;
    public MaxActionQueueGame(ActionQueue queue, int maximum){
        super(queue);
        this.stepsRemaining = maximum;
    }
    public MaxActionQueueGame(int maximum){
        this(new ActionQueue(), maximum);
    }

    @Override
    protected boolean step() {
        stepsRemaining--;
        return super.step() && stepsRemaining > 0;
    }

    protected boolean isFinished(){
        return super.isFinished() || stepsRemaining <= 0;
    }
}
