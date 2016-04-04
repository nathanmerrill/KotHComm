package game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class Game<T> {
    protected Random random;
    protected List<T> players;
    protected Directory<T> directory;
    public Game(){
        this.players = new ArrayList<>();
        this.random = new Random();
    }

    public void setPlayers(List<T> players){
        this.players = players;
    }

    public void setDirectory(Directory<T> directory){
        this.directory = directory;
    }

    public void setRandom(Random random){
        this.random = random;
    }

    public abstract Scoreboard<T> run();

}
