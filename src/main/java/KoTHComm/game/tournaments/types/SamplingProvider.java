package KoTHComm.game.tournaments.types;

import KoTHComm.game.*;
import KoTHComm.game.tournaments.GameProvider;
import KoTHComm.game.tournaments.ProviderSupplier;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class SamplingProvider<T extends AbstractPlayer<T>> implements ProviderSupplier<T> {

    private final GameManager<T> manager;

    public SamplingProvider(GameManager<T> manager) {
        this.manager = manager;
    }

    @Override
    public Sampling getTournament() {
        return new Sampling();
    }

    public class Sampling implements GameProvider<T> {
        final Queue<PlayerType<T>> currentPopulation;
        final List<PlayerType<T>> availablePlayers;
        final int gameSize;

        public Sampling(){
            availablePlayers = manager.allPlayers();
            currentPopulation = new LinkedList<>(availablePlayers);
            gameSize = manager.gameSize();
            repopulate(null);
        }

        private void repopulate(Collection<PlayerType<T>> ignore){
            List<PlayerType<T>> next = new ArrayList<>(availablePlayers);
            if (ignore != null) {
                Set<PlayerType<T>> ignoreSet = new HashSet<>(ignore);
                next.removeIf(ignoreSet::contains);
            }
            Collections.shuffle(next);
            currentPopulation.addAll(next);
        }

        @Override
        public AbstractGame<T> get() {
            if (currentPopulation.size() < gameSize){
                repopulate(currentPopulation);
            }
            return manager.constructFromType(
                    Stream.generate(currentPopulation::poll)
                    .limit(gameSize)
                    .collect(Collectors.toList()));
        }

    }



}
