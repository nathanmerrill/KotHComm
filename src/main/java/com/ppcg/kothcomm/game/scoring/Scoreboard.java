package com.ppcg.kothcomm.game.scoring;

import com.ppcg.kothcomm.utils.Cache;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.map.primitive.MutableObjectDoubleMap;
import org.eclipse.collections.api.set.MutableSet;
import org.eclipse.collections.api.set.sorted.MutableSortedSet;
import org.eclipse.collections.api.tuple.primitive.ObjectDoublePair;
import org.eclipse.collections.impl.factory.SortedSets;
import org.eclipse.collections.impl.factory.primitive.ObjectDoubleMaps;


public class Scoreboard<T> implements Ranking<T> {
    private final MutableObjectDoubleMap<T> scores;
    private final Cache<MutableSortedSet<T>> ranking;
    private final double defaultScore;
    private int ordering;

    public Scoreboard(double defaultScore){
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


    private MutableSortedSet<T> calculateRankings(){
        return SortedSets.mutable.ofAll(this, scores.keySet());
    }

    @Override
    public int compare(T o1, T o2) {
        return ordering*Double.compare(scores.get(o1), scores.get(o2));
    }

    @Override
    public MutableSortedSet<T> rank() {
        return ranking.get(this::calculateRankings);
    }

    public String scoreTable() {
        StringBuilder builder = new StringBuilder();
        if (scores.isEmpty()){
            return "No scores";
        }
        int currentRank = 1;
        int index = 1;
        double lastScore = Double.MIN_VALUE;
        for (ObjectDoublePair pair :scores.keyValuesView()){
            String name = pair.getOne().toString().split("\n")[0];
            double score = pair.getTwo();
            if (score != lastScore){
                currentRank = index+1;
            }
            index++;
            builder.append(currentRank).append(".\t");
            builder.append(score).append("\t");
            builder.append(name);
            builder.append('\n');
        }
        return builder.toString();
    }
}
