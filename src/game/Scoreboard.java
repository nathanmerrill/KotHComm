package game;

import utils.iterables.Pair;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Scoreboard {
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
    private final Map<Player, Double> aggregates;
    private final Map<Player, ArrayList<Double>> scores;

    public Scoreboard(Collection<Player> players, Function<List<Double>, Double> aggregator, boolean maxScoring) {
        this.aggregator = aggregator;
        this.maxScoring = maxScoring;
        this.aggregates = new HashMap<>();
        this.scores = players.stream().collect(Collectors.toMap(Function.identity(), i -> new ArrayList<>()));
    }

    public Scoreboard(Collection<Player> players, Function<List<Double>, Double> aggregator) {
        this(players, aggregator, true);
    }

    public Scoreboard(Collection<Player> players) {
        this(players, Scoreboard::sumAggregator);
    }

    public void addScores(Scoreboard board) {
        scores.keySet().forEach(i -> addScore(i, board.getAggregatedScore(i)));
    }
    public void addScore(Player player, double score) {
        List<Double> history = scores.get(player);
        history.add(score);
        aggregates.remove(player);
    }
    public List<Double> getPlayerHistory(Player player) {
        return new ArrayList<>(scores.get(player));
    }
    public Double getAggregatedScore(Player player) {
        if (aggregates.containsKey(player)) {
            return aggregates.get(player);
        }
        Double aggregate = aggregator.apply(getPlayerHistory(player));
        aggregates.put(player, aggregate);
        return aggregate;
    }
    public List<Pair<Player, Double>> playerAggregates() {
        List<Pair<Player, Double>> aggregates = scores.keySet().stream()
                .map(i -> new Pair<>(i, getAggregatedScore(i)))
                .sorted(Comparator.comparing(Pair::second))
                .collect(Collectors.toList());
        if (maxScoring){
            Collections.reverse(aggregates);
        }
        return aggregates;
    }

    public PlayerRanking playerRanking() {
        return new PlayerRanking(playerAggregates().stream().map(Pair::first).collect(Collectors.toList()));
    }

    public void clear(){
        aggregates.clear();
        scores.clear();
    }

    public boolean isMaxScoring() {
        return maxScoring;
    }
}
