package com.ppcg.kothcomm.game.scoreboards;

import com.ppcg.kothcomm.utils.Pair;
import com.ppcg.kothcomm.utils.iterables.PermutationIterable;

import java.util.*;
import java.util.stream.Collectors;


public class EloScoreboard<T> extends Scoreboard<T> {

    private final Map<T, Double> ratings;
    private final double kFactor;

    public static final double DEFAULT_K_FACTOR = 32;
    public static final double DEFAULT_SCORE = 1000;

    public EloScoreboard(double kFactor){
        this.kFactor = kFactor;
        ratings = new HashMap<>();
    }

    public EloScoreboard() {
        this(DEFAULT_K_FACTOR);
    }

    @Override
    protected void update(Map<T, Double> scores) {
        Map<T, Double> mapped = mapScores(scores);
        scores.forEach((i, j) -> ratings.putIfAbsent(i, DEFAULT_SCORE));
        List<Pair<T, Double>> updates = new ArrayList<>();
        for (List<T> pairing : new PermutationIterable<>(mapped.keySet())) {
            Pair<Double, Double> expected = expectedScore(new Pair<>(pairing.get(0), pairing.get(1)));
            updates.add(new Pair<>(pairing.get(0), expected.first()));
            updates.add(new Pair<>(pairing.get(1), expected.second()));
        }
        updates.forEach(p -> updateScore(p.first(), p.second(), mapped.get(p.first())));
    }

    private void updateScore(T player, double score, double update) {
        ratings.put(player, ratings.get(player) + kFactor * (update - score));
    }

    private Pair<Double, Double> expectedScore(Pair<T, T> matchup) {
        double adj1 = Math.pow(10, ratings.get(matchup.first()) / 400);
        double adj2 = Math.pow(10, ratings.get(matchup.second()) / 400);
        double expected1 = adj1 / (adj1 + adj2);
        double expected2 = 1 - expected1;
        return new Pair<>(expected1, expected2);
    }

    @Override
    public double getScore(T item) {
        return ratings.get(item);
    }

    private Map<T, Double> mapScores(Map<T, Double> scores) {
        double min = scores.values().stream().min(Double::compare).get();
        double max = scores.values().stream().max(Double::compare).get();
        double range = max - min;
        if (range == 0) {
            return scores.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, i -> .5));
        }
        return scores.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, i -> 1 - (i.getValue() - min) / range));
    }

    @Override
    protected void clearScores() {
        ratings.clear();
    }
}
