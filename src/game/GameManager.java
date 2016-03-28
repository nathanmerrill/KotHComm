package game;

import game.exceptions.InvalidPlayerCountException;
import utils.iterables.Tools;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class GameManager {
    public final static int MIN_GAME_SIZE = 2;
    private final Function<List<Player>, Game> gameFactory;
    private final int preferredPlayerCount;
    private final int minPlayerCount;
    private final int maxPlayerCount;
    private final boolean allowDuplicates;
    public GameManager(Function<List<Player>, Game> gameFactory,
                       int preferredPlayerCount,
                       int minPlayerCount,
                       int maxPlayerCount,
                       boolean allowDuplicates){
        this.gameFactory = gameFactory;
        this.maxPlayerCount = maxPlayerCount < MIN_GAME_SIZE ? Integer.MAX_VALUE : maxPlayerCount;
        this.minPlayerCount = Tools.clamp(minPlayerCount, MIN_GAME_SIZE, this.maxPlayerCount);
        this.preferredPlayerCount = Tools.clamp(preferredPlayerCount, this.minPlayerCount, this.maxPlayerCount);
        this.allowDuplicates = allowDuplicates;
    }
    public GameManager(Function<List<Player>, Game> gameFactory,
                       int preferredPlayerCount,
                       int minPlayerCount,
                       int maxPlayerCount){
        this(gameFactory, preferredPlayerCount, minPlayerCount, maxPlayerCount, false);

    }
    public GameManager(Function<List<Player>, Game> gameFactory,
                       int minPlayerCount,
                       int maxPlayerCount,
                       boolean allowDuplicates){
        this(gameFactory, maxPlayerCount, minPlayerCount, maxPlayerCount, allowDuplicates);
    }
    public GameManager(Function<List<Player>, Game> gameFactory,
                       int minPlayerCount,
                       int maxPlayerCount){
        this(gameFactory, minPlayerCount, maxPlayerCount, false);
    }
    public GameManager(Function<List<Player>, Game> gameFactory,
                       int preferredPlayerCount,
                       boolean allowDuplicates){
        this(gameFactory, preferredPlayerCount, preferredPlayerCount, preferredPlayerCount, allowDuplicates);
    }
    public GameManager(Function<List<Player>, Game> gameFactory,
                       int preferredPlayerCount){
        this(gameFactory, preferredPlayerCount, false);
    }
    public GameManager(Function<List<Player>, Game> gameFactory, boolean allowDuplicates){
        this(gameFactory, 1, 0, allowDuplicates);
    }
    public GameManager(Function<List<Player>, Game> gameFactory){
        this(gameFactory, false);
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

    public boolean allowDuplicates() {
        return allowDuplicates;
    }

    public List<Scoreboard> runGames(Collection<List<Player>> playerSets){
        return playerSets.stream().map(this::runGame).collect(Collectors.toList());
    }

    public Scoreboard runGame(List<Player> playerSet){
        if (!Tools.inBounds(playerSet.size(), minPlayerCount, maxPlayerCount+1)){
            throw new InvalidPlayerCountException();
        }
        return gameFactory.apply(Tools.apply(new ArrayList<>(playerSet), Collections::shuffle)).run();
    }
}
