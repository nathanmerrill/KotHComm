package com.nmerrill.kothcomm.game.tournaments;

import com.nmerrill.kothcomm.exceptions.InvalidPlayerCountException;
import com.nmerrill.kothcomm.game.AbstractPlayer;
import com.nmerrill.kothcomm.game.PlayerType;
import com.nmerrill.kothcomm.game.scoring.Scoreboard;
import com.nmerrill.kothcomm.utils.iterables.Itertools;
import org.eclipse.collections.api.list.MutableList;

import java.util.Iterator;
import java.util.Random;


public final class RoundRobin<T extends AbstractPlayer<T>> implements Tournament<PlayerType<T>> {

    private final MutableList<PlayerType<T>> players;
    private final Random random;
    private Iterator<MutableList<PlayerType<T>>> iterator;

    public RoundRobin(MutableList<PlayerType<T>> players, Random random){
        if (players.size() < 2){
            throw new InvalidPlayerCountException("RoundRobin requires at least 2 players");
        }
        this.players = players;
        this.random = random;
    }

    private void nextIterator(int count){
        iterator = Itertools.combination(players, count).iterator();
    }

    @Override
    public MutableList<PlayerType<T>> get(int size, Scoreboard<PlayerType<T>> scoreboard) {
        //TODO:  support changing game size
        if (iterator == null || !iterator.hasNext()){
            nextIterator(size);
        }
        return iterator.next();
    }

}
