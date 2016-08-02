package KoTHComm.game.tournaments.types;

import KoTHComm.game.*;
import KoTHComm.game.tournaments.GameProvider;
import KoTHComm.game.tournaments.ProviderSupplier;
import KoTHComm.utils.iterables.CombinationIterable;
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
