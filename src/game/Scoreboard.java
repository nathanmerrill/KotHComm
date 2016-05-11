package game;

import utils.iterables.Pair;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Scoreboard<T> implements Iterable<PlayerType<T>>{
    public static Double sumAggregator(List<Double> list) {
        return list.stream().reduce(0.0, Double::sum);
    }

    public static Double minAggregator(List<Double> list) {
        return list.stream().min(Double::compare).orElse(0.0);
    }

    public static Double maxAggregator(List<Double> list) {
        return list.stream().max(Double::compare).orElse(0.0);
    }

    public static Double meanAggregator(List<Double> list) {
        return sumAggregator(list) / list.size();
    }

    private final Function<List<Double>, Double> aggregator;
    private final boolean maxScoring;
    private final Map<PlayerType<T>, Double> aggregates;
    private final Map<PlayerType<T>, ArrayList<Double>> scores;

    public Scoreboard(Function<List<Double>, Double> aggregator, boolean maxScoring) {
        this.aggregator = aggregator;
        this.maxScoring = maxScoring;
        this.aggregates = new HashMap<>();
        this.scores = new HashMap<>();
    }

    public Scoreboard(Function<List<Double>, Double> aggregator) {
        this(aggregator, true);
    }

    public Scoreboard() {
        this(Scoreboard::sumAggregator);
    }

    public void addScores(Scoreboard<T> board) {
        board.playerAggregates().forEach(a -> this.addScore(a.first(), a.second()));
    }
    public void addScore(PlayerType<T> player, double score) {
        scores.putIfAbsent(player, new ArrayList<>());
        scores.get(player).add(score);
        aggregates.remove(player);
    }
    public List<Double> getPlayerHistory(PlayerType<T> player) {
        return new ArrayList<>(scores.getOrDefault(player, new ArrayList<>()));
    }
    public Double getAggregatedScore(PlayerType<T> player) {
        if (aggregates.containsKey(player)) {
            return aggregates.get(player);
        }
        Double aggregate = aggregator.apply(getPlayerHistory(player));
        aggregates.put(player, aggregate);
        return aggregate;
    }
    public List<Pair<PlayerType<T>, Double>> playerAggregates() {
        List<Pair<PlayerType<T>, Double>> aggregates = scores.entrySet().stream()
                .filter(i -> !i.getValue().isEmpty())
                .map(Map.Entry::getKey)
                .map(i -> new Pair<>(i, getAggregatedScore(i)))
                .sorted(Comparator.comparing(Pair::second))
                .collect(Collectors.toList());
        if (maxScoring){
            Collections.reverse(aggregates);
        }
        return aggregates;
    }


    public void clear(){
        aggregates.clear();
        scores.clear();
    }

    @Override
    public Iterator<PlayerType<T>> iterator() {
        return playerAggregates().stream().map(Pair::first).iterator();
    }

    public boolean isMaxScoring() {
        return maxScoring;
    }


    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        List<Pair<PlayerType<T>, Double>> aggregates = playerAggregates();
        if (aggregates.isEmpty()){
            return "No scores";
        }
        int currentRank = 1;
        double lastScore = aggregates.get(0).second();
        for (int i = 0; i < aggregates.size(); i++){
            String name = aggregates.get(i).first().getName();
            double score = aggregates.get(i).second();
            if (score != lastScore){
                currentRank = i+1;
            }
            builder.append(currentRank).append(".\t");
            builder.append(score).append("\t");
            builder.append(name);
            builder.append('\n');
        }
        return builder.toString();
    }
}
