package game.tournaments;

import game.*;
import game.exceptions.InvalidPlayerCountException;
import utils.iterables.CombinationIterable;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class RoundRobinTournament<T> implements Tournament<T>{


    private final Supplier<Scoreboard<T>> scoreboardSupplier;
    private final GameManager<T> manager;
    public RoundRobinTournament(GameManager<T> manager, Supplier<Scoreboard<T>> scoreboardSupplier) {
        this.manager = manager;
        this.scoreboardSupplier = scoreboardSupplier;
    }

    @Override
    public RoundRobinTournamentIterator iterator() {
        return new RoundRobinTournamentIterator();
    }
    public class RoundRobinTournamentIterator implements TournamentIterator<T>{
        final Scoreboard<T> scoreboard;
        final Iterator<List<PlayerType<T>>> iterator;

        public RoundRobinTournamentIterator(){
            scoreboard = scoreboardSupplier.get();
            List<PlayerType<T>> players = manager.getDirectory().allPlayers();
            if (players.size() < manager.minPlayerCount()){
                throw new InvalidPlayerCountException("Need more players");
            }
            int gameSize = Math.min(players.size(), manager.preferredPlayerCount());
            iterator = new CombinationIterable<>(manager.getDirectory().allPlayers(), gameSize).iterator();
        }

        @Override
        public Game<T> next() {
            Game<T> game = manager.construct(iterator.next());
            game.onFinish(scoreboard::addScores);
            return game;
        }

        @Override
        public boolean hasNext() {
            return iterator.hasNext();
        }

        @Override
        public Scoreboard<T> currentRankings() {
            return scoreboard;
        }

    }

}
