package game.tournaments.types;

import game.*;
import game.tournaments.GameProvider;
import game.tournaments.ProviderSupplier;
import utils.iterables.CombinationIterable;
import java.util.*;


public class RoundRobinProvider<T extends GamePlayer> implements ProviderSupplier<T> {


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
            iterable = new CombinationIterable<>(manager.getDirectory().allPlayers(), manager.gameSize());
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
