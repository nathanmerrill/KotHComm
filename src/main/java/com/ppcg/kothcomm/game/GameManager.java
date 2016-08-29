package com.ppcg.kothcomm.game;

import com.ppcg.kothcomm.game.exceptions.InvalidPlayerCountException;
import com.ppcg.kothcomm.utils.Tools;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class GameManager<T extends AbstractPlayer<T>> {
    public final static int MIN_GAME_SIZE = 2;
    private final Supplier<AbstractGame<T>> gameFactory;
    private final List<PlayerType<T>> registeredPlayers;
    private final Random random;
    private int preferredPlayerCount;
    private int minPlayerCount;
    private int maxPlayerCount;
    private boolean allowDuplicates;
    public GameManager(Supplier<AbstractGame<T>> gameFactory, Random random){
        this.gameFactory = gameFactory;
        this.minPlayerCount = 2;
        this.maxPlayerCount = Integer.MAX_VALUE-1;
        this.preferredPlayerCount = Integer.MAX_VALUE-1;
        this.registeredPlayers = new ArrayList<>();
        this.allowDuplicates = true;
        this.random = random;
    }

    public GameManager(Supplier<AbstractGame<T>> gameFactory){
        this(gameFactory, new Random());
    }

    public Random getRandom(){
        return random;
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

    public void register(List<PlayerType<T>> playerTypes){
        playerTypes.forEach(this::register);
    }

    public void register(PlayerType<T> playerType){
        registeredPlayers.add(playerType);
    }

    public void register(Class<? extends AbstractPlayer<T>> clazz, Supplier<T> supplier){
        register(clazz.getSimpleName(), supplier);
    }

    public void register(String name, Supplier<T> supplier){
        registeredPlayers.add(new PlayerType<>(name, supplier));
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
        int playerCount = registeredPlayers.size();
        if (playerCount < minPlayerCount){
            if (allowDuplicates){
                return minPlayerCount;
            }
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
        if (playerSet.size() < 2){
            throw new InvalidPlayerCountException("Too few players!");
        }
        int gameSize = gameSize();
        List<T> players = new ArrayList<>();
        while (players.size() < gameSize){
            playerSet.stream().map(PlayerType::create).forEach(players::add);
        }
        return construct(players.subList(0, gameSize));
    }

    public List<PlayerType<T>> allPlayers(){
        return new ArrayList<>(registeredPlayers);
    }


}
