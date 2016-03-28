package game;

import java.util.Random;

public abstract class Game {
    private final Random random;
    public Game(Random random){
        this.random = random;
    }
    public abstract Scoreboard run();
}
