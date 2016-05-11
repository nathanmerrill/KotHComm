package game.tournaments;

import game.*;
import game.exceptions.InvalidPlayerCountException;
import utils.iterables.CombinationIterable;
import utils.iterables.Pair;
import utils.iterables.PermutationIterable;
import utils.iterables.Tools;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class EloTournament<T> implements Tournament<T>{

    public static final double INITIAL_RATING = 1000;
    public static final double DEFAULT_KFACTOR = 32;

    private final double kFactor;
    private final GameManager<T> manager;
    private final int numGames;

    public EloTournament(GameManager<T> manager, int numGames, double kFactor){
        this.manager = manager;
        this.numGames = numGames;
        this.kFactor = kFactor;
    }

    public EloTournament(GameManager<T> gameManager, int numGames){
        this(gameManager, numGames, DEFAULT_KFACTOR/gameManager.getDirectory().playerCount());
    }



    @Override
    public EloTournamentIterator iterator() {
        return new EloTournamentIterator();
    }
    private class EloTournamentIterator implements TournamentIterator<T> {
        private final List<PlayerType<T>> players;
        private Queue<PlayerType<T>> focuses;
        private final Map<PlayerType<T>, Double> ratings;
        private int remainingGames;

        public EloTournamentIterator() {
            ratings = manager.getDirectory().allPlayers().stream()
                    .collect(Collectors.toMap(Function.identity(), i -> INITIAL_RATING));
            remainingGames = numGames;
            players = manager.getDirectory().allPlayers();
            if (players.size() < manager.minPlayerCount()) {
                throw new InvalidPlayerCountException("Need more players");
            }
            focuses = new LinkedList<>();

            int gameSize = Math.min(players.size(), manager.preferredPlayerCount());


        }

        @Override
        public Game<T> next() {
            remainingGames--;
            if (focuses.isEmpty()) {
                List<PlayerType<T>> next = new ArrayList<>(ratings.keySet());
                Collections.shuffle(next);
                focuses.addAll(next);
            }
            List<PlayerType<T>> players = rangeAround(focuses.poll());
            Game<T> game = manager.construct(players);
            game.onFinish(this::updateScores);
            return game;
        }

        @Override
        public boolean hasNext() {
            return remainingGames > 0;
        }

        @Override
        public Scoreboard<T> currentRankings() {
            Scoreboard<T> scoreboard = new Scoreboard<>();
            ratings.forEach(scoreboard::addScore);
            return scoreboard;
        }

        private void updateScores(Scoreboard<T> scoreboard) {
            Map<PlayerType<T>, Double> scores = mapScores(scoreboard);
            for (List<PlayerType<T>> pairing : new PermutationIterable<>(scores.keySet())) {
                Pair<Double, Double> expected = expectedScore(new Pair<>(pairing.get(0), pairing.get(1)));
                BiConsumer<PlayerType<T>, Double> update = (p, d) -> ratings.put(p, ratings.get(p) + kFactor * (scores.get(p) - d));
                update.accept(pairing.get(0), expected.first());
                update.accept(pairing.get(1), expected.second());
            }
        }

        private Pair<Double, Double> expectedScore(Pair<PlayerType<T>, PlayerType<T>> matchup) {
            double adj1 = Math.pow(10, ratings.get(matchup.first()) / 400);
            double adj2 = Math.pow(10, ratings.get(matchup.second()) / 400);
            double expected1 = adj1 / (adj1 + adj2);
            double expected2 = 1 - expected1;
            return new Pair<>(expected1, expected2);
        }

        private Map<PlayerType<T>, Double> mapScores(Scoreboard<T> scores) {
            List<Pair<PlayerType<T>, Double>> aggregates = scores.playerAggregates();
            double min = aggregates.get(0).second();
            double max = aggregates.get(aggregates.size() - 1).second();
            double range = max - min;
            if (range == 0) {
                return aggregates.stream().collect(Collectors.toMap(Pair::first, i -> .5));
            }
            return aggregates.stream().collect(Collectors.toMap(Pair::first, i -> 1 - (i.second() - min) / range));
        }


        private List<PlayerType<T>> rangeAround(PlayerType<T> player) {
            List<PlayerType<T>> sorted = ratings.keySet().stream()
                    .sorted(Comparator.comparingDouble(ratings::get))
                    .collect(Collectors.toList());
            int minIndex = sorted.indexOf(player);
            int maxIndex = minIndex;
            while (maxIndex - minIndex + 1 < manager.preferredPlayerCount()) {
                if (minIndex == 0) {
                    maxIndex = manager.preferredPlayerCount() - 1;
                    break;
                }
                if (maxIndex == sorted.size() - 1) {
                    minIndex = sorted.size() - manager.preferredPlayerCount();
                    break;
                }
                double minDiff = ratings.get(sorted.get(minIndex)) - ratings.get(sorted.get(minIndex - 1));
                double maxDiff = ratings.get(sorted.get(maxIndex + 1)) - ratings.get(sorted.get(maxIndex));
                if (maxDiff < minDiff) {
                    maxIndex++;
                } else {
                    minIndex--;
                }
            }
            return sorted.subList(minIndex, maxIndex + 1);
        }
    }
}
