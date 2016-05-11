package game;

import game.exceptions.InvalidPlayerCountException;
import utils.iterables.Tools;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class GameManager<T> {
    public final static int MIN_GAME_SIZE = 2;
    private final Supplier<Game<T>> gameFactory;
    private final Directory<T> directory;
    private int preferredPlayerCount;
    private int minPlayerCount;
    private int maxPlayerCount;
    private boolean allowDuplicates;
    public GameManager(Supplier<Game<T>> gameFactory, Directory<T> directory){
        this.gameFactory = gameFactory;
        this.minPlayerCount = 2;
        this.maxPlayerCount = Integer.MAX_VALUE;
        this.preferredPlayerCount = Integer.MAX_VALUE;
        this.directory = directory;
    }

    public Directory<T> getDirectory(){
        return directory;
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

    public boolean allowsDuplicates() {
        return allowDuplicates;
    }

    public Game<T> construct(List<PlayerType<T>> playerSet){
        if (!Tools.inBounds(playerSet.size(), minPlayerCount, maxPlayerCount+1)){
            throw new InvalidPlayerCountException("Game does not support "+playerSet.size()+" players");
        }
        List<T> players = directory.instantiate(playerSet);
        Collections.shuffle(players);
        Game<T> game = gameFactory.get();
        game.setPlayers(players);
        game.setDirectory(directory);
        return game;
    }


}
