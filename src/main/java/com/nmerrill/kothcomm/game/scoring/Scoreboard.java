package com.nmerrill.kothcomm.game.scoring;

import com.nmerrill.kothcomm.utils.Cache;
import org.eclipse.collections.api.RichIterable;
import org.eclipse.collections.api.block.function.Function;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.list.primitive.DoubleList;
import org.eclipse.collections.api.map.primitive.MutableObjectDoubleMap;
import org.eclipse.collections.api.set.MutableSet;
import org.eclipse.collections.api.tuple.primitive.ObjectDoublePair;
import org.eclipse.collections.impl.factory.Lists;
import org.eclipse.collections.impl.factory.Sets;
import org.eclipse.collections.impl.factory.primitive.DoubleLists;
import org.eclipse.collections.impl.factory.primitive.ObjectDoubleMaps;

import java.util.Comparator;


public final class Scoreboard<T> implements Comparator<T> {
    private final MutableObjectDoubleMap<T> scores;
    private final Cache<MutableList<MutableSet<T>>> ranking;
    private final double defaultScore;
    private int ordering;

    public Scoreboard(double defaultScore) {
        scores = ObjectDoubleMaps.mutable.empty();
        ranking = new Cache<>();
        ordering = 1;
        this.defaultScore = defaultScore;
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
        return ordering == -1;
    }

    public void setAscending() {
        ordering = 1;
    }

    public void setDescending(){
        ordering = -1;
    }

    public void addScore(T item, double score){
        scores.updateValue(item, 0, d -> d+score);
    }

    public void addScores(RichIterable<T> items, double score){
        items.forEachWith(this::addScore, score);
    }

    public void setScore(T item, double score){
        scores.put(item, score);
        ranking.breakCache();
    }

    public double getScore(T item){
        return scores.getIfAbsentPut(item, defaultScore);
    }


    private MutableList<MutableSet<T>> calculateRankings(){
        MutableList<T> rankings = scores.keysView().toSortedList(this);
        MutableList<MutableSet<T>> withTies = Lists.mutable.empty();
        double currentScore = 0;
        MutableSet<T> currentSet = null;
        for (T item: rankings){
            if (currentSet == null || currentScore != getScore(item)){
                currentSet = Sets.mutable.empty();
                currentScore = getScore(item);
                withTies.add(currentSet);
            }
            currentSet.add(item);
        }
        return withTies;
    }

    @Override
    public int compare(T o1, T o2) {
        return ordering*Double.compare(scores.get(o1), scores.get(o2));
    }

    public MutableList<MutableSet<T>> rank() {
        return ranking.get(this::calculateRankings);
    }

    public <U> Scoreboard<U> mapScoreboard(Function<T, U> map, Function<DoubleList, Double> aggregator){
        Scoreboard<U> scoreboard = new Scoreboard<>();
        this.scores
                .keyValuesView()
                .aggregateInPlaceBy(p -> map.apply(p.getOne()), DoubleLists.mutable::empty, (l, p) -> l.add(p.getTwo()))
                .forEachKeyValue((j,l) -> scoreboard.setScore(j, aggregator.valueOf(l)));
        scoreboard.ordering = ordering;
        return scoreboard;
    }

    static <T> Scoreboard<T> toScoreboard(MutableList<T> ordered){
        Scoreboard<T> scoreboard = new Scoreboard<>();
        ordered.zipWithIndex().forEach(p -> scoreboard.setScore(p.getOne(), p.getTwo()));
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


}
