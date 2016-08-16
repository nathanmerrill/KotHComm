package KoTHComm.game;

import KoTHComm.utils.Pair;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Scoreboard<T> implements Iterable<T>, Comparator<T> {

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
    private final boolean maxScoring;
    private final Map<T, Double> aggregates;
    private final Map<T, ArrayList<Double>> scores;

    public Scoreboard(Function<List<Double>, Double> aggregator, boolean maxScoring) {
        this.aggregator = aggregator;
        this.maxScoring = maxScoring;
        this.aggregates = new HashMap<>();
        this.scores = new HashMap<>();
    }

    public Scoreboard(Function<List<Double>, Double> aggregator) {
        this(aggregator, true);
    }

    public Scoreboard(boolean maxScoring){
        this(Scoreboard::sumAggregator, maxScoring);
    }

    public Scoreboard() {
        this(Scoreboard::sumAggregator);
    }

    public void addScores(Scoreboard<T> board) {
        addScores(board.getScores());
    }

    public void addScores(Iterable<Pair<T, Double>> scores){
        scores.forEach(this::addScore);
    }

    public void addScore(Pair<T, Double> score){
        addScore(score.first(), score.second());
    }

    public void addScore(T item, double score) {
        scores.putIfAbsent(item, new ArrayList<>());
        scores.get(item).add(score);
        aggregates.remove(item);
    }
    public List<Double> getPlayerHistory(T item) {
        return new ArrayList<>(scores.getOrDefault(item, new ArrayList<>()));
    }
    public Double getScore(T item) {
        if (aggregates.containsKey(item)) {
            return aggregates.get(item);
        }
        Double aggregate = aggregator.apply(getPlayerHistory(item));
        aggregates.put(item, aggregate);
        return aggregate;
    }
    public List<Pair<T, Double>> getScores() {
        List<Pair<T, Double>> aggregates = scores.entrySet().stream()
                .filter(i -> !i.getValue().isEmpty())
                .map(Map.Entry::getKey)
                .map(i -> new Pair<>(i, getScore(i)))
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

    public List<T> items(){
        return new ArrayList<>(scores.keySet());
    }

    public List<T> getPlayersRanked(){
        return stream().collect(Collectors.toList());
    }

    @Override
    public Iterator<T> iterator() {
        return stream().iterator();
    }

    public boolean isMaxScoring() {
        return maxScoring;
    }

    @Override
    public int compare(T o1, T o2) {
        return (int) Math.signum(getScore(o1)-getScore(o2));
    }

    public Stream<T> stream(){
        return getScores().stream().map(Pair::first);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        List<Pair<T, Double>> aggregates = getScores();
        if (aggregates.isEmpty()){
            return "No scores";
        }
        int currentRank = 1;
        double lastScore = aggregates.get(0).second();
        for (int i = 0; i < aggregates.size(); i++){
            String name = aggregates.get(i).first().toString().split("\n")[0];
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
