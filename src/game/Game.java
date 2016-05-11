package game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

public abstract class Game<T> {
    protected Random random;
    protected List<T> players;
    protected Directory<T> directory;
    private final List<Consumer<Scoreboard<T>>> listeners;
    private boolean started;
    public Game(){
        this.players = new ArrayList<>();
        this.random = new Random();
        this.listeners = new ArrayList<>();
        this.started = false;
    }

    public final void setPlayers(List<T> players){
        this.players = players;
    }

    public final void setDirectory(Directory<T> directory){
        this.directory = directory;
    }

    public final void setRandom(Random random){
        this.random = random;
    }

    public abstract void setup();

    protected abstract boolean step();

    public abstract Scoreboard<T> getScores();

    public final boolean next(){
        if (!started){
            setup();
            started = true;
        }
        if (!step()){
            return false;
        }
        Scoreboard<T> scores = getScores();
        listeners.forEach(i -> i.accept(scores));
        return true;
    }

    public final Scoreboard<T> run(){
        setup();
        while (step()){
            //Do nothing
        }
        Scoreboard<T> scores = getScores();
        listeners.forEach(i -> i.accept(scores));
        return scores;
    }

    public final void onFinish(Consumer<Scoreboard<T>> listener){
        listeners.add(listener);
    }

}
