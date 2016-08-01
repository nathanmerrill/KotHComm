package game;

import game.exceptions.InvalidPlayerCountException;
import utils.Tools;

import java.util.*;
import java.util.function.Supplier;

public class GameManager<T extends GamePlayer> {
    public final static int MIN_GAME_SIZE = 2;
    private final Supplier<AbstractGame<T>> gameFactory;
    private final Directory<T> directory;
    private final Random random;
    private int preferredPlayerCount;
    private int minPlayerCount;
    private int maxPlayerCount;
    private boolean allowDuplicates;
    public GameManager(Supplier<AbstractGame<T>> gameFactory, Directory<T> directory, Random random){
        this.gameFactory = gameFactory;
        this.minPlayerCount = 2;
        this.maxPlayerCount = Integer.MAX_VALUE-1;
        this.preferredPlayerCount = Integer.MAX_VALUE-1;
        this.directory = directory;
        this.random = random;
    }

    public GameManager(Supplier<AbstractGame<T>> gameFactory, Directory<T> directory){
        this(gameFactory, directory, new Random());
    }

    public Random getRandom(){
        return random;
    }

    public Directory<T> getDirectory(){
        return directory;
    }

    public GameManager<T> playerCount(int playerCount){
        if (minPlayerCount < 2){
            throw new InvalidPlayerCountException("Too few players");
        }
        this.minPlayerCount = playerCount;
        this.maxPlayerCount = playerCount;
        this.preferredPlayerCount = playerCount;
        return this;
    }

    public GameManager<T> maxPlayerCount(int maxPlayerCount) {
        if (maxPlayerCount == -1){
            maxPlayerCount = Integer.MAX_VALUE;
        }
        if (maxPlayerCount < minPlayerCount){
            throw new InvalidPlayerCountException("Maximum players below minimum");
        }
        this.maxPlayerCount = maxPlayerCount;
        if (this.preferredPlayerCount > this.maxPlayerCount){
            this.preferredPlayerCount = this.maxPlayerCount;
        }
        return this;
    }

    public GameManager<T> minPlayerCount(int minPlayerCount) {
        if (minPlayerCount < 2 || minPlayerCount > maxPlayerCount){
            throw new InvalidPlayerCountException("Minimum players above maximum");
        }
        this.minPlayerCount = minPlayerCount;
        if (this.preferredPlayerCount < this.minPlayerCount){
            this.preferredPlayerCount = this.minPlayerCount;
        }
        return this;
    }


    public GameManager<T> preferredPlayerCount(int preferredPlayerCount) {
        if (preferredPlayerCount > maxPlayerCount || preferredPlayerCount < minPlayerCount){
            throw new InvalidPlayerCountException("Preferred player count outside of maximum and minimum");
        }
        this.preferredPlayerCount = preferredPlayerCount;
        return this;
    }

    public GameManager<T> allowDuplicates(boolean allowDuplicates) {
        this.allowDuplicates = allowDuplicates;
        return this;
    }

    public int maxPlayerCount() {
        return maxPlayerCount;
    }

    public int minPlayerCount() {
        return minPlayerCount;
    }

    public int preferredPlayerCount() {
        return preferredPlayerCount;
    }

    public int gameSize(){
        int playerCount = directory.allPlayers().size();
        if (playerCount < minPlayerCount){
            throw new InvalidPlayerCountException("Need more players");
        }
        return Math.min(playerCount, preferredPlayerCount);
    }

    public boolean allowsDuplicates() {
        return allowDuplicates;
    }

    public AbstractGame<T> construct(Collection<T> players){
        if (!Tools.inRange(players.size(), minPlayerCount, maxPlayerCount+1)){
            throw new InvalidPlayerCountException("Game does not support "+players.size()+" players. Must be between "+minPlayerCount+" and "+maxPlayerCount);
        }
        AbstractGame<T> game = gameFactory.get();
        game.setPlayers(new ArrayList<>(players));
        return game;
    }

    public AbstractGame<T> constructFromType(Collection<PlayerType<T>> playerSet){
        return construct(directory.instantiate(playerSet));
    }


}
