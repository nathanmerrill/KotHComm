package com.ppcg.kothcomm.game.scoreboards;

import com.ppcg.kothcomm.utils.Pair;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class Scoreboard<T> implements Iterable<T>, Comparator<T> {

    private Set<T> items;
    private boolean maxOrdered;

    public Scoreboard(boolean maxOrdered){
        this.maxOrdered = maxOrdered;
        items = new HashSet<>();
    }
    public Scoreboard(){
        this(true);
    }

    protected abstract void update(Map<T, Double> scores);

    public abstract double getScore(T item);

    protected abstract void clearScores();

    protected final void addItem(T item){
        this.items.add(item);
    }

    public final int size(){
        return items.size();
    }

    public final boolean isEmpty() {
        return items.isEmpty();
    }

    public final boolean contains(T item){
        return items.contains(item);
    }

    public final void setMaxOrdered(boolean maxOrdered) {
        this.maxOrdered = maxOrdered;
    }

    public final boolean isMaxOrdered() {
        return maxOrdered;
    }

    public final void addScores(Scoreboard<T> board) {
        addScores(board.getScores());
    }

    public final void addScores(Map<T, Double> scores){
        items.addAll(scores.keySet());
        update(scores);
    }

    public final Map<T, Double> getScores(){
        return items().stream().collect(Collectors.toMap(Function.identity(), this::getScore));
    }

    public final List<Pair<T, Double>> getScoresOrdered(){
        List<Pair<T, Double>> scores =
                Pair.fromMap(getScores()).stream()
                .sorted(Comparator.comparing(Pair::second))
                .collect(Collectors.toList());
        if (maxOrdered){
            Collections.reverse(scores);
        }
        return scores;
    }

    public final void clear(){
        items.clear();
        clearScores();
    }

    public final List<T> items(){
        return new ArrayList<>(items);
    }

    public final List<T> itemsOrdered(){
        return stream().collect(Collectors.toList());
    }

    @Override
    public final Iterator<T> iterator() {
        return stream().iterator();
    }

    @Override
    public final int compare(T o1, T o2) {
        return (int) Math.signum(getScore(o1)-getScore(o2));
    }

    public final Stream<T> stream(){
        return getScores().keySet().stream();
    }

    public final Stream<T> streamOrdered(){
        return getScoresOrdered().stream().map(Pair::first);
    }

    public final String scoreTable() {
        StringBuilder builder = new StringBuilder();
        List<Pair<T, Double>> aggregates = getScoresOrdered();
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
