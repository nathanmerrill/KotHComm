package com.nmerrill.kothcomm.game.scoring;

import com.nmerrill.kothcomm.utils.Cache;
import org.eclipse.collections.api.block.function.Function;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.list.primitive.DoubleList;
import org.eclipse.collections.api.map.primitive.MutableObjectDoubleMap;
import org.eclipse.collections.api.set.MutableSet;
import org.eclipse.collections.api.tuple.primitive.ObjectDoublePair;
import org.eclipse.collections.impl.factory.primitive.DoubleLists;
import org.eclipse.collections.impl.factory.primitive.ObjectDoubleMaps;

import java.util.Comparator;
import java.util.Iterator;


public final class Scoreboard<T> implements Comparator<T>, Iterable<T> {
    private final MutableObjectDoubleMap<T> scores;
    private final Cache<MutableList<T>> ranking;
    private final double defaultScore;
    private int ordering;
    private boolean showScores;

    public Scoreboard(double defaultScore){
        scores = ObjectDoubleMaps.mutable.empty();
        ranking = new Cache<>();
        ordering = 1;
        this.defaultScore = defaultScore;
        this.showScores = true;
    }

    public void showScores() {
        this.showScores = true;
    }

    public void hideScores() {
        this.showScores = false;
    }

    public Scoreboard(){
        this(0);
    }

    public MutableSet<T> items(){
        return scores.keysView().toSet();
    }


    public MutableObjectDoubleMap<T> scores(){
        return ObjectDoubleMaps.mutable.ofAll(scores);
    }

    public MutableList<ObjectDoublePair<T>> scoresOrdered(){
        return scores.keyValuesView().toSortedList((i, j) -> this.compare(i.getOne(), j.getOne()));
    }

    public boolean isAscending() {
        return ordering == 1;
    }

    public void setAscending() {
        ordering = 1;
    }

    public void setDescending(){
        ordering = -1;
    }

    public void addScore(T item, double score){
        scores.put(item, score);
        ranking.breakCache();
    }

    public double getScore(T item){
        return scores.getIfAbsentPut(item, defaultScore);
    }


    private MutableList<T> calculateRankings(){
        return scores.keysView().toSortedList(this);
    }

    @Override
    public int compare(T o1, T o2) {
        return ordering*Double.compare(scores.get(o1), scores.get(o2));
    }

    public MutableList<T> rank() {
        return ranking.get(this::calculateRankings);
    }

    public String scoreTable() {
        StringBuilder builder = new StringBuilder();
        if (scores.isEmpty()){
            return "No scores";
        }
        int currentRank = 1;
        int index = 0;
        double lastScore = Double.MIN_VALUE;
        for (ObjectDoublePair pair :scoresOrdered()){
            String name = pair.getOne().toString().split("\n")[0];
            double score = pair.getTwo();
            if (score != lastScore){
                currentRank = index+1;
            }
            index++;
            builder.append(currentRank).append(".\t");
            if (showScores) {
                builder.append(score).append("\t");
            }
            builder.append(name);
            builder.append('\n');
        }
        return builder.toString();
    }

    public <U> Scoreboard<U> mapScoreboard(Function<T, U> map, Function<DoubleList, Double> aggregator){
        Scoreboard<U> scoreboard = new Scoreboard<>();
        this.scores
                .keyValuesView()
                .aggregateInPlaceBy(p -> map.apply(p.getOne()), DoubleLists.mutable::empty, (l, p) -> l.add(p.getTwo()))
                .forEachKeyValue((j,l) -> scoreboard.addScore(j, aggregator.valueOf(l)));
        return scoreboard;
    }

    static <T> Scoreboard<T> toScoreboard(MutableList<T> ordered){
        Scoreboard<T> scoreboard = new Scoreboard<>();
        ordered.zipWithIndex().forEach(p -> scoreboard.addScore(p.getOne(), p.getTwo()));
        return scoreboard;
    }


    public int size(){
        return scores.size();
    }

    public boolean isEmpty() {
        return scores.isEmpty();
    }

    public boolean contains(T item){
        return scores.containsKey(item);
    }

    @Override
    public Iterator<T> iterator() {
        return rank().iterator();
    }

    public MutableList<T> toList() {
        return rank().toList();
    }

}
