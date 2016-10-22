package com.ppcg.kothcomm.game.scoring;

import com.ppcg.kothcomm.utils.iterables.PermutationIterable;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.list.primitive.MutableDoubleList;
import org.eclipse.collections.api.map.primitive.MutableObjectDoubleMap;
import org.eclipse.collections.api.tuple.Pair;
import org.eclipse.collections.impl.factory.primitive.ObjectDoubleMaps;


public class EloAggregator<T> implements Aggregator<Scoreboard<T>> {
    public static final double DEFAULT_K_FACTOR = 32;
    public static final double STARTING_SCORE = 1000;

    private final double kFactor;


    public EloAggregator(double kFactor){
        this.kFactor = kFactor;
    }

    public EloAggregator() {
        this(DEFAULT_K_FACTOR);
    }

    @Override
    public Scoreboard<T> aggregate(MutableList<Scoreboard<T>> scoreboards) {
        Scoreboard<T> ratings = new Scoreboard<>(STARTING_SCORE);
        scoreboards.forEach(scoreboard -> {
            MutableObjectDoubleMap<T> scores = scoreboard.scores();
            normalize(scores);
            MutableObjectDoubleMap<T> updates = ObjectDoubleMaps.mutable.empty();
            for (MutableList<T> pairing : new PermutationIterable<>(scores.keySet())) {
                MutableList<Pair<T, Double>> expected = pairing.zip(expectedScore(pairing, ratings));
                expected.forEach(pair -> {
                    T item = pair.getOne();
                    updates.put(item, newRating(ratings.getScore(item), pair.getTwo(), scores.get(item)));
                });
            }
        });
        return ratings;
    }

    private double newRating(double currentRating, double expected, double score){
        return currentRating + kFactor*(expected-score);
    }

    private MutableList<Double> expectedScore(MutableList<T> matchup, Scoreboard<T> ratings) {
        MutableDoubleList adjusted = matchup.collectDouble(i -> Math.pow(10, ratings.getScore(i)/400));
        double sum = adjusted.sum();
        return adjusted.collect(i -> i/sum);
    }

    private void normalize(MutableObjectDoubleMap<T> scores) {
        double min = scores.min();
        double max = scores.max();
        double range = max - min;
        scores.keysView().forEach(i -> scores.updateValue(i, 0.5, v -> (v - min)/range));
    }

}
