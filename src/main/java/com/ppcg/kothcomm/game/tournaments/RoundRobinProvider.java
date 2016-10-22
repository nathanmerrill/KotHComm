package com.ppcg.kothcomm.game.tournaments;

import com.ppcg.kothcomm.game.*;
import com.ppcg.kothcomm.game.scoring.Scoreboard;
import com.ppcg.kothcomm.utils.iterables.CombinationIterable;
import org.eclipse.collections.api.list.MutableList;

import java.util.*;
import java.util.function.Supplier;


public class RoundRobinProvider<T extends AbstractPlayer<T>> implements Supplier<GameProvider<T>> {


    private final GameManager<T> manager;
    public RoundRobinProvider(GameManager<T> manager) {
        this.manager = manager;
    }

    @Override
    public RoundRobin get() {
        return new RoundRobin();
    }

    public class RoundRobin implements GameProvider<T> {
        final Iterable<MutableList<PlayerType<T>>> iterable;
        Iterator<MutableList<PlayerType<T>>> iterator;

        public RoundRobin(){
            iterable = new CombinationIterable<>(manager.allPlayers(), manager.gameSize());
            iterator = iterable.iterator();
        }

        @Override
        public AbstractGame<T> get(Scoreboard<PlayerType<T>> scoreboard) {
            if (!iterator.hasNext()){
                iterator = iterable.iterator();
            }
            return manager.constructFromType(iterator.next());
        }

    }

}