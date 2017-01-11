package com.nmerrill.kothcomm.game;

import com.nmerrill.kothcomm.game.players.AbstractPlayer;
import com.nmerrill.kothcomm.game.players.Submission;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.impl.factory.Lists;

import java.util.function.Supplier;

public final class PlayerRegistry<T extends AbstractPlayer<T>> {
    private final MutableList<Submission<T>> registeredPlayers;
    public PlayerRegistry(){
        this.registeredPlayers = Lists.mutable.empty();
    }


    public void register(MutableList<Submission<T>> submissions){
        submissions.forEach(this::register);
    }

    public void register(Submission<T> submission){
        registeredPlayers.add(submission);
    }

    public void register(Class<? extends AbstractPlayer<T>> clazz, Supplier<T> supplier){
        register(clazz.getSimpleName(), supplier);
    }

    public void register(String name, Supplier<T> supplier){
        registeredPlayers.add(new Submission<>(name, supplier));
    }
//
//    public AbstractGame<T> construct(MutableCollection<T> players){
//        AbstractGame<T> game = gameFactory.get();
//        game.addPlayers(players);
//        return game;
//    }
//
//    public AbstractGame<T> constructFromType(MutableList<Submission<T>> players){
//        if (players.size() < 2){
//            throw new InvalidPlayerCountException("Too few players!");
//        }
//
//        return construct(
//                players.subList(0, gameSize())
//                        .collect(Submission::create));
//    }

    public MutableList<Submission<T>> allPlayers(){
        return registeredPlayers.clone();
    }


}
