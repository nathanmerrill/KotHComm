package com.ppcg.kothcomm.game;

import com.ppcg.kothcomm.game.scoreboards.AggregateScoreboard;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

public abstract class AbstractGame<T extends AbstractPlayer<T>> {
    protected Random random;
    protected List<T> players;
    private final List<Consumer<AggregateScoreboard<T>>> listeners;
    private boolean started;
    private boolean finished;
    private AggregateScoreboard<T> scores;

    public AbstractGame(){
        this.players = new ArrayList<>();
        this.random = new Random();
        this.listeners = new ArrayList<>();
        this.started = false;
        this.finished = false;
    }

    public final void setPlayers(List<T> players){
        this.players = players;
        if (random != null) {
            for (T player : players) {
                player.setRandom(random);
            }
        }
    }

    public final void setRandom(Random random){
        this.random = random;
        if (players != null){
            for (T player: players){
                player.setRandom(random);
            }
        }
    }

    public abstract void setup();

    protected abstract boolean step();

    public abstract AggregateScoreboard<T> getScores();

    public final boolean next(){
        if (!started){
            setup();
            started = true;
        }
        if (!step()) {
            return false;
        }
        finished = true;
        scores = getScores();
        listeners.forEach(i -> i.accept(scores));
        return true;
    }

    public boolean started(){
        return started;
    }

    public boolean finished(){
        return finished;
    }


    public final AggregateScoreboard<T> run(){
        setup();
        started = true;
        //noinspection StatementWithEmptyBody
        while (step()){ }
        finished = true;
        scores = getScores();
        listeners.forEach(i -> i.accept(scores));
        return scores;
    }

    public final void onFinish(Consumer<AggregateScoreboard<T>> listener){
        if (finished){
            listener.accept(scores);
        }
        listeners.add(listener);
    }

}
