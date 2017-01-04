package com.nmerrill.kothcomm.game.games;

import com.nmerrill.kothcomm.game.players.AbstractPlayer;
import com.nmerrill.kothcomm.utils.ActionQueue;

public abstract class ActionQueueGame<T extends AbstractPlayer<T>> extends AbstractGame<T> {
    protected ActionQueue queue;
    public ActionQueueGame(ActionQueue queue){
        this.queue = queue;
    }
    public ActionQueueGame(){
        this(new ActionQueue());
    }
    @Override
    protected void step() {
        queue.tick();
    }

    @Override
    public boolean finished(){
        return queue.isEmpty();
    }
}
