package game;

import game.exceptions.InvalidPlayerCountException;
import utils.iterables.Tools;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
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
        this.maxPlayerCount = -1;
        this.preferredPlayerCount = -1;
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
            throw new InvalidPlayerCountException();
        }
        this.maxPlayerCount = maxPlayerCount;
        if (this.preferredPlayerCount > this.maxPlayerCount){
            this.preferredPlayerCount = this.maxPlayerCount;
        }
        return this;
    }

    public GameManager<T> minPlayerCount(int minPlayerCount) {
        if (minPlayerCount < 2 || minPlayerCount > maxPlayerCount){
            throw new InvalidPlayerCountException();
        }
        this.minPlayerCount = minPlayerCount;
        if (this.preferredPlayerCount < this.minPlayerCount){
            this.preferredPlayerCount = this.minPlayerCount;
        }
        return this;
    }


    public GameManager<T> preferredPlayerCount(int preferredPlayerCount) {
        if (preferredPlayerCount > maxPlayerCount || preferredPlayerCount < minPlayerCount){
            throw new InvalidPlayerCountException();
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

    public List<Scoreboard<T>> runGames(Collection<List<PlayerType<T>>> playerSets){
        return playerSets.stream().map(this::runGame).collect(Collectors.toList());
    }

    public Scoreboard<T> runGame(List<PlayerType<T>> playerSet){
        if (!Tools.inBounds(playerSet.size(), minPlayerCount, maxPlayerCount+1)){
            throw new InvalidPlayerCountException();
        }
        List<T> players = directory.instantiate(playerSet);
        Collections.shuffle(players);
        Game<T> game = gameFactory.get();
        game.setPlayers(players);
        game.setDirectory(directory);
        return game.run();
    }

}
