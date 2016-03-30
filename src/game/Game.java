package game;

import java.util.List;
import java.util.Random;

public abstract class Game {
    protected final Random random;
    protected final List<Player> players;
    public Game(Random random, List<Player> players){
        this.random = random;
        this.players = players;
    }
    public abstract Scoreboard run();
}
