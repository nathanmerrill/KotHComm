package game.tournaments;

import game.*;
import game.exceptions.InvalidPlayerCountException;
import utils.iterables.Tools;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;


public class SamplingTournament<T> implements Tournament<T> {

    private final Supplier<Scoreboard<T>> scoreboardSupplier;
    private final GameManager<T> manager;

    public SamplingTournament(GameManager<T> manager, Supplier<Scoreboard<T>> scoreboardSupplier) {
        this.manager = manager;
        this.scoreboardSupplier = scoreboardSupplier;
    }


    @Override
    public SamplingTournamentIterator iterator() {
        return new SamplingTournamentIterator();
    }

    public class SamplingTournamentIterator implements TournamentIterator<T>{
        final Queue<PlayerType<T>> currentPopulation;
        final Scoreboard<T> scoreboard;
        final List<PlayerType<T>> availablePlayers;
        final int gameSize;

        public SamplingTournamentIterator(){
            scoreboard = scoreboardSupplier.get();
            availablePlayers = manager.getDirectory().allPlayers();
            currentPopulation = new LinkedList<>(availablePlayers);
            if (currentPopulation.size() < manager.minPlayerCount()){
                throw new InvalidPlayerCountException("Need more players");
            }
            gameSize = Math.min(availablePlayers.size(), manager.preferredPlayerCount());
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
        public Game<T> next() {
            if (currentPopulation.size() < gameSize){
                repopulate(currentPopulation);
            }
            Game<T> game = manager.construct(
                    Stream.generate(currentPopulation::poll)
                    .limit(gameSize)
                    .collect(Collectors.toList()));
            game.onFinish(scoreboard::addScores);
            return game;
        }

        @Override
        public boolean hasNext() {
            return !currentPopulation.isEmpty();
        }

        @Override
        public Scoreboard<T> currentRankings() {
            return scoreboard;
        }

    }



}
