package com.nmerrill.kothcomm.game.tournaments;

import com.nmerrill.kothcomm.exceptions.InvalidPlayerCountException;
import com.nmerrill.kothcomm.game.scoring.Scoreboard;
import com.nmerrill.kothcomm.utils.iterables.Itertools;
import org.eclipse.collections.api.list.MutableList;

import java.util.Iterator;
import java.util.Random;


public final class RoundRobin<T> implements Tournament<T> {

    private final MutableList<T> players;
    private final Random random;
    private Iterator<MutableList<T>> iterator;

    public RoundRobin(MutableList<T> players, Random random){
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
    public MutableList<T> get(int size, Scoreboard<T> scoreboard) {
        //TODO:  support changing game size
        if (iterator == null || !iterator.hasNext()){
            nextIterator(size);
        }
        return iterator.next();
    }

}
