package com.ppcg.kothcomm.game.tournaments.types;

import com.ppcg.kothcomm.game.*;
import com.ppcg.kothcomm.game.tournaments.GameProvider;
import com.ppcg.kothcomm.game.tournaments.ProviderSupplier;
import com.ppcg.kothcomm.utils.iterables.CombinationIterable;
import java.util.*;


public class RoundRobinProvider<T extends AbstractPlayer<T>> implements ProviderSupplier<T> {


    private final GameManager<T> manager;
    public RoundRobinProvider(GameManager<T> manager) {
        this.manager = manager;
    }

    @Override
    public RoundRobin getTournament() {
        return new RoundRobin();
    }

    public class RoundRobin implements GameProvider<T> {
        final Iterable<List<PlayerType<T>>> iterable;
        Iterator<List<PlayerType<T>>> iterator;

        public RoundRobin(){
            iterable = new CombinationIterable<>(manager.allPlayers(), manager.gameSize());
            iterator = iterable.iterator();
        }

        @Override
        public AbstractGame<T> get() {
            if (!iterator.hasNext()){
                iterator = iterable.iterator();
            }
            return manager.constructFromType(iterator.next());
        }

    }

}
