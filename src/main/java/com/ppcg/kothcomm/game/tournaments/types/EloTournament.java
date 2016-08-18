package com.ppcg.kothcomm.game.tournaments.types;

import com.ppcg.kothcomm.game.*;
import com.ppcg.kothcomm.game.exceptions.InvalidPlayerCountException;
import com.ppcg.kothcomm.game.tournaments.*;
import com.ppcg.kothcomm.utils.Pair;
import com.ppcg.kothcomm.utils.iterables.PermutationIterable;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class EloTournament<T extends AbstractPlayer<T>> implements TournamentSupplier<T> {

    public static final double INITIAL_RATING = 1000;
    public static final double DEFAULT_K_FACTOR = 32;

    private final double kFactor;
    private final GameManager<T> manager;
    private final int gameSize;

    public EloTournament(GameManager<T> manager, double kFactor){
        this.manager = manager;
        this.kFactor = kFactor;
        gameSize = manager.gameSize();
    }

    public EloTournament(GameManager<T> gameManager){
        this(gameManager, DEFAULT_K_FACTOR /gameManager.gameSize());
    }

    @Override
    public Elo get() {
        return new Elo();
    }

    public class Elo implements Tournament<T> {
        private final Queue<PlayerType<T>> focuses;
        private final Map<PlayerType<T>, Double> ratings;

        public Elo() {
            ratings = manager.allPlayers().stream()
                    .collect(Collectors.toMap(Function.identity(), i -> INITIAL_RATING));
            focuses = new LinkedList<>();
            if (ratings.size() < gameSize){
                throw new InvalidPlayerCountException("Elo Tournament only supports games with unique players: more players needed");
            }
        }

        @Override
        public AbstractGame<T> get() {
            if (focuses.isEmpty()) {
                List<PlayerType<T>> next = new ArrayList<>(ratings.keySet());
                Collections.shuffle(next);
                focuses.addAll(next);
            }
            List<PlayerType<T>> players = rangeAround(focuses.poll());
            return manager.constructFromType(players);
        }

        @Override
        public Scoreboard<PlayerType<T>> getRankings() {
            Scoreboard<PlayerType<T>> scoreboard = new Scoreboard<>();
            ratings.forEach(scoreboard::addScore);
            return scoreboard;
        }

        public void scoreGame(AbstractGame<T> game){
            game.onFinish(this::updateScores);
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
            List<Pair<PlayerType<T>, Double>> aggregates = scores.stream().map(Pair.fromValue(
                            AbstractPlayer::getType,
                            scores::getScore))
                    .collect(Collectors.toList());

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
            while (maxIndex - minIndex + 1 < gameSize) {
                if (minIndex == 0) {
                    maxIndex = gameSize - 1;
                    break;
                }
                if (maxIndex == sorted.size() - 1) {
                    minIndex = sorted.size() - gameSize;
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
