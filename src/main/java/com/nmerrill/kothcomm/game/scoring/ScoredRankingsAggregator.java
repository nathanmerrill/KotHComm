package com.nmerrill.kothcomm.game.scoring;

import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.set.MutableSet;

public class ScoredRankingsAggregator<T> extends IncrementalAggregator<Scoreboard<T>>{
    private final double win, loss, tie;
    public ScoredRankingsAggregator(double win, double tie, double loss){
        this.win = win;
        this.tie = tie;
        this.loss = loss;
    }

    public ScoredRankingsAggregator(){
        this(3, 1, 0);
    }

    @Override
    public Scoreboard<T> update(Scoreboard<T> recent, Scoreboard<T> currentAggregate) {
        if (currentAggregate == null){
            currentAggregate = new Scoreboard<>();
            currentAggregate.setDescending();
        }
        MutableList<MutableSet<T>> rankings = recent.rank();
        MutableSet<T> top = rankings.getFirst();
        if (top.size() == 1){
            currentAggregate.addScore(top.getOnly(), win);
        } else {
            top.forEachWith(currentAggregate::addScore, tie);
        }
        rankings.remove(top);
        rankings.flatCollect(i -> i).forEachWith(currentAggregate::addScore, loss);
        return currentAggregate;
    }
}
