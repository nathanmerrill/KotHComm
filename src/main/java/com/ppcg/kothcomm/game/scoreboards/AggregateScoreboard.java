package com.ppcg.kothcomm.game.scoreboards;

import com.ppcg.kothcomm.utils.Pair;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class AggregateScoreboard<T> extends Scoreboard<T> {

    public static Double sumAggregator(List<Double> list) {
        return list.stream().reduce(0.0, Double::sum);
    }

    public static Double maxAggregator(List<Double> list) {
        return list.stream().max(Double::compare).orElse(0.0);
    }

    public static Double minAggregator(List<Double> list) {
        return list.stream().min(Double::compare).orElse(0.0);
    }

    public static Double meanAggregator(List<Double> list) {
        if (list.isEmpty()){
            return 0.0;
        }
        return sumAggregator(list) / list.size();
    }

    private final Function<List<Double>, Double> aggregator;
    private final Map<T, Double> aggregates;
    private final Map<T, ArrayList<Double>> scores;

    public AggregateScoreboard(Function<List<Double>, Double> aggregator) {
        this.aggregator = aggregator;
        this.aggregates = new HashMap<>();
        this.scores = new HashMap<>();
    }

    public AggregateScoreboard() {
        this(AggregateScoreboard::sumAggregator);
    }

    public void addScores(AggregateScoreboard<T> board) {
        addScores(board.getScores());
    }

    public void addScore(T item, double score) {
        scores.putIfAbsent(item, new ArrayList<>());
        scores.get(item).add(score);
        aggregates.remove(item);
        super.addItem(item);
    }

    public void addScore(Pair<T, Double> score){
        addScore(score.first(), score.second());
    }


    public List<Double> getPlayerHistory(T item) {
        return new ArrayList<>(scores.getOrDefault(item, new ArrayList<>()));
    }
    public double getScore(T item) {
        if (aggregates.containsKey(item)) {
            return aggregates.get(item);
        }
        Double aggregate = aggregator.apply(getPlayerHistory(item));
        aggregates.put(item, aggregate);
        return aggregate;
    }

    @Override
    protected void update(Map<T, Double> scores) {
        scores.forEach(this::addScore);
    }


    public void clearScores(){
        aggregates.clear();
        scores.clear();
    }

    @Override
    public String toString() {
        return this.scoreTable();
    }
}
