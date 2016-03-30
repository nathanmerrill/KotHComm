package game;

import java.util.function.Supplier;

/**
 * Represents a player within a game.  Depending on the game, there may be multiple instances
 * of your player in the same game, and there will definitely be multiple instances of your player
 * across the tournament.
 */
public abstract class Player {
    private final PlayerType type;

    /**
     * The alternative, default constructor.  Uses reflection to figure out your type.
     */
    public Player(){
        this.type = new PlayerType(this.getClass());
    }

    /**
     * This is the preferable constructor to override.  Use as follows:
     * super("YourName", this::YourClassName)
     *
     * @param name The name your player will be known as
     * @param constructor A function that returns an instance of your player
     */
    public Player(String name, Supplier<? extends Player> constructor){
        this.type = new PlayerType(name, constructor);
    }

    public final PlayerType getType(){
        return type;
    }
}
