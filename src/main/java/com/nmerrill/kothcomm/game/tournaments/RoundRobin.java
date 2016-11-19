package com.nmerrill.kothcomm.game.tournaments;

import com.nmerrill.kothcomm.exceptions.InvalidPlayerCountException;
import com.nmerrill.kothcomm.game.AbstractPlayer;
import com.nmerrill.kothcomm.game.GameManager;
import com.nmerrill.kothcomm.game.PlayerType;
import com.nmerrill.kothcomm.game.games.AbstractGame;
import com.nmerrill.kothcomm.game.scoring.Scoreboard;
import com.nmerrill.kothcomm.utils.iterables.Itertools;
import org.eclipse.collections.api.list.MutableList;

import java.util.Iterator;


public final class RoundRobin<T extends AbstractPlayer<T>> implements Tournament<T> {
    private final GameManager<T> manager;
    private Iterator<MutableList<PlayerType<T>>> iterator;

    public RoundRobin(GameManager<T> manager){
        if (manager.gameSize() > manager.allPlayers().size()){
            throw new InvalidPlayerCountException("RoundRobin does not support duplicate players");
        }
        this.manager = manager;
        nextIterator();
    }

    private void nextIterator(){
        iterator = Itertools.combination(manager.allPlayers(), manager.gameSize()).iterator();
    }

    @Override
    public AbstractGame<T> get(Scoreboard<PlayerType<T>> scoreboard) {
        if (!iterator.hasNext()){
            nextIterator();
        }
        return manager.constructFromType(iterator.next());
    }

}
