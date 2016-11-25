package com.nmerrill.kothcomm.game.scoring;

import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.impl.factory.Lists;

public abstract class IncrementalAggregator<T> implements Aggregator<T>{

    private T current;
    private MutableList<? extends T> history;

    public IncrementalAggregator(){
        this.history = Lists.mutable.empty();
        current = null;
    }

    public T aggregate(MutableList<? extends T> scores){
        if (scores.size() >= history.size()){
            if (this.history.zip(scores).allSatisfy(p -> p.getOne().equals(p.getTwo()))){
                updateAll(scores.subList(history.size(), scores.size()));
                history = scores.clone();
            }
        } else {
            current = null;
            updateAll(scores);
        }
        return current;
    }

    private void updateAll(MutableList<? extends T> list){
        for (T recent: list){
            current = update(recent, current);
        }
    }

    public abstract T update(T recent, T currentAggregate);

}
