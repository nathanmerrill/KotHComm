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
    private final Directory<T> directory;
    private final Map<PlayerType<T>, ArrayList<Double>> scores;

    public Scoreboard(Directory<T> directory, Function<List<Double>, Double> aggregator, boolean maxScoring) {
        this.aggregator = aggregator;
        this.maxScoring = maxScoring;
        this.aggregates = new HashMap<>();
        this.directory = directory;
        this.scores = directory.allPlayers().stream().collect(Collectors.toMap(Function.identity(), i -> new ArrayList<>()));
    }

    public Scoreboard(Directory<T> directory, Function<List<Double>, Double> aggregator) {
        this(directory, aggregator, true);
    }

    public Scoreboard(Directory<T> directory) {
        this(directory, Scoreboard::sumAggregator);
    }

    public void addScores(Scoreboard<T> board) {
        scores.keySet().forEach(i -> addScore(i, board.getAggregatedScore(i)));
    }
    public void addScore(T player, double score){
        addScore(directory.getType(player.getClass()), score);
    }
    public void addScore(PlayerType<T> player, double score) {
        List<Double> history = scores.get(player);
        history.add(score);
        aggregates.remove(player);
    }
    public List<Double> getPlayerHistory(PlayerType<T> player) {
        return new ArrayList<>(scores.get(player));
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
        List<Pair<PlayerType<T>, Double>> aggregates = scores.keySet().stream()
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
        for (int i = 0; i < aggregates.size(); i++){
            builder.append(i).append(".\t");
            builder.append(aggregates.get(i).second()).append("\t");
            builder.append(aggregates.get(i).first().getName());
            builder.append('\n');
        }
        return builder.toString();
    }
}
