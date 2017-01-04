package com.nmerrill.kothcomm.game;

import com.nmerrill.kothcomm.game.players.AbstractPlayer;
import com.nmerrill.kothcomm.game.players.PlayerType;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.impl.factory.Lists;

import java.util.function.Supplier;

public final class PlayerRegistry<T extends AbstractPlayer<T>> {
    private final MutableList<PlayerType<T>> registeredPlayers;
    public PlayerRegistry(){
        this.registeredPlayers = Lists.mutable.empty();
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
//
//    public AbstractGame<T> construct(MutableCollection<T> players){
//        AbstractGame<T> game = gameFactory.get();
//        game.addPlayers(players);
//        return game;
//    }
//
//    public AbstractGame<T> constructFromType(MutableList<PlayerType<T>> players){
//        if (players.size() < 2){
//            throw new InvalidPlayerCountException("Too few players!");
//        }
//
//        return construct(
//                players.subList(0, gameSize())
//                        .collect(PlayerType::create));
//    }

    public MutableList<PlayerType<T>> allPlayers(){
        return registeredPlayers.clone();
    }


}
