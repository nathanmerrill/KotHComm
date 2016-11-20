package com.nmerrill.kothcomm.game.scoring;

import org.eclipse.collections.api.bag.Bag;
import org.eclipse.collections.api.bag.MutableBag;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.impl.factory.Bags;

public final class STVAggregator<T> implements Aggregator<Scoreboard<T>> {

    @Override
    public Scoreboard<T> aggregate(MutableList<? extends Scoreboard<T>> scores) {
        MutableBag<MutableList<T>> votes = scores.collect(Scoreboard::rank).toBag();
        Scoreboard<T> scoreboard = new Scoreboard<>();
        votes.flatCollect(i -> i).forEachWith(scoreboard::addScore, 0.0);

        while (!votes.isEmpty()) {
            countVotes(votes).bottomOccurrences(1).forEach(p -> {
                T bottom = p.getOne();
                scoreboard.addScore(bottom, p.getTwo());
                MutableBag<MutableList<T>> toRemove = votes.select(list -> list.get(0).equals(bottom));
                votes.removeAll(toRemove);
                toRemove.forEachWithOccurrences((list, count) -> {
                    if (list.size() > 2){
                        votes.addOccurrences(list.subList(1, list.size()), count);
                    }
                });
            });
        }
        scoreboard.setDescending();
        return scoreboard;
    }


    public MutableBag<T> countVotes(Bag<MutableList<T>> votes){
        MutableBag<T> counts = Bags.mutable.empty();
        votes.forEachWithOccurrences((list, count)-> {
            counts.addOccurrences(list.get(0), count);
        });
        return counts;
    }


}
