package com.nmerrill.kothcomm.game;

import com.nmerrill.kothcomm.exceptions.InvalidPlayerCountException;
import com.nmerrill.kothcomm.game.games.AbstractGame;
import com.nmerrill.kothcomm.utils.MathTools;
import org.eclipse.collections.api.collection.MutableCollection;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.impl.factory.Lists;

import java.util.*;
import java.util.function.Supplier;

public final class GameManager<T extends AbstractPlayer<T>> {
    public final static int MIN_GAME_SIZE = 2;
    private final Supplier<AbstractGame<T>> gameFactory;
    private final MutableList<PlayerType<T>> registeredPlayers;
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
        this.registeredPlayers = Lists.mutable.empty();
        this.allowDuplicates = false;
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

    public GameManager<T> allowDuplicates() {
        this.allowDuplicates = true;
        return this;
    }

    public void register(MutableList<PlayerType<T>> playerTypes){
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
            if (allowDuplicates && playerCount != 0){
                return minPlayerCount;
            }
            throw new InvalidPlayerCountException("Need at least "+(allowDuplicates?1:minPlayerCount)+" players.  Found "+playerCount);
        }
        return Math.min(playerCount, preferredPlayerCount);
    }

    public boolean allowsDuplicates() {
        return allowDuplicates;
    }

    public AbstractGame<T> construct(MutableCollection<T> players){
        if (!MathTools.inRange(players.size(), minPlayerCount, maxPlayerCount+1)){
            throw new InvalidPlayerCountException("Game does not support "+players.size()+" players. Must be between "+minPlayerCount+" and "+maxPlayerCount);
        }
        AbstractGame<T> game = gameFactory.get();
        game.addPlayers(players);
        return game;
    }

    public AbstractGame<T> constructFromType(MutableList<PlayerType<T>> players){
        if (players.size() < 2){
            throw new InvalidPlayerCountException("Too few players!");
        }

        return construct(
                players.subList(0, gameSize())
                        .collect(PlayerType::create));
    }

    public MutableList<PlayerType<T>> allPlayers(){
        return registeredPlayers.clone().shuffleThis(this.getRandom());
    }


}
